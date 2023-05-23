package org.laganini.lagano.snpashot.cleanup

import org.laganini.lagano.snpashot.storage.StorageStrategy
import java.nio.file.Path
import kotlin.io.path.exists

class KeepLatestNCleanStrategy<T : Comparable<T>>(
    private val storageStrategy: StorageStrategy<T>,
    private val n: Int
) : CleanStrategy {

    override fun clean(snapshotRootPath: Path) {
        storageStrategy
            .all(snapshotRootPath)
            .filter { it.exists() }
            .sortedBy { path -> storageStrategy.namingStrategy().resolve(path.fileName.toString()) }
            .dropLast(n)
            .forEach { it.toFile().delete() }
    }

}