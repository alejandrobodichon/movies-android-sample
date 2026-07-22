plugins {
    id("moviesexample.jvm.library")
    id("moviesexample.di.library")
    id("moviesexample.kover")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(projects.core.testing)
}