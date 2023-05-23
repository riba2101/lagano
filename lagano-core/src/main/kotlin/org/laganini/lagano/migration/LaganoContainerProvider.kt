package org.laganini.lagano.migration

interface LaganoContainerProvider {

    fun provide() : SqlSnapshotContainer

}