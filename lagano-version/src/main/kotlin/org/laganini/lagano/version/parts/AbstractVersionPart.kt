package org.laganini.lagano.version.parts

abstract class AbstractVersionPart<T> : VersionPart<T> {

    override fun supports(value: String): Boolean {
        return try {
            parse(value)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun build(value: T): String {
        return value.toString()
    }

}