// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlinxSerialization) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.kover)
    alias(libs.plugins.detekt) apply false


}
dependencies {
    kover(project(":app"))
    kover(project(":core:data"))
    kover(project(":core:domain"))
    kover(project(":core:ui"))
    kover(project(":feature:detail"))
    kover(project(":feature:home"))
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    "*_Factory",
                    "*_Factory\$*",
                    "*_HiltModules*",
                    "*_HiltComponents*",
                    "*_Impl",
                    "*.BuildConfig",
                    "*Hilt_*",
                    "hilt_aggregated_deps.*",
                    "dagger.hilt.*",
                    "*_MembersInjector",
                    "*.databinding.*",
                )
                annotatedBy("*Generated*")
            }
        }
    }
}