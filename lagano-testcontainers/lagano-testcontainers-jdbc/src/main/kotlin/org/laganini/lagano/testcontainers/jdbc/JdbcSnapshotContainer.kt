package org.laganini.lagano.testcontainers.jdbc

import org.laganini.lagano.migration.SqlSnapshotContainer
import javax.sql.DataSource

interface JdbcSnapshotContainer : SqlSnapshotContainer {

    fun dataSource(): DataSource

}