package org.laganini.lagano.snpashot.name

import org.laganini.lagano.version.Version

interface NamingStrategy<T : Comparable<T>> {

    fun resolve(filename: String): Version<T>?

    fun next(current: Version<T>): Version<T>?

    fun initial(): Version<T>?

    fun build(version: Version<T>): String

}