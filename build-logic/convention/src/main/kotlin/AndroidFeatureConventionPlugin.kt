
import com.android.build.gradle.LibraryExtension
import io.devexpert.architectcoders.addUnitTestDependencies
import io.devexpert.architectcoders.libs
import io.devexpert.architectcoders.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("moviesexample.android.library.compose")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }
            addUnitTestDependencies()
            dependencies{
                testImplementation(libs.findLibrary("hilt.testing").get())
            }
        }
    }
}