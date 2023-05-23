package org.laganini.lagano.snpashot.cleanup

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.laganini.lagano.snpashot.name.VersionedNamingStrategy
import org.laganini.lagano.snpashot.storage.FileStorageStrategy
import org.laganini.lagano.version.IncrementalVersionStrategy
import org.laganini.lagano.version.Version
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile

internal class CleanAllStrategyTest {

    @BeforeEach
    internal fun setUp() {
        Path(PATH).toFile().deleteRecursively()
    }

    @AfterEach
    internal fun tearDown() {
        Path(PATH).toFile().deleteRecursively()
    }

    private val versionStrategy = IncrementalVersionStrategy()
    private val namingStrategy = VersionedNamingStrategy(FILENAME, versionStrategy)
    private val storageStrategy = FileStorageStrategy(namingStrategy)

    @Nested
    internal inner class Empty {

        @BeforeEach
        internal fun setUp() {
            Path(PATH).createDirectory()
        }

        @Test
        internal fun shouldClean() {
            val strategy = CleanAllStrategy(storageStrategy)

            Assertions.assertThat(Files.list(Path(PATH)).count()).isEqualTo(0)

            val exception = Assertions.catchException { strategy.clean(Path(PATH)) }

            Assertions.assertThat(exception).isNull()
        }

    }

    @Nested
    internal inner class Existing {

        @BeforeEach
        internal fun setUp() {
            Path(PATH).createDirectory()
        }

        @Test
        internal fun shouldClean() {
            val strategy = CleanAllStrategy(storageStrategy)

            Assertions.assertThat(Files.list(Path(PATH)).count()).isEqualTo(0)

            val initial = namingStrategy.initial()!!
            create(initial)
            val first = versionStrategy.next(initial)
            create(first)

            Assertions.assertThat(Files.list(Path(PATH)).count()).isEqualTo(2)

            strategy.clean(Path(PATH))

            Assertions.assertThat(Files.list(Path(PATH)).count()).isEqualTo(0)
        }

    }

    private fun create(version: Version<Int>): Path {
        val path = storageStrategy.build(Path(PATH), namingStrategy.build(version))
        return path.createFile()
    }

    companion object {
        private const val BASE = "src/test/resources"
        private const val PATH = "$BASE/clean-all"
        private const val FILENAME = "db.sql"
    }

}