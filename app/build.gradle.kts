plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("moviesexample.android.application")
    id("moviesexample.android.application.compose")
    id("moviesexample.di.library.compose")
    id("moviesexample.android.room")
    id("moviesexample.jvm.retrofit")
    id("com.google.firebase.crashlytics")
    id("moviesexample.kover")
}

android {
    namespace = "com.example.moviesexample"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.moviesexample"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    configurations.all {
        resolutionStrategy {
            force("org.jetbrains:annotations:23.0.0")
        }
        exclude(group = "com.intellij", module = "annotations")
    }
}

dependencies {
    // Modules
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.feature.detail)
    implementation(projects.feature.home)

    implementation(libs.firebase.analytics)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    // Navigation
    implementation(libs.androidx.navigation.compose)
}
