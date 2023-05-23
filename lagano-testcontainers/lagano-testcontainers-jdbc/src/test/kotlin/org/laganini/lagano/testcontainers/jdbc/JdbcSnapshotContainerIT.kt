package org.laganini.lagano.testcontainers.jdbc

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junitpioneer.jupiter.SetEnvironmentVariable
import org.laganini.lagano.snpashot.SnapshotConfiguration
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import java.sql.ResultSet
import kotlin.io.path.Path
import kotlin.io.path.copyTo
import kotlin.io.path.createDirectory


internal class JdbcSnapshotContainerIT {

    private val container: MariaDBContainer<*> = MariaDBContainer(DockerImageName.parse(MariaDBContainer.NAME))
        .waitingFor(Wait.forListeningPort())

    @BeforeEach
    internal fun setUp() {
        Path(PATH).toFile().deleteRecursively()
    }

    @AfterEach
    internal fun tearDown() {
        Path(PATH).toFile().deleteRecursively()
    }

    @Nested
    @SetEnvironmentVariable(key = SnapshotConfiguration.PROPERTY_FEATURE_DB_SNAPSHOT, value = "true")
    @SetEnvironmentVariable(key = SnapshotConfiguration.PROPERTY_FEATURE_DB_SNAPSHOT_PATH, value = PATH)
    internal inner class WithSupport {


        @Nested
        internal inner class DefaultStorage {

            @Nested
            internal inner class Empty {

                @Nested
                internal inner class Suite {

                    private lateinit var snapshotContainer: JdbcSnapshotContainer

                    @BeforeEach
                    internal fun setUp() {
                        snapshotContainer = JdbcSnapshotContainer(container)
                        snapshotContainer.start()
                    }

                    @AfterEach
                    internal fun tearDown() {
                        snapshotContainer.stop()
                    }

                    @Test
                    fun shouldRun() {
                        snapshotContainer.restore()

                        val databases = getDatabases(container)
                        val tables = getTables(container)

                        Assertions.assertThat(databases).contains(TEST_DB)
                        Assertions.assertThat(tables).doesNotContain(DUMMY_TABLE)
                    }

                }

            }

            @Nested
            internal inner class Existing {

                @BeforeEach
                internal fun setUp() {
                    Path(PATH).createDirectory()
                    Path("$BASE/$TEST_SCRIPT").copyTo(Path("$PATH/$TEST_SCRIPT"))
                }

                @Nested
                internal inner class Suite {

                    private lateinit var snapshotContainer: JdbcSnapshotContainer

                    @BeforeEach
                    internal fun setUp() {
                        snapshotContainer = JdbcSnapshotContainer(container)
                        snapshotContainer.start()
                    }

                    @AfterEach
                    internal fun tearDown() {
                        snapshotContainer.stop()
                    }

                    @Test
                    fun shouldRun() {
                        snapshotContainer.restore()

                        val databases = getDatabases(container)
                        val tables = getTables(container)

                        Assertions.assertThat(databases).contains(TEST_DB)
                        Assertions.assertThat(tables).contains(DUMMY_TABLE)
                    }

                }

            }

        }

        private fun getDatabases(container: MariaDBContainer<*>): List<String> {
            val resultSet = performQuery(container, "SHOW DATABASES")

            return resultSet
                .use { rs ->
                    generateSequence {
                        if (rs?.next() == true) rs.getString(1) else null
                    }.toList()
                }
        }

        private fun getTables(container: MariaDBContainer<*>): List<String> {
            performQuery(container, "USE $TEST_DB")
            val resultSet = performQuery(container, "SHOW FULL TABLES")

            return resultSet
                .use { rs ->
                    generateSequence {
                        if (rs?.next() == true) rs.getString(1) else null
                    }.toList()
                }
        }

        private fun performQuery(container: JdbcDatabaseContainer<*>, sql: String): ResultSet? {
            return container
                .createConnection("")
                .use {
                    it.createStatement()
                        .use { it.executeQuery(sql) }
                }
        }

    }

    companion object {
        const val BASE = "src/test/resources"
        const val PATH = "$BASE/lagano"
        const val TEST_SCRIPT = "database-snapshot.sql"
        const val TEST_DB = "test"
        const val DUMMY_TABLE = "dummy"
    }

}
