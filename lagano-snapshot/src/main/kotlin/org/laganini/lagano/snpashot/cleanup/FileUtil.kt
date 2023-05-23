package org.laganini.lagano.snpashot.cleanup

import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class FileUtil {

    companion object {

        @JvmStatic
        fun truncate(path: Path) {
            FileChannel
                .open(
                    path,
                    StandardOpenOption.WRITE
                )
                .truncate(0)
                .close()
        }
    }

}