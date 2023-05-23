package org.laganini.lagano.migration

import javax.sql.DataSource

interface SqlSnapshotContainer {

    fun delegate(): Any

    fun dataSource(): DataSource

    fun start()

    fun stop()

    fun snapshot()

    fun restore()

}