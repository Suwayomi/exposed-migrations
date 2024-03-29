# exposed-migrations

This library is a super-basic migration tool for the Kotlin SQL Framework Exposed by Jetbrains.

See this issue: https://github.com/JetBrains/Exposed/issues/165

Currently, only migrations to a higher version are possible, downgrades are not.

# Installation
This package is published using jitpack. Head to https://jitpack.io/#Suwayomi/exposed-migrations for instructions.

# Sample usage
Put all your migrations in the same package, for example `com.your.program.migration`.

Create a class named `MXXXX` like bellow, `XXXX` being the number of this migration class.

*Note: you can append anything to `MXXXX` for extra context, i.e. `M0001_FirstMigration`*

```kotlin
package com.your.program.migration

class M0001 : Migration() {
  /** a static snapshot of [SomeTable] */
  private class SomeTable : IntIdTable() {
    init {
      integer("someField")

      index(true, someField)
    }
  }

  override fun run() {
    SchemaUtils.create(SomeTestTable)
  }
}
```

and run the migration(s)

```kotlin
val migrations = loadMigrationsFrom("com.your.program.migration", AnyClassFromYourCode::class.java)
runMigrations(migrations)
```
*Note: the line above will find all migration classes named according to 
the regex `^M(\\d+)(.*)$` and apply them in order of the number after `M`.
`AnyClassFromYourCode` is needed to get a java ClassLoader*


or list your migrations manually

```kotlin
runMigrations(listOf(M0001()))
```

# Migration helpers
Explore [`de.neonew.exposed.migrations.helpers`](./lib/src/main/kotlin/de/neonew/exposed/migrations/helpers) 
for some useful Migration helper classes to make writing them easier. Pull requests for additional helpers is welcomed.

For example to rename a field you can have:
```kotlin
package com.your.program.migration

import de.neonew.exposed.migrations.helpers.RenameFieldMigration

/** Fifth migration: Renamed Order's quantity field to use a more verbose name*/ 
class M0005_OrderTableQuantityRename : RenameFieldMigration(
    "Order", // table name
    "qtty", // old field name
    "quantity" // new field name
)
```


# SQL details

A table named `MIGRATIONS` is used to store all executed migrations.
It is used to find the current state of the database and to determine which migrations still need to be executed.


# Credit
A fork of [exposed-migrations](https://gitlab.com/andreas-mausch/exposed-migrations) 
by [Andreas Mausch](https://gitlab.com/andreas-mausch), licenced under `MIT`. New files
added to the project in the Suwayomi fork are licensed under `MPL 2.0`, if a file doesn't
include a license header then it's covered by the `MIT` license.

    MIT License
    
    Copyright (c) 2020 Andreas Mausch
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
