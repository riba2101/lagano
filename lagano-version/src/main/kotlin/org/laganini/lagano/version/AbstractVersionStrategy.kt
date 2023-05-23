package org.laganini.lagano.version

import org.laganini.lagano.version.parts.VersionPart

abstract class AbstractVersionStrategy<T : Comparable<T>> protected constructor(
    val parts: List<VersionPart<T>>,
    val separator: String = SEPARATOR
) : VersionStrategy<T> {

    init {
        if (parts.isEmpty()) {
            throw IllegalArgumentException("Provided version parts are empty")
        }
    }

    override fun supports(value: String): Boolean {
        val values = value.split(separator)
        if (values.size != parts.size) {
            return false
        }

        return IntRange(0, parts.size - 1)
            .toList()
            .stream()
            .allMatch { i -> parts[i].supports(values[i]) }
    }

    override fun resolve(value: String): Version<T> {
        return Version(parse(value), separator)
    }

    override fun parse(value: String): List<T> {
        val values = value.split(separator)

        return IntRange(0, parts.size - 1)
            .toList()
            .map { i -> parts[i].parse(values[i]) }
    }

    override fun build(version: Version<T>): String {
        return version.values.joinToString(separator)
    }

    companion object {
        const val SEPARATOR: String = "."
    }

}