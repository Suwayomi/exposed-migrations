# exposed-migrations
A fork of [exposed-migrations](https://gitlab.com/andreas-mausch/exposed-migrations) 
by [Andreas Mausch](https://gitlab.com/andreas-mausch), licenced under `MIT`.
You can find a copy of the license [here](./LICENSE) 

# Original README

This library is a super-basic migration tool for the Kotlin SQL Framework Exposed by Jetbrains.

See this issue: https://github.com/JetBrains/Exposed/issues/165

Currently, only migrations to a higher version are possible, downgrades are not.

# Installation
This package is published using jitpack. Head to https://jitpack.io/#Suwayomi/exposed-migrations

# Sample usage
Put all your migrations in the same package, for example `com.your.program.migration`.

Create a class named `MXXXX` like bellow, `XXXX` being the number of this migration class.

*Note: you can append anything to `MXXXX` for extra context, i.e. `M0001_FirstMigration`*

```kotlin
package com.your.program.migration

class M0001 : Migration() {
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
val migrations = loadMigrationsFrom("com.your.program.migration", AnyClassFromYourCode::class.java)
runMigrations(migrations)
```

or list your migrations manually

```kotlin
runMigrations(listOf(M0001()))
```

The line above will find all classes named `MXXXX(.*)` and apply them in order
 of the number after `M`.

# SQL details

A table named `MIGRATIONS` is used to store all executed migrations.
It is used to find the current state of the database and to determine which migrations still need to be executed.

 