package org.laganini.lagano.migration

import org.flywaydb.core.api.configuration.FluentConfiguration

interface LaganoFlywayConfigProvider {

    fun provide(): FluentConfiguration

}