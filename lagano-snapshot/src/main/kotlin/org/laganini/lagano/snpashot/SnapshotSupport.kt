package org.laganini.lagano.snpashot

import org.laganini.lagano.snpashot.cleanup.CleanAllStrategy
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.notExists

open class SnapshotSupport constructor(private val configuration: SnapshotConfiguration = SnapshotConfiguration()) {

    fun setup() {
        if (!configuration.enabled) {
            return
        }

        //create folder
        val snapshotRootPath = configuration.path
        if (!snapshotRootPath.exists()) {
            snapshotRootPath.createDirectory()
        }

        val storageStrategy = configuration.storageStrategy
        //clear all files on rebuild
        if (configuration.rebuild) {
            CleanAllStrategy(storageStrategy).clean(snapshotRootPath)
        }

        //find latest version and prepare file
        val latest = storageStrategy.latest(snapshotRootPath) ?: storageStrategy.initial(snapshotRootPath) ?: return
        if (latest.notExists()) {
            latest.createFile()
        }

        //cleanup old per strategy
        configuration.cleanupStrategy.clean(snapshotRootPath)
    }

    fun buildFsPath(): Path? {
        return configuration.storageStrategy.latest(configuration.path)
    }

    fun buildContainerPath(): String {
        return "/" + configuration.containerFilename
    }


}