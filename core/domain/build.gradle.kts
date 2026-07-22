plugins {
    id("moviesexample.jvm.library")
    id("moviesexample.di.library")
    id("moviesexample.kover")
    id("moviesexample.detekt")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(projects.core.testing)
}