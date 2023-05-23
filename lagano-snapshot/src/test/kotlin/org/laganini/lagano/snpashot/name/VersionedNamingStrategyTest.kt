package org.laganini.lagano.snpashot.name

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.laganini.lagano.version.IncrementalVersionStrategy
import org.laganini.lagano.version.Version

internal class VersionedNamingStrategyTest {

    private val versionStrategy = IncrementalVersionStrategy()

    @Nested
    internal inner class Initial {

        @Test
        internal fun shouldInitial() {
            val strategy = VersionedNamingStrategy(FILENAME, versionStrategy)

            val initial = strategy.initial()

            Assertions.assertThat(initial).isEqualTo(Version(listOf(1)))
        }

    }

    @Nested
    internal inner class Resolve {

        @Test
        internal fun shouldResolve() {
            val strategy = VersionedNamingStrategy(FILENAME, versionStrategy)

            val filename = "2${VersionedNamingStrategy.SEPARATOR}$FILENAME"
            val resolved = strategy.resolve(filename)

            Assertions.assertThat(resolved).isEqualTo(Version(listOf(2)))
        }

        @Test
        internal fun shouldResolveWithCustomSeparator() {
            val separator = "+"
            val strategy = VersionedNamingStrategy(FILENAME, versionStrategy, separator)

            val filename = "2${separator}$FILENAME"
            val resolved = strategy.resolve(filename)

            Assertions.assertThat(resolved).isEqualTo(Version(listOf(2)))
        }

    }

    @Nested
    internal inner class Next {

        @Test
        internal fun shouldNext() {
            val strategy = VersionedNamingStrategy(FILENAME, versionStrategy)

            val version = Version(listOf(2))
            val next = strategy.next(version)

            Assertions.assertThat(next).isEqualTo(Version(listOf(3)))
        }

    }

    @Nested
    internal inner class Build {

        @Test
        internal fun shouldBuild() {
            val strategy = VersionedNamingStrategy(FILENAME, versionStrategy)

            val version = Version(listOf(2))
            val build = strategy.build(version)

            Assertions.assertThat(build).isEqualTo("2${VersionedNamingStrategy.SEPARATOR}db.sql")
        }

        @Test
        internal fun shouldBuildWithCustomSeparator() {
            val separator = "+"
            val strategy = VersionedNamingStrategy(FILENAME, versionStrategy, separator)

            val version = Version(listOf(2))
            val build = strategy.build(version)

            Assertions.assertThat(build).isEqualTo("2${separator}db.sql")
        }

    }

    companion object {
        private const val FILENAME = "db.sql"
    }

}