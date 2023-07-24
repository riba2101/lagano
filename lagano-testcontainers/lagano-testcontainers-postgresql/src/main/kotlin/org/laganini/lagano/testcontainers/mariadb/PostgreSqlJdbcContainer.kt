package org.laganini.lagano.testcontainers.mariadb

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.laganini.lagano.snpashot.SnapshotConfiguration
import org.laganini.lagano.snpashot.SnapshotSupport
import org.laganini.lagano.snpashot.cleanup.FileUtil
import org.laganini.lagano.testcontainers.jdbc.JdbcSnapshotContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.WaitStrategy
import org.testcontainers.utility.DockerImageName
import javax.sql.DataSource

class PostgreSqlJdbcContainer(
    dockerImageName: DockerImageName = DockerImageName.parse(NAME).withTag(DEFAULT_IMAGE_VERSION),
    private val snapshotConfiguration: SnapshotConfiguration = SnapshotConfiguration()
) : JdbcSnapshotContainer, PostgreSQLContainer<PostgreSqlJdbcContainer>(dockerImageName) {

    private val snapshotSupport: SnapshotSupport = SnapshotSupport(snapshotConfiguration)

    init {
        val fsPath = snapshotSupport.buildFsPath()
        if (fsPath != null) {
            withFileSystemBind(
                fsPath.toAbsolutePath().toString(),
                snapshotSupport.buildContainerPath()
            )
        }
    }

    override fun dataSource(): DataSource {
        return HikariDataSource(HikariConfig().apply {
            jdbcUrl = super.getJdbcUrl()
            username = super.getUsername()
            password = super.getPassword()
            driverClassName = super.getDriverClassName()
        })
    }

    fun whitFixatedPort(from: Int, to: Int): PostgreSqlJdbcContainer {
        addFixedExposedPort(from, to)
        return this
    }

    fun waitingFor(waitStrategy: WaitStrategy?): PostgreSqlJdbcContainer {
        setWaitStrategy(waitStrategy)
        return this
    }

    override fun snapshot() {
        if (!snapshotConfiguration.enabled) {
            return
        }

        val fsPath = snapshotSupport.buildFsPath() ?: return

        FileUtil.truncate(fsPath)

        execInContainer(
            "sh",
            "-c",
            "pg_dump --user " + username + " -p" + password + " " + databaseName + " > " + snapshotConfiguration.containerFilename
        )
    }

    override fun restore() {
        if (!snapshotConfiguration.enabled) {
            return
        }

        execInContainer(
            "sh",
            "-c",
            "psql --user " + username + " -p" + password + " " + databaseName + " < " + snapshotConfiguration.containerFilename
        )
    }

    companion object {
        const val DEFAULT_IMAGE_VERSION = "10.9.6"
    }

}
