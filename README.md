# exposed-migrations

This library is a super-basic migration tool for the Kotlin SQL Framework Exposed by Jetbrains.

See this issue: https://github.com/JetBrains/Exposed/issues/165

Currently, only migrations to a higher version are possible, downgrades are not.

# Installation
This package is published using jitpack. Head to https://jitpack.io/#Suwayomi/exposed-migrations/3.0.0 for instructions.

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

and

```kotlin
val migrations = loadMigrationsFrom("com.your.program.migration", AnyClassFromYourCode::class.java)
runMigrations(migrations)
```

or list your migrations manually

```kotlin
runMigrations(listOf(M0001()))
```

The line above will find all classes named according to 
the regex `^M(\\d+)(.*)$` and apply them in order of the number after `M`.

# SQL details

A table named `MIGRATIONS` is used to store all executed migrations.
It is used to find the current state of the database and to determine which migrations still need to be executed.


# Credit
A fork of [exposed-migrations](https://gitlab.com/andreas-mausch/exposed-migrations) 
by [Andreas Mausch](https://gitlab.com/andreas-mausch), licenced under `MIT`.

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
