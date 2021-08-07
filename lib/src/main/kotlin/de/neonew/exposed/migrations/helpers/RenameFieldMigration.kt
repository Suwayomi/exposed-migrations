package de.neonew.exposed.migrations.helpers

/*
 * Copyright (C) Contributors to the Suwayomi project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

import de.neonew.exposed.migrations.Migration
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.vendors.currentDialect

abstract class RenameFieldMigration(
    tableName: String,
    originalName: String,
    newName: String,
) : SQLMigration() {
    private val fixedTableName by lazy { tableName.toSqlName() }
    private val fixedOriginalName by lazy { originalName.toSqlName() }
    private val fixedNewName by lazy { newName.toSqlName() }

    override val sql by lazy {
        "ALTER TABLE $fixedTableName " +
            "ALTER COLUMN $fixedOriginalName RENAME TO $fixedNewName"
    }
}
