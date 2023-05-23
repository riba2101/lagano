package org.laganini.lagano.snpashot.name

import org.laganini.lagano.version.Version

class SingleNamingStrategy(private val filename: String) : NamingStrategy<Comparable<Any>> {

    override fun resolve(filename: String): Version<Comparable<Any>> {
        return initial()
    }

    override fun next(current: Version<Comparable<Any>>): Version<Comparable<Any>> {
        return initial()
    }

    override fun initial(): Version<Comparable<Any>> {
        return Version(emptyList())
    }

    override fun build(version: Version<Comparable<Any>>): String {
        return filename
    }

}