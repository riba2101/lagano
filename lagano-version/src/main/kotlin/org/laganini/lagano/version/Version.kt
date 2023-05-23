package org.laganini.lagano.version

data class Version<T>(val values: List<T>, val separator: String = AbstractVersionStrategy.SEPARATOR) :
    Comparable<Version<T>> where T : Comparable<T> {

    override fun compareTo(other: Version<T>): Int {
        //fallback to string if different size
        if (values.size != other.values.size) {
            return toString().compareTo(other.toString())
        }

        val range = IntRange(0, values.size - 1)
            .toList()
        val weights: List<Int> = range
            .map { i -> calculateWeights(i, range.size, values[i], other.values[i]) }

        return weights.sum()
    }

    private fun calculateWeights(i: Int, size: Int, value: T, other: T): Int {
        val compared = value.compareTo(other)
        if (compared == 0) {
            return 0
        }

        return compared * ((size - i) * 10)
    }

}