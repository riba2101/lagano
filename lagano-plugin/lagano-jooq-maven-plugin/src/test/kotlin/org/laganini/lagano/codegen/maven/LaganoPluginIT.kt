package org.laganini.lagano.codegen.maven

//import org.apache.maven.plugin.testing.AbstractMojoTestCase
//import org.apache.maven.plugin.testing.MojoRule
//import org.apache.maven.project.MavenProject
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import java.io.File


//class LaganoPluginIT : AbstractMojoTestCase() {

//    private val mojoRule: MojoRule = MojoRule(this)
//
//    @BeforeEach
//    override fun setUp() {
//        super.setUp()
//    }
//
//    @Test
//    fun shouldRunEmpty() {
//        val pluginXml = File(getBasedir(), "src/test/resources/units/empty")
//        val project: MavenProject? = mojoRule.readMavenProject(pluginXml)
//        val mojo: LaganoPlugin = lookupConfiguredMojo(project, "lagano-generate") as LaganoPlugin
//
//        mojo.execute()
//    }

//}