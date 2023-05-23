package org.laganini.lagano.version

import org.laganini.lagano.version.parts.VersionPart
import java.time.temporal.Temporal

abstract class AbstractTemporalVersionStrategy :
    AbstractVersionStrategy<AbstractTemporalVersionStrategy.ComparableAndTemporal> {

    constructor(parts: List<VersionPart<ComparableAndTemporal>>) : this(parts, SEPARATOR)

    constructor(parts: List<VersionPart<ComparableAndTemporal>>, separator: String) : super(parts, separator)

    interface ComparableAndTemporal : Temporal, Comparable<Temporal>

}