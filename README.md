# exposed-migrations
A fork of [exposed-migrations](https://gitlab.com/andreas-mausch/exposed-migrations) 
by [Andreas Mausch](https://gitlab.com/andreas-mausch), Originally licenced under `MIT`.

All changes are licenced under `Mozilla Public License 2.0`.

You can find a copy of `MIT` [here](./LICENSE_MIT) 
and a copy of `Mozilla Public License 2.0` [here](./LICENSE_MPL).

# Original README

This library is a super-basic migration tool for the Kotlin SQL Framework Exposed by Jetbrains.

See this issue: https://github.com/JetBrains/Exposed/issues/165

Currently, only migrations to a higher version are possible, downgrades are not.

# Installation
This package is published using jitpack. Head to https://jitpack.io/#Suwayomi/exposed-migrations

# Sample usage
Put all your migrations in the same package, for example `com.your.program.migration`

```kotlin
package com.your.program.migration

class V1_SampleMigration : Migration() {
  object SomeTestTable : IntIdTable() {
    val someField = integer("someField")

    init {
      index(true, someField)
    }
  }

  override fun run() {
    SchemaUtils.create(SomeTestTable)
  }
}
```

and

```kotlin
loadMigrationsFrom("com.your.program.migration", AnyClassFromYourCode::class.java)
```

# SQL details

A table named `MIGRATIONS` is used to store all executed migrations.
It is used to find the current state of the database and to determine which migrations still need to be executed.

 