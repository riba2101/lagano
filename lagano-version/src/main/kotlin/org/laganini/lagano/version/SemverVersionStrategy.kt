package org.laganini.lagano.version

import org.laganini.lagano.version.parts.IntegerVersionPart

class SemverVersionStrategy :
    AbstractVersionStrategy<Int> {

    constructor() : this(SEPARATOR)

    constructor(separator: String) : super(
        listOf(
            IntegerVersionPart(),
            IntegerVersionPart(),
            IntegerVersionPart(),
        ),
        separator
    )

    override fun next(current: Version<Int>): Version<Int> {
        return patch(current)
    }

    override fun initial(): Version<Int> {
        return Version(
            listOf(1, 0, 0),
            separator
        )
    }

    fun major(version: Version<Int>): Version<Int> {
        return rebuild(version, 0)
    }

    fun minor(version: Version<Int>): Version<Int> {
        return rebuild(version, 1)
    }

    fun patch(version: Version<Int>): Version<Int> {
        return rebuild(version, 2)
    }

    private fun rebuild(version: Version<Int>, index: Int): Version<Int> {
        return Version(
            version
                .values
                .mapIndexed { i, value -> incrementOrGet(version, index, i) }
                .toList(),
            separator
        )
    }

    private fun incrementOrGet(version: Version<Int>, index: Int, i: Int): Int {
        if (i == index) {
            return parts[i].increment(version.values[i])
        }

        return version.values[i]
    }

}