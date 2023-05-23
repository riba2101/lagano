package org.laganini.lagano.version.parts

import java.time.DateTimeException
import java.time.DayOfWeek

class DayVersionPart : AbstractVersionPart<DayOfWeek>() {

    override fun parse(value: String): DayOfWeek {
        return try {
            val day = value.toIntOrNull() ?: throw IllegalArgumentException("Provided value: $value is invalid")
            DayOfWeek.of(day)
        } catch (e: DateTimeException) {
            throw IllegalArgumentException("Provided value: $value is invalid", e)
        }
    }

    override fun build(value: DayOfWeek): String {
        return value.value.toString()
    }

    override fun increment(value: DayOfWeek): DayOfWeek {
        return value.plus(1L)
    }

    override fun decrement(value: DayOfWeek): DayOfWeek {
        return value.minus(1L)
    }

}