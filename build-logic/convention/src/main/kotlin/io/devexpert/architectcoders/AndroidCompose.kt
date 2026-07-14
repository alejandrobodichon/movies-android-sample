package io.devexpert.architectcoders

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

    commonExtension.apply {

        buildFeatures {
            compose = true
        }

    }

    dependencies {
        val composeBom = libs.findLibrary("androidx.compose.bom").get()
        implementation(platform(composeBom))
        implementation(libs.findLibrary("androidx.ui").get())
        implementation(libs.findLibrary("androidx.ui.graphics").get())
        implementation(libs.findLibrary("androidx.ui.tooling.preview").get())
        implementation(libs.findLibrary("androidx.material3").get())
        implementation(libs.findLibrary("coil.compose").get())
        implementation(libs.findLibrary("androidx.activity.compose").get())
        testImplementation(libs.findLibrary("compose.ui.test.junit4").get())
        testImplementation(libs.findLibrary("robolectric").get())
        testImplementation(libs.findLibrary("androidx.ui.test.manifest").get())

        add("debugImplementation", libs.findLibrary("androidx.ui.tooling").get())
    }

}