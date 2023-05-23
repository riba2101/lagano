package org.laganini.lagano.snpashot.name

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.laganini.lagano.version.Version

internal class SingleNamingStrategyTest {

    @Nested
    internal inner class Initial {

        @Test
        internal fun shouldInitial() {
            val strategy = SingleNamingStrategy(FILENAME)

            val initial = strategy.initial()

            Assertions.assertThat(initial).isEqualTo(Version<Comparable<Any>>(listOf()))
        }

    }

    @Nested
    internal inner class Resolve {

        @Test
        internal fun shouldResolve() {
            val strategy = SingleNamingStrategy(FILENAME)

            val initial = strategy.resolve(FILENAME)

            Assertions.assertThat(initial).isEqualTo(Version<Comparable<Any>>(listOf()))
        }

    }

    @Nested
    internal inner class Next {

        @Test
        internal fun shouldNext() {
            val strategy = SingleNamingStrategy(FILENAME)

            val initial = strategy.next(Version(listOf()))

            Assertions.assertThat(initial).isEqualTo(Version<Comparable<Any>>(listOf()))
        }

    }

    @Nested
    internal inner class Build {

        @Test
        internal fun shouldBuild() {
            val strategy = SingleNamingStrategy(FILENAME)

            val initial = strategy.build(Version(listOf()))

            Assertions.assertThat(initial).isEqualTo(FILENAME)
        }

    }

    companion object {
        private const val FILENAME = "db.sql"
    }

}