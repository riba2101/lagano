package org.laganini.lagano.codegen.maven

import org.apache.maven.plugin.testing.AbstractMojoTestCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File


open class LaganoPluginIT : AbstractMojoTestCase() {

    @BeforeEach
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun shouldRunEmpty() {
        val pluginXml = File(getBasedir(), "src/test/resources/units/empty/pom.xml")
        val mojo: LaganoPlugin = lookupMojo("lagano-generate", pluginXml) as LaganoPlugin

        mojo.execute()
    }

}