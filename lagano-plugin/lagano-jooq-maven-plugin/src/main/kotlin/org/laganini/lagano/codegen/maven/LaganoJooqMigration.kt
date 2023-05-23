package org.laganini.lagano.codegen.maven

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jooq.AttachableQueryPart
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.tools.jdbc.JDBCUtils

abstract class LaganoJooqMigration : BaseJavaMigration() {

    override fun migrate(context: Context?) {
        context?.connection.use { connection ->
            val dialect = JDBCUtils.dialect(connection)
            val create = DSL.using(dialect)

            migrate(create, context)
        }
    }

    protected abstract fun migrate(create: DSLContext, context: Context?): AttachableQueryPart

}