package org.laganini.lagano.testcontainers.jdbc

import org.laganini.lagano.testcontainers.mariadb.PostgreSqlJdbcContainer

internal class PostgreSqlJdbcContainerIT : JdbcSnapshotContainerSuite<PostgreSqlJdbcContainer>({ PostgreSqlJdbcContainer() })