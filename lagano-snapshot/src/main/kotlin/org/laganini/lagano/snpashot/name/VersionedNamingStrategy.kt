package org.laganini.lagano.snpashot.name

import org.laganini.lagano.version.Version
import org.laganini.lagano.version.VersionStrategy

class VersionedNamingStrategy<T : Comparable<T>>(
    private val filename: String,
    private val versionStrategy: VersionStrategy<T>,
    private val separator: String = SEPARATOR
) : NamingStrategy<T> {

    override fun resolve(filename: String): Version<T> {
        if (!filename.contains(separator)) {
            throw IllegalArgumentException("Filename: $filename does not contain the separator: $SEPARATOR")
        }

        val split = filename.split(separator)
        return versionStrategy.resolve(split.first())
    }

    override fun next(current: Version<T>): Version<T>? {
        return versionStrategy.next(current)
    }

    override fun initial(): Version<T>? {
        return versionStrategy.initial()
    }

    override fun build(version: Version<T>): String {
        return "${versionStrategy.build(version)}${separator}$filename"
    }

    companion object {
        const val SEPARATOR = "_"
    }

}