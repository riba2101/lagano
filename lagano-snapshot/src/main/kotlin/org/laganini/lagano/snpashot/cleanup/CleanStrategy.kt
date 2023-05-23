package org.laganini.lagano.snpashot.cleanup

import java.nio.file.Path

interface CleanStrategy {

    fun clean(snapshotRootPath: Path)

}