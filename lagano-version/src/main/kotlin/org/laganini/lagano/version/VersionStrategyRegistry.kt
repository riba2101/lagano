package org.laganini.lagano.version

class VersionStrategyRegistry() {

    private var registry: MutableList<AbstractVersionStrategy<*>> = mutableListOf()

    constructor(strategies: List<AbstractVersionStrategy<*>>) : this() {
        this.registry.addAll(strategies)
    }

    fun register(version: AbstractVersionStrategy<*>) {
        registry.add(version)
    }

    fun getAll(): List<AbstractVersionStrategy<*>> {
        return registry.toList()
    }

    fun findStrategy(value: String): AbstractVersionStrategy<*>? = registry
        .stream()
        .filter { strategy -> strategy.supports(value) }
        .findFirst()
        .orElse(null)

}