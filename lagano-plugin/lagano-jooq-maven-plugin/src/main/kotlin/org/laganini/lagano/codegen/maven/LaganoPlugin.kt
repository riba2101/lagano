package org.laganini.lagano.codegen.maven

import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.plugins.annotations.ResolutionScope
import org.apache.maven.project.MavenProject
import org.jooq.codegen.maven.Plugin
import org.jooq.meta.jaxb.Jdbc
import org.laganini.lagano.migration.LaganoContainerProvider
import org.laganini.lagano.migration.LaganoFlywayConfigProvider
import org.testcontainers.containers.JdbcDatabaseContainer
import java.io.File
import java.lang.reflect.Field
import java.net.URL
import java.net.URLClassLoader


@Mojo(
    name = "lagano-generate",
    defaultPhase = LifecyclePhase.GENERATE_SOURCES,
    requiresDependencyResolution = ResolutionScope.TEST,
    threadSafe = true
)
class LaganoPlugin : Plugin() {

    @Parameter(property = "project", required = true, readonly = true)
    private var project: MavenProject? = null

    @Parameter
    private var containerProvider: String? = null

    @Parameter
    private var flywayProvider: String? = null

    override fun execute() {
        if (containerProvider == null) {
            throw IllegalArgumentException("Container provider missing")
        }
        if (flywayProvider == null) {
            throw IllegalArgumentException("Flyway config provider missing")
        }

        val pluginClassLoader = getClassLoader(project)

        val containerProvider = Class
            .forName(containerProvider, true, pluginClassLoader)
            .getDeclaredConstructor()
            .newInstance() as LaganoContainerProvider
        val container = containerProvider.provide()

        val flywayConfigProvider = Class
            .forName(flywayProvider, true, pluginClassLoader)
            .getDeclaredConstructor()
            .newInstance() as LaganoFlywayConfigProvider
        val flywayConfig = flywayConfigProvider.provide()

        try {
            container.start()

            val flyway = flywayConfig
                .dataSource(container.dataSource())
                .load()

            flyway.migrate()
            container.snapshot()

            //set parent project
            val superProjectField: Field = javaClass.superclass.getDeclaredField("project")
            superProjectField.isAccessible = true
            superProjectField.set(this, project)

            //set jdbc
            val jdbcDatabaseContainer = container.delegate() as JdbcDatabaseContainer<*>
            val superJdbcField: Field = javaClass.superclass.getDeclaredField("jdbc")
            superJdbcField.isAccessible = true
            val jdbc: Jdbc = superJdbcField.get(this) as Jdbc
            jdbc.apply {
                driver = jdbcDatabaseContainer.getDriverClassName()
                url = jdbcDatabaseContainer.getJdbcUrl()
                user = jdbcDatabaseContainer.getUsername()
                password = jdbcDatabaseContainer.getPassword()
            }
            superJdbcField.set(this, jdbc)

            super.execute()
        } finally {
            container.stop()
        }
    }

    private fun getClassLoader(project: MavenProject?): ClassLoader? {
        return try {
            if (project == null) {
                log.warn("Couldn't load Maven Project - using the default classloader.")
                return javaClass.classLoader
            }

            val classpathElements: MutableList<String> = project.compileClasspathElements
            classpathElements.add(project.build.sourceDirectory)
            classpathElements.add(project.build.outputDirectory)
            classpathElements.add(project.build.testSourceDirectory)
            classpathElements.add(project.build.testOutputDirectory)
            log.debug("Plugin classpath: $classpathElements")

            val urls: Array<URL?> = arrayOfNulls(classpathElements.size)
            for (i in classpathElements.indices) {
                urls[i] = File(classpathElements[i] as String?).toURI().toURL()
            }

            URLClassLoader(urls, this.javaClass.classLoader)
        } catch (e: Exception) {
            log.warn("Couldn't get the classloader.")
            javaClass.classLoader
        }

    }

}