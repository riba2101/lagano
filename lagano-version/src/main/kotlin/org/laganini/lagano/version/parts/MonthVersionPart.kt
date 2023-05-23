package org.laganini.lagano.version.parts

import java.time.DateTimeException
import java.time.Month

class MonthVersionPart : AbstractVersionPart<Month>() {

    override fun parse(value: String): Month {
        return try {
            val month = value.toIntOrNull() ?: throw IllegalArgumentException("Provided value: $value is invalid")
            Month.of(month)
        } catch (e: DateTimeException) {
            throw IllegalArgumentException("Provided value: $value is invalid", e)
        }
    }

    override fun build(value: Month): String {
        return value.value.toString()
    }

    override fun increment(value: Month): Month {
        return value.plus(1L)
    }

    override fun decrement(value: Month): Month {
        return value.minus(1L)
    }

}