package org.laganini.lagano.snpashot.storage

import org.laganini.lagano.snpashot.name.NamingStrategy
import java.nio.file.Path

interface StorageStrategy<T : Comparable<T>> {

    fun all(snapshotRootPath: Path): List<Path>

    fun latest(snapshotRootPath: Path): Path?

    fun initial(snapshotRootPath: Path): Path?

    fun build(path: Path, filename: String): Path?

    fun namingStrategy(): NamingStrategy<T>

}