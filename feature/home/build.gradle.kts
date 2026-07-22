
plugins {
    id("moviesexample.android.feature")
    id("moviesexample.di.library.compose")
    id("moviesexample.kover")

}

android {
    namespace = "com.example.feature.home"

    defaultConfig {

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    // Modules
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(projects.core.data)
    testImplementation(projects.core.testing)
    testImplementation(testFixtures(project(":core:data")))
}