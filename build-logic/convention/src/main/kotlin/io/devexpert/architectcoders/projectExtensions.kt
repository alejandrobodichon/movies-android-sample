package io.devexpert.architectcoders

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun DependencyHandler.implementation(dependency: Any) =
    add("implementation", dependency)

internal fun DependencyHandler.testImplementation(dependency: Any) =
    add("testImplementation", dependency)

internal fun DependencyHandler.ksp(dependency: Any) =
    add("ksp", dependency)

internal fun Project.addUnitTestDependencies() {
    dependencies {
        testImplementation(project.libs.findLibrary("junit").get())
        testImplementation(project.libs.findLibrary("mockito.kotlin").get())
        testImplementation(project.libs.findLibrary("coroutines.test").get())
        testImplementation(project.libs.findLibrary("turbine").get())
    }
}