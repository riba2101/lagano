package org.laganini.lagano.snpashot.storage

import org.laganini.lagano.snpashot.name.NamingStrategy
import java.nio.file.Path

class FileStorageStrategy<T : Comparable<T>>(
    namingStrategy: NamingStrategy<T>
) : AbstractStorageStrategy<T>(namingStrategy) {

    override fun latest(snapshotRootPath: Path): Path? {
        val all = all(snapshotRootPath)
        if (all.isEmpty()) {
            return null
        }

        val last = all.last()

        val resolved = namingStrategy.resolve(last.fileName.toString()) ?: return null

        val next = namingStrategy.next(resolved) ?: return null
        val filename = namingStrategy.build(next)

        return build(snapshotRootPath, filename)
    }

    override fun initial(snapshotRootPath: Path): Path? {
        val next = namingStrategy.initial() ?: return null
        val filename = namingStrategy.build(next)

        return build(snapshotRootPath, filename)
    }

}