package org.laganini.lagano.snpashot

import org.laganini.lagano.snpashot.cleanup.CleanStrategy
import org.laganini.lagano.snpashot.cleanup.KeepAllCleanStrategy
import org.laganini.lagano.snpashot.name.SingleNamingStrategy
import org.laganini.lagano.snpashot.storage.FileStorageStrategy
import org.laganini.lagano.snpashot.storage.StorageStrategy
import java.nio.file.Path
import kotlin.io.path.Path

open class SnapshotConfiguration(
    val enabled: Boolean = System.getenv(PROPERTY_FEATURE_DB_SNAPSHOT).toBoolean(),
    val rebuild: Boolean = System.getenv(PROPERTY_FEATURE_DB_SNAPSHOT_REBUILD).toBoolean(),
    val path: Path = Path(System.getenv(PROPERTY_FEATURE_DB_SNAPSHOT_PATH) ?: DEFAULT_DATABASE_SNAPSHOT_FS_PATH),
    val containerFilename: String = DEFAULT_DATABASE_SNAPSHOT_FS_NAME,
    val storageStrategy: StorageStrategy<*> = FileStorageStrategy(SingleNamingStrategy(DEFAULT_DATABASE_SNAPSHOT_FS_NAME)),
    val cleanupStrategy: CleanStrategy = KeepAllCleanStrategy(),
) {
    companion object {
        const val DEFAULT_DATABASE_SNAPSHOT_FS_PATH = ".lagano"
        const val DEFAULT_DATABASE_SNAPSHOT_FS_NAME = "database-snapshot.sql"

        const val PROPERTY_FEATURE_DB_SNAPSHOT = "lagano.db.snapshot"
        const val PROPERTY_FEATURE_DB_SNAPSHOT_REBUILD = "lagano.db.snapshot.rebuild"
        const val PROPERTY_FEATURE_DB_SNAPSHOT_PATH = "lagano.db.snapshot.path"
    }
}