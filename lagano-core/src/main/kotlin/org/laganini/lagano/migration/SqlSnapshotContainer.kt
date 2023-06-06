package org.laganini.lagano.migration

import javax.sql.DataSource

interface SqlSnapshotContainer {

    fun start()

    fun stop()

    fun snapshot()

    fun restore()

}