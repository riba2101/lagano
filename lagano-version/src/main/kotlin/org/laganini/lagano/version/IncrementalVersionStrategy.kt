package org.laganini.lagano.version

import org.laganini.lagano.version.parts.IntegerVersionPart

class IncrementalVersionStrategy : AbstractVersionStrategy<Int>(listOf(IntegerVersionPart())) {

    override fun next(current: Version<Int>): Version<Int> {
        return Version(
            listOf(parts[0].increment(current.values[0])),
            separator
        )
    }

    override fun initial(): Version<Int> {
        return Version(
            listOf(1),
            separator
        )
    }

}