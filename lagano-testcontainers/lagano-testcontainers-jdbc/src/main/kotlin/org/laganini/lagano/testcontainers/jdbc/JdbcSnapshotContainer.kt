package org.laganini.lagano.testcontainers.jdbc

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.laganini.lagano.migration.SqlSnapshotContainer
import org.laganini.lagano.snpashot.SnapshotConfiguration
import org.laganini.lagano.snpashot.SnapshotSupport
import org.laganini.lagano.snpashot.cleanup.FileUtil
import org.testcontainers.containers.JdbcDatabaseContainer
import javax.sql.DataSource

class JdbcSnapshotContainer(
    private val delegate: JdbcDatabaseContainer<*>,
    private val snapshotConfiguration: SnapshotConfiguration = SnapshotConfiguration()
) : SqlSnapshotContainer {

    private val snapshotSupport: SnapshotSupport = SnapshotSupport(snapshotConfiguration)

    init {
        val fsPath = snapshotSupport.buildFsPath()
        if (fsPath != null) {
            delegate.withFileSystemBind(
                fsPath.toAbsolutePath().toString(),
                snapshotSupport.buildContainerPath()
            )
        }
    }

    override fun dataSource(): DataSource {
        return HikariDataSource(HikariConfig().apply {
            jdbcUrl = delegate.jdbcUrl
            username = delegate.username
            password = delegate.password
            driverClassName = delegate.driverClassName
        })
    }

    override fun delegate(): JdbcDatabaseContainer<*> {
        return delegate
    }

    override fun start() {
        delegate.start()
    }

    override fun stop() {
        delegate.stop()
    }

    override fun snapshot() {
        if (!snapshotConfiguration.enabled) {
            return
        }

        val fsPath = snapshotSupport.buildFsPath() ?: return

        FileUtil.truncate(fsPath)

        delegate.execInContainer(
            "sh",
            "-c",
            "mysqldump --user " + delegate.username + " -p" + delegate.password + " " + delegate.databaseName + " > " + snapshotConfiguration.containerFilename
        )
    }

    override fun restore() {
        if (!snapshotConfiguration.enabled) {
            return
        }

        delegate.execInContainer(
            "sh",
            "-c",
            "mysql --user " + delegate.username + " -p" + delegate.password + " " + delegate.databaseName + " < " + snapshotConfiguration.containerFilename
        )
    }

}