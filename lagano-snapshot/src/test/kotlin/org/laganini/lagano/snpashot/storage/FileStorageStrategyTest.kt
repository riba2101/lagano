package org.laganini.lagano.snpashot.storage

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.laganini.lagano.snpashot.name.VersionedNamingStrategy
import org.laganini.lagano.version.IncrementalVersionStrategy
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile

internal class FileStorageStrategyTest {

    private val versionStrategy = IncrementalVersionStrategy()
    private val namingStrategy = VersionedNamingStrategy(FILENAME, versionStrategy)

    @BeforeEach
    internal fun setUp() {
        Path(PATH).toFile().deleteRecursively()
    }

    @AfterEach
    internal fun tearDown() {
        Path(PATH).toFile().deleteRecursively()
    }

    @Nested
    internal inner class Empty {

        @BeforeEach
        internal fun setUp() {
            Path(PATH).createDirectory()
        }

        @Test
        internal fun shouldInitial() {
            val strategy = FileStorageStrategy(namingStrategy)

            val initial = strategy.initial(Path(PATH))

            Assertions.assertThat(initial).isEqualTo(Path(PATH, "1_$FILENAME").toAbsolutePath())
        }

    }

    @Nested
    internal inner class Existing {

        @BeforeEach
        internal fun setUp() {
            Path(PATH).createDirectory()
            Path(PATH, "1_$FILENAME").createFile()
        }

        @Test
        internal fun shouldLatest() {
            val snapshotRootPath = Path(PATH)
            val strategy = FileStorageStrategy(namingStrategy)

            val initial = strategy.initial(snapshotRootPath)!!
            Assertions.assertThat(initial).exists()

            val latest = strategy.latest(snapshotRootPath)
            Assertions.assertThat(latest).isEqualTo(Path(PATH, "2_$FILENAME").toAbsolutePath())
        }

    }

    companion object {
        private const val BASE = "src/test/resources"
        private const val PATH = "$BASE/file-storage"
        private const val FILENAME = "db.sql"
    }

}