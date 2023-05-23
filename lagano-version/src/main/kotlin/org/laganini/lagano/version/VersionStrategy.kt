package org.laganini.lagano.version

interface VersionStrategy<T : Comparable<T>> {

    fun supports(value: String): Boolean

    fun resolve(value: String): Version<T>

    fun parse(value: String): List<T>

    fun build(version: Version<T>): String

    fun initial(): Version<T>?

    fun next(current: Version<T>): Version<T>?

}