package org.laganini.lagano.testcontainers.jdbc

import org.laganini.lagano.testcontainers.mariadb.PostgreSqlJdbcContainer

internal class PostgreSqlJdbcContainerIT :
    JdbcSnapshotContainerSuite<PostgreSqlJdbcContainer>({ PostgreSqlJdbcContainer() }) {

    override fun getDatabasesQuery(): String {
        return "SELECT datname FROM pg_database"
    }

    override fun getTablesQuery(): String {
        return "SELECT tablename FROM pg_tables WHERE schemaname != 'pg_catalog' AND schemaname != 'information_schema';"
    }
}
