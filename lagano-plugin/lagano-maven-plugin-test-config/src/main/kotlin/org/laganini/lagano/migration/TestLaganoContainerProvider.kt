package org.laganini.lagano.migration

import org.laganini.lagano.testcontainers.jdbc.JdbcSnapshotContainer
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

class TestLaganoContainerProvider : LaganoContainerProvider {
    override fun provide(): SqlSnapshotContainer {
        return JdbcSnapshotContainer(
            MariaDBContainer(DockerImageName.parse(MariaDBContainer.NAME))
                .withConfigurationOverride("sql")
                .waitingFor(Wait.forListeningPort())
        )
    }
}