import io.devexpert.architectcoders.implementation
import io.devexpert.architectcoders.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class JvmRetrofitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                implementation(libs.findLibrary("retrofit").get())
                implementation(libs.findLibrary("retrofit.converter.kotlinx.serialization").get())
                implementation(libs.findLibrary("kotlinx.serialization.json").get())
                implementation(libs.findLibrary("okhttp-logging").get())
            }
        }
    }
}