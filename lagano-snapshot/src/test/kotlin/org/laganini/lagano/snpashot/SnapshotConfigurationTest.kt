package org.laganini.lagano.snpashot

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junitpioneer.jupiter.SetEnvironmentVariable
import kotlin.io.path.Path

internal class SnapshotConfigurationTest {

    @Nested
    internal inner class WithoutSupport {

        @Test
        fun shouldNotRun() {
            val configuration = SnapshotConfiguration()
            val shouldRun = configuration.enabled
            Assertions.assertThat(shouldRun).isFalse
        }
    }

    @Nested
    @SetEnvironmentVariable(key = SnapshotConfiguration.PROPERTY_FEATURE_DB_SNAPSHOT, value = "true")
    internal inner class WithSupport {

        @Nested
        internal inner class Rebuild {

            @Nested
            internal inner class WithoutRebuild {

                @Test
                fun shouldRun() {
                    val configuration = SnapshotConfiguration()
                    val shouldRun = configuration.rebuild
                    Assertions.assertThat(shouldRun).isFalse()
                }

            }

            @Nested
            @SetEnvironmentVariable(key = SnapshotConfiguration.PROPERTY_FEATURE_DB_SNAPSHOT_REBUILD, value = "true")
            internal inner class WithRebuild {

                @Test
                fun shouldRun() {
                    val configuration = SnapshotConfiguration()
                    val shouldRun = configuration.rebuild
                    Assertions.assertThat(shouldRun).isTrue
                }

            }

        }

        @Nested
        internal inner class Path {

            @Nested
            internal inner class WithoutPath {

                @Test
                fun shouldSetup() {
                    val configuration = SnapshotConfiguration()
                    val path = configuration.path

                    Assertions.assertThat(path).isEqualTo(Path(SnapshotConfiguration.DEFAULT_DATABASE_SNAPSHOT_FS_PATH))
                }
            }

            @Nested
            @SetEnvironmentVariable(
                key = SnapshotConfiguration.PROPERTY_FEATURE_DB_SNAPSHOT_PATH,
                value = "/resource/lagano"
            )
            internal inner class WithPath {

                @Test
                fun shouldSetup() {
                    val configuration = SnapshotConfiguration()
                    val path = configuration.path

                    Assertions.assertThat(path).isEqualTo(Path("/resource/lagano"))
                }
            }

        }

    }

}