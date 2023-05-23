package org.laganini.lagano.version

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class VersionTest {

    @Test
    fun shouldPassCornerCases() {
        val cases = listOf(
            Version(listOf(2, 0, 0)) > Version(listOf(0, 0, 1)),
            Version(listOf(2, 0, 0)) < Version(listOf(2, 0)),
            Version(listOf(1, 0, 0)) > Version(listOf(0, 0, 1)),
            Version(listOf(0, 1, 0)) > Version(listOf(0, 0, 1))
        )

        Assertions.assertThat(cases).doesNotContain(false)
    }

    @Nested
    inner class SameLength {

        @Test
        fun shouldBeLt() {
            val v1 = Version(listOf(1))
            val v2 = Version(listOf(2))

            Assertions.assertThat(v1).isLessThan(v2)
        }

        @Test
        fun shouldBeSame() {
            val v1 = Version(listOf(1))
            val v2 = Version(listOf(1))

            Assertions.assertThat(v1).isEqualTo(v2)
        }

        @Test
        fun shouldBeGt() {
            val v1 = Version(listOf(2))
            val v2 = Version(listOf(1))

            Assertions.assertThat(v1).isGreaterThan(v2)
        }

    }

    @Nested
    inner class DifferentLength {

        @Test
        fun shouldBeLt() {
            val v1 = Version(listOf(1, 0))
            val v2 = Version(listOf(2))

            Assertions.assertThat(v1).isLessThan(v2)
        }

        @Test
        fun shouldBeGt() {
            val v1 = Version(listOf(2))
            val v2 = Version(listOf(1, 0))

            Assertions.assertThat(v1).isGreaterThan(v2)
        }

    }

}