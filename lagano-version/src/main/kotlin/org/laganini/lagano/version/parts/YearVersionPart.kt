package org.laganini.lagano.version.parts

import java.time.DateTimeException
import java.time.Year

class YearVersionPart : AbstractVersionPart<Year>() {

    override fun parse(value: String): Year {
        return try {
            val year = value.toIntOrNull() ?: throw IllegalArgumentException("Provided value: $value is invalid")
            Year.of(year)
        } catch (e: DateTimeException) {
            throw IllegalArgumentException("Provided value: $value is invalid", e)
        }
    }

    override fun build(value: Year): String {
        return value.value.toString()
    }

    override fun increment(value: Year): Year {
        return value.plusYears(1L)
    }

    override fun decrement(value: Year): Year {
        return value.minusYears(1L)
    }

}