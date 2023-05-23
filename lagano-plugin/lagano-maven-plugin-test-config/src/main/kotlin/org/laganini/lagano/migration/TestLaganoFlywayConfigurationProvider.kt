package org.laganini.lagano.migration

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration

class TestLaganoFlywayConfigurationProvider : LaganoFlywayConfigProvider {
    override fun provide(): FluentConfiguration {
        return Flyway.configure()
    }
}