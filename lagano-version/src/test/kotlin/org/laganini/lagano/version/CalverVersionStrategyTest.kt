package org.laganini.lagano.version

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CalverVersionStrategyTest {

    val strategy = CalverVersionStrategy()

    companion object {
        val SUPPORTED = listOf(
            "1990.12",
            "2000.1",
        )

        val NOT_SUPPORTED = listOf(
            "test",
            "1.2.1",
            "1",
            "2000.0",
            "2000.13"
        )
    }

    @Nested
    inner class Supported {

        @Test
        fun shouldSupport() {
            val actual = SUPPORTED.map { input -> strategy.supports(input) }.toList()

            Assertions.assertThat(actual).containsExactly(true, true)
        }

//        @Test
//        fun shouldParse() {
//            val actual = SUPPORTED.map { input -> strategy.parse(input) }.toList()
//
//            Assertions
//                .assertThat(actual)
//                .containsExactly(
//                    listOf(0, 0, 1),
//                    listOf(0, 1, 0),
//                    listOf(1, 0, 0),
//                    listOf(2, 0, 0),
//                )
//        }

        @Test
        fun shouldSort() {
            val actual = SUPPORTED
                .reversed()
                .map { input -> strategy.resolve(input) }
                .sorted()
                .map { version -> strategy.build(version) }
                .toList()

            Assertions
                .assertThat(actual)
                .containsExactlyElementsOf(SUPPORTED)
        }

    }


    @Nested
    inner class NotSupported {

        @Test
        fun shouldNotSupport() {
            val actual = NOT_SUPPORTED.map { input -> strategy.supports(input) }.toList()

            Assertions.assertThat(actual).containsOnly(false, false, false, false, false)
        }

    }

}