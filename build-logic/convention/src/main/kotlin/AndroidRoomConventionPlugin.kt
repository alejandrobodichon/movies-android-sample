
import com.example.moviesexample.implementation
import com.example.moviesexample.ksp
import com.example.moviesexample.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            dependencies {
                implementation(libs.findLibrary("androidx.room.ktx").get())
                ksp(libs.findLibrary("androidx.room.compiler").get())
            }
        }
    }
}