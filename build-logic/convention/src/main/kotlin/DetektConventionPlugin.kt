import com.example.moviesexample.libs
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            extensions.configure<DetektExtension> {
                buildUponDefaultConfig = true
                allRules = false
                config.setFrom(rootProject.file("config/detekt/detekt.yml"))
                baseline = file("$projectDir/config/detekt/baseline.xml")
                parallel = true
            }

            tasks.withType(io.gitlab.arturbosch.detekt.Detekt::class.java).configureEach {
                reports {
                    html.required.set(true)
                    xml.required.set(true)
                    txt.required.set(false)
                    sarif.required.set(true) // para integrar con GitHub Code Scanning
                }
            }

            dependencies.add(
                "detektPlugins",
                "io.gitlab.arturbosch.detekt:detekt-formatting:${libs.findVersion("detekt").get()}"
            )
        }
    }
}