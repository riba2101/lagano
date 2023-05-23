package org.laganini.lagano.snpashot

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junitpioneer.jupiter.SetEnvironmentVariable
import org.laganini.lagano.snpashot.name.SingleNamingStrategy
import org.laganini.lagano.snpashot.storage.FileStorageStrategy
import kotlin.io.path.Path
import kotlin.io.path.copyTo
import kotlin.io.path.createDirectory

internal class SnapshotSupportTest {

    @BeforeEach
    internal fun setUp() {
        Path(PATH).toFile().deleteRecursively()
    }

    @AfterEach
    internal fun tearDown() {
        Path(PATH).toFile().deleteRecursively()
    }

    @Nested
    internal inner class WithoutSupport {

        @Test
        fun shouldNotRun() {
            val snapshotSupport = SnapshotSupport()
            snapshotSupport.setup()

            Assertions.assertThat(Path(PATH)).doesNotExist()
        }
    }

    @Nested
    @SetEnvironmentVariable(key = SnapshotConfiguration.PROPERTY_FEATURE_DB_SNAPSHOT, value = "true")
    internal inner class WithSupport {

        @Nested
        @SetEnvironmentVariable(key = SnapshotConfiguration.PROPERTY_FEATURE_DB_SNAPSHOT_PATH, value = PATH)
        internal inner class WithPath {

            @Test
            fun shouldSetup() {
                val snapshotSupport = SnapshotSupport()
                snapshotSupport.setup()

                Assertions.assertThat(Path(PATH)).exists()
            }

            @Nested
            internal inner class Initial {

                @Test
                fun shouldSetup() {
                    val snapshotSupport = SnapshotSupport(
                        SnapshotConfiguration(storageStrategy = FileStorageStrategy(NAME_STRATEGY))
                    )
                    snapshotSupport.setup()

                    Assertions.assertThat(snapshotSupport.buildFsPath())
                        .isEqualTo(Path("$PATH/$TEST_SCRIPT").toAbsolutePath())
                    Assertions.assertThat(snapshotSupport.buildFsPath()).exists()
                    Assertions.assertThat(snapshotSupport.buildFsPath()).isEmptyFile()
                }

            }

            @Nested
            internal inner class Existing {

                @BeforeEach
                internal fun setUp() {
                    Path(PATH).createDirectory()
                    val target = Path("$PATH/$TEST_SCRIPT")
                    Path("$BASE/$TEST_SCRIPT").copyTo(target)
                }

                @Test
                fun shouldSetup() {
                    val snapshotSupport = SnapshotSupport(
                        SnapshotConfiguration(storageStrategy = FileStorageStrategy(NAME_STRATEGY))
                    )
                    snapshotSupport.setup()

                    Assertions.assertThat(snapshotSupport.buildFsPath())
                        .isEqualTo(Path("$PATH/$TEST_SCRIPT").toAbsolutePath())
                    Assertions.assertThat(snapshotSupport.buildFsPath()).exists()
                    Assertions.assertThat(snapshotSupport.buildFsPath()).isNotEmptyFile()
                }

            }

        }

    }

    companion object {
        private const val BASE = "src/test/resources"
        private const val PATH = "$BASE/lagano"
        private const val TEST_SCRIPT = "initial.sql"
        private val NAME_STRATEGY = SingleNamingStrategy(TEST_SCRIPT)
    }

}