package org.laganini.lagano.snpashot.storage

import org.laganini.lagano.snpashot.name.NamingStrategy
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.walk

abstract class AbstractStorageStrategy<T : Comparable<T>>(
    protected val namingStrategy: NamingStrategy<T>
) : StorageStrategy<T> {

    @OptIn(ExperimentalPathApi::class)
    override fun all(snapshotRootPath: Path): List<Path> {
        return snapshotRootPath
            .walk()
            .filter { path -> namingStrategy.resolve(path.fileName.toString()) != null }
            .toList()
    }

    override fun build(path: Path, filename: String): Path {
        return Path(
            path.toAbsolutePath().toString(),
            filename
        )
    }

    override fun namingStrategy(): NamingStrategy<T> {
        return namingStrategy
    }
}