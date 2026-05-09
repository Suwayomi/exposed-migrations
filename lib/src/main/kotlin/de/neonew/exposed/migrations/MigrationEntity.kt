package de.neonew.exposed.migrations

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.datetime.timestamp

object MigrationsTable : IdTable<Int>() {
    override val id = integer("version").entityId()
    override val primaryKey = PrimaryKey(id)

    val name = varchar("name", length = 400)
    val executedAt = timestamp("executed_at")

    init {
        index(true, name)
    }
}

class MigrationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MigrationEntity>(MigrationsTable)

    var version by MigrationsTable.id
    var name by MigrationsTable.name
    var executedAt by MigrationsTable.executedAt
}
