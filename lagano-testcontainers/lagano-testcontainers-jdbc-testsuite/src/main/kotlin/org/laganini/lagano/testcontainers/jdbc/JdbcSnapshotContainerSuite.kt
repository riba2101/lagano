package org.laganini.lagano.testcontainers.jdbc

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junitpioneer.jupiter.SetEnvironmentVariable
import org.laganini.lagano.snpashot.SnapshotConfiguration
import org.testcontainers.containers.JdbcDatabaseContainer
import java.sql.ResultSet
import java.util.*
import java.util.function.Supplier
import kotlin.io.path.Path
import kotlin.io.path.copyTo
import kotlin.io.path.createDirectory


abstract class JdbcSnapshotContainerSuite<CONTAINER>(private val snapshotContainerSupplier: Supplier<CONTAINER>)
        where CONTAINER : JdbcSnapshotContainer,
              CONTAINER : JdbcDatabaseContainer<CONTAINER> {

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

                    private lateinit var snapshotContainer: CONTAINER

                    @BeforeEach
                    internal fun setUp() {
                        snapshotContainer = snapshotContainerSupplier.get()
                        snapshotContainer.start()
                    }

                    @AfterEach
                    internal fun tearDown() {
                        snapshotContainer.stop()
                    }

                    @Test
                    fun shouldRun() {
                        snapshotContainer.restore()

                        val databases = getDatabases(snapshotContainer)
                        val tables = getTables(snapshotContainer)

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

                    private lateinit var snapshotContainer: CONTAINER

                    @BeforeEach
                    internal fun setUp() {
                        snapshotContainer = snapshotContainerSupplier.get()
                        snapshotContainer.start()
                    }

                    @AfterEach
                    internal fun tearDown() {
                        snapshotContainer.stop()
                    }

                    @Test
                    fun shouldRun() {
                        snapshotContainer.restore()

                        val databases = getDatabases(snapshotContainer)
                        val tables = getTables(snapshotContainer)

                        Assertions.assertThat(databases).contains(TEST_DB)
                        Assertions.assertThat(tables).contains(DUMMY_TABLE)
                    }

                }

            }

        }

    }

    private fun getDatabases(container: CONTAINER): List<String> {
        val resultSet = performQuery(container, getDatabasesQuery())

        return resultSet
            .use { rs ->
                generateSequence {
                    if (rs?.next() == true) rs.getString(1) else null
                }.toList()
            }
    }

    protected open fun getDatabasesQuery(): String {
        return "SHOW DATABASES"
    }

    private fun getTables(container: CONTAINER): List<String> {
        val resultSet = performQuery(container, getTablesQuery(), useCatalog())

        return resultSet
            .use { rs ->
                generateSequence {
                    if (rs?.next() == true) rs.getString(1) else null
                }.toList()
            }
    }

    protected open fun getTablesQuery(): String {
        return "SHOW FULL TABLES"
    }

    protected open fun useCatalog(): String? {
        return null
    }

    private fun performQuery(container: JdbcDatabaseContainer<*>, sql: String, database: String? = null): ResultSet? {
        val connector = container.createConnection("", buildProps(database))
        val statement = connector.createStatement()

        return statement.executeQuery(sql)
    }

    protected open fun buildProps(database: String?): Properties {
        if (database == null) {
            return Properties()
        }

        val props = Properties()
        props.put("database", database)
        return props
    }

    companion object {
        const val BASE = "src/test/resources"
        const val PATH = "$BASE/lagano"
        const val TEST_SCRIPT = "database-snapshot.sql"
        const val TEST_DB = "test"
        const val DUMMY_TABLE = "dummy"
    }

}
