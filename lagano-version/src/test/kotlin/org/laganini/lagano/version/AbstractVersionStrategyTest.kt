package org.laganini.lagano.version

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.laganini.lagano.version.parts.IntegerVersionPart
import org.laganini.lagano.version.parts.VersionPart

internal class AbstractVersionStrategyTest {

    @Nested
    inner class DefaultSeparator {
        @Nested
        inner class Single : TestFactory<Int>(listOf(IntegerVersionPart())) {
            override fun givenSeparator(): String {
                return AbstractVersionStrategy.SEPARATOR
            }

            override fun givenInput(): String {
                return "1"
            }

            override fun givenExpected(): List<Int> {
                return listOf(1)
            }

        }

        @Nested
        inner class Multi : TestFactory<Int>(listOf(IntegerVersionPart(), IntegerVersionPart())) {
            override fun givenSeparator(): String {
                return AbstractVersionStrategy.SEPARATOR
            }

            override fun givenInput(): String {
                return "1${AbstractVersionStrategy.SEPARATOR}0"
            }

            override fun givenExpected(): List<Int> {
                return listOf(1, 0)
            }

        }

    }

    @Nested
    inner class CustomSeparator {

        val SEPARATOR: String = ","

        @Nested
        inner class Single : TestFactory<Int>(listOf(IntegerVersionPart()), SEPARATOR) {
            override fun givenSeparator(): String {
                return SEPARATOR
            }

            override fun givenInput(): String {
                return "1"
            }

            override fun givenExpected(): List<Int> {
                return listOf(1)
            }

        }

        @Nested
        inner class Multi : TestFactory<Int>(listOf(IntegerVersionPart(), IntegerVersionPart()), SEPARATOR) {
            override fun givenSeparator(): String {
                return SEPARATOR
            }

            override fun givenInput(): String {
                return "1${SEPARATOR}0"
            }

            override fun givenExpected(): List<Int> {
                return listOf(1, 0)
            }

        }

    }

    abstract class TestFactory<T : Comparable<T>>(
        val versionPart: List<VersionPart<T>>,
        val separator: String = AbstractVersionStrategy.SEPARATOR
    ) {

        protected abstract fun givenSeparator(): String

        protected abstract fun givenInput(): String

        protected abstract fun givenExpected(): List<T>

        protected fun givenVersionStrategy(): VersionStrategy<T> {
            return object : AbstractVersionStrategy<T>(versionPart, separator) {
                override fun next(current: Version<T>): Version<T> {
                    return current
                }

                override fun initial(): Version<T> {
                    return Version(listOf())
                }

            }
        }

        @Nested
        inner class Support {

            @Test
            internal fun shouldNotSupportEmpty() {
                val versionStrategy = givenVersionStrategy()

                val actual = versionStrategy.supports("")

                Assertions.assertThat(actual).isFalse()
            }

            @Test
            internal fun shouldSupport() {
                val versionStrategy = givenVersionStrategy()

                val actual = versionStrategy.supports(givenInput())

                Assertions.assertThat(actual).isTrue()
            }

        }

        @Nested
        inner class Parse {

            @Test
            internal fun shouldParse() {
                val versionStrategy = givenVersionStrategy()

                val actual = versionStrategy.parse(givenInput())

                Assertions.assertThat(actual).containsExactlyElementsOf(givenExpected())
            }

        }

        @Nested
        inner class Resolve {

            @Test
            internal fun shouldResolve() {
                val versionStrategy = givenVersionStrategy()

                val actual = versionStrategy.resolve(givenInput())

                Assertions.assertThat(actual)
                    .isEqualTo(Version(givenExpected(), separator = givenSeparator()))
            }

        }

        @Nested
        inner class Build {

            @Test
            internal fun shouldBuild() {
                val versionStrategy = givenVersionStrategy()

                val actual =
                    versionStrategy.build(Version(givenExpected(), separator = givenSeparator()))

                Assertions.assertThat(actual).isEqualTo(givenInput())
            }

        }

    }

}
