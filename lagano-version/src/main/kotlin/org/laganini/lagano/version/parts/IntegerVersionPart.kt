package org.laganini.lagano.version.parts

class IntegerVersionPart : AbstractVersionPart<Int>() {

    override fun parse(value: String): Int {
        return value.toInt()
    }

    override fun increment(value: Int): Int {
        return value + 1
    }

    override fun decrement(value: Int): Int {
        return value - 1
    }

}