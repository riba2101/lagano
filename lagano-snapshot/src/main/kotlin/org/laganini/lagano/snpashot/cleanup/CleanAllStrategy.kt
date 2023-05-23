package org.laganini.lagano.snpashot.cleanup

import org.laganini.lagano.snpashot.storage.StorageStrategy
import java.nio.file.Path
import kotlin.io.path.exists

class CleanAllStrategy<T : Comparable<T>>(
    private val storageStrategy: StorageStrategy<T>
) : CleanStrategy {

    override fun clean(snapshotRootPath: Path) {
        storageStrategy
            .all(snapshotRootPath)
            .filter { it.exists() }
            .forEach { it.toFile().delete() }
    }

}