import java.util.Properties

plugins {
    id("moviesexample.android.library")
    id("moviesexample.di.library")
    id("moviesexample.android.room")
    id("moviesexample.jvm.retrofit")
}

android {
    namespace = "com.example.data"
    testFixtures { enable = true }

    defaultConfig {

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").readText().byteInputStream())

        val tmdbApiKey = properties.getProperty("TMDB_API_KEY", "")
        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
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
    buildFeatures {
        buildConfig = true
    }

}

dependencies {
    implementation(libs.play.services.location)

    //Libs
    implementation(projects.core.domain)
    testImplementation(projects.core.testing)

    androidTestImplementation(libs.androidx.junit.ktx)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.coroutines.test)

    testFixturesImplementation(projects.core.domain)
    testFixturesImplementation(libs.coroutines.test)
    testFixturesImplementation(libs.kotlinx.coroutines.core)
}