package org.laganini.lagano.version.parts

interface VersionPart<T> {

    fun supports(value: String): Boolean

    fun parse(value: String): T

    fun build(value: T): String

    fun increment(value: T): T

    fun decrement(value: T): T

}