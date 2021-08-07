package de.neonew.exposed.migrations.helpers

import de.neonew.exposed.migrations.Migration
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.vendors.currentDialect

/*
 * Copyright (C) Contributors to the Suwayomi project
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

abstract class SQLMigration : Migration() {
    abstract val sql: String

    override fun run() {
        with(TransactionManager.current()) {
            exec(sql)
            commit()
            currentDialect.resetCaches()
        }
    }
}