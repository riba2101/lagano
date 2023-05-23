package org.laganini.lagano.snpashot.cleanup

import java.nio.file.Path

class KeepAllCleanStrategy : CleanStrategy {

    override fun clean(path: Path) {
        //noop
    }

}