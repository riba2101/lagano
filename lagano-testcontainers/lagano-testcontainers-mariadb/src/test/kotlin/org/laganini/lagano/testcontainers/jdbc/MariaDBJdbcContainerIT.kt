package org.laganini.lagano.testcontainers.jdbc

import org.laganini.lagano.testcontainers.mariadb.MariaDBJdbcContainer

internal class MariaDBJdbcContainerIT : JdbcSnapshotContainerSuite<MariaDBJdbcContainer>({ MariaDBJdbcContainer() })