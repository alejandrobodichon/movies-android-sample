import org.gradle.api.Plugin
import org.gradle.api.Project
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.kotlin.dsl.configure


class KoverConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlinx.kover")

            extensions.configure<KoverProjectExtension> {
                reports {
                    filters {
                        excludes {
                            classes(
                                "*_Factory",
                                "*_Factory\$*",
                                "*_HiltModules*",
                                "*_Impl",
                                "*.BuildConfig",
                                "*Hilt_*",
                                "hilt_aggregated_deps.*",
                                "dagger.hilt.*",
                                "*_MembersInjector",
                                "*.databinding.*",
                                "*.di.*", // módulos de Hilt/Dagger, si los tenés separados así
                            )
                            annotatedBy(
                                "*Generated*",
                            )
                        }
                    }
                }
            }
        }
    }
}