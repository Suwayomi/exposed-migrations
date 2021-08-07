package de.neonew.exposed.migrations.helpers

/*
 * Copyright (C) Contributors to the Suwayomi project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

abstract class AddColumnMigration(
    tableName: String,
    columnName: String,
    columnType: String,
    default: String,
) : SQLMigration() {
    private val fixedTableName by lazy { tableName.toSqlName() }
    private val fixedColumnName by lazy { columnName.toSqlName() }

    override val sql by lazy {
        "ALTER TABLE $fixedTableName" +
            " ADD COLUMN $fixedColumnName $columnType DEFAULT $default"
    }
}
