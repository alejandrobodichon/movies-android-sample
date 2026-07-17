
import com.android.build.gradle.LibraryExtension
import com.example.moviesexample.addUnitTestDependencies
import com.example.moviesexample.libs
import com.example.moviesexample.testImplementation
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