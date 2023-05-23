package org.laganini.lagano.version

import org.laganini.lagano.version.parts.MonthVersionPart
import org.laganini.lagano.version.parts.VersionPart
import org.laganini.lagano.version.parts.YearVersionPart
import java.time.LocalDate
import java.time.Month
import java.time.Year

class CalverVersionStrategy : AbstractTemporalVersionStrategy {

    constructor() : this(SEPARATOR)

    constructor(separator: String) : super(
        listOf(
            YearVersionPart() as VersionPart<ComparableAndTemporal>,
            MonthVersionPart() as VersionPart<ComparableAndTemporal>,
        ),
        separator
    )

    override fun build(version: Version<ComparableAndTemporal>): String {
        return IntRange(0, parts.size - 1)
            .toList()
            .map { i -> parts[i].build(version.values[i]) }
            .joinToString(separator)
    }

    override fun next(current: Version<ComparableAndTemporal>): Version<ComparableAndTemporal> {
        return now()
    }

    override fun initial(): Version<ComparableAndTemporal> {
        return now()
    }

    fun now(): Version<ComparableAndTemporal> {
        return Version(
            listOf(
                Year.now() as ComparableAndTemporal,
                Month.from(LocalDate.now()) as ComparableAndTemporal
            ),
            separator
        )
    }

}