package de.neonew.exposed.migrations

import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.Closeable
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Clock
import java.time.Instant.now
import kotlin.io.path.isDirectory
import kotlin.io.path.name
// shows up as unused, however, it's required, otherwise, the jitpack build fails with:
//  "... Unresolved reference. None of the following candidates is applicable because of receiver type mismatch: ..."
import kotlin.streams.toList

private val logger = KotlinLogging.logger {}

internal lateinit var migrationsDatabase: Database

fun runMigrations(
    migrations: List<Migration>,
    database: Database = TransactionManager.defaultDatabase!!,
    clock: Clock = Clock.systemUTC(),
) {
    migrationsDatabase = database

    checkVersions(migrations)

    logger.info { "Running migrations on database ${database.url}" }

    val latestVersion = transaction(database) {
        createTableIfNotExists(database)
        MigrationEntity.all().maxByOrNull { it.version.value }?.version?.value
    }

    logger.info { "Database version before migrations: $latestVersion" }

    migrations
        .sortedBy { it.version }
        .filter { shouldRun(latestVersion, it) }
        .forEach {
            logger.info { "Running migration version ${it.version}: ${it.name}" }
            transaction(database) {
                it.run()

                MigrationEntity.new {
                    version = EntityID(it.version, MigrationsTable)
                    name = it.name
                    executedAt = now(clock)
                }
            }
        }

    logger.info { "Migrations finished successfully" }
}

private fun getTopLevelClasses(packageName: String, klass: Class<*>): List<Class<*>> {
    klass.getResource("/" + packageName.replace('.', '/'))
    val path = "/" + packageName.replace('.', '/')
    val uri = klass.getResource(path)!!.toURI()
    var closable: Closeable? = null

    return when (uri.scheme) {
        "jar" -> {
            val fileSystem = FileSystems.newFileSystem(uri, emptyMap<String, Any>())
            closable = fileSystem
            fileSystem.getPath(path)
        }
        else -> Paths.get(uri)
    }.let { Files.walk(it, 1) }
        .toList()
        .filterNot { it.isDirectory() || it.name.contains('$') } // '$' means it's not a top level class
        .filter { it.name.endsWith(".class") }
        .map { Class.forName("$packageName.${it.name.substringBefore(".class")}") }
        .also { closable?.close() }
}

fun loadMigrationsFrom(packageName: String, klass: Class<*>): List<Migration> {
    return getTopLevelClasses(packageName, klass)
        .map {
            logger.debug { "found Migration class ${it.name}" }
            val clazz = it.getDeclaredConstructor().newInstance()
            if (clazz is Migration)
                clazz
            else
                throw RuntimeException("found a class that's not a Migration")
        }
}

private fun checkVersions(migrations: List<Migration>) {
    val sorted = migrations.map { it.version }.sorted()
    if ((1..migrations.size).toList() != sorted) {
        throw IllegalStateException("List of migrations version is not consecutive: $sorted")
    }
}

private fun createTableIfNotExists(database: Database) {
    if (MigrationsTable.exists()) {
        return
    }
    val tableNames = database.dialect.allTablesNames()
    when (tableNames.isEmpty()) {
        true -> {
            logger.info { "Empty database found, creating table for migrations" }
            create(MigrationsTable)
        }
        false -> throw IllegalStateException("Tried to run migrations against a non-empty database without a Migrations table. This is not supported.")
    }
}

private fun shouldRun(latestVersion: Int?, migration: Migration): Boolean {
    val run = latestVersion?.let { migration.version > it } ?: true
    if (!run) {
        logger.debug { "Skipping migration version ${migration.version}: ${migration.name}" }
    }
    return run
}
