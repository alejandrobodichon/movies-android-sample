plugins {
    id("moviesexample.jvm.library")
    id("moviesexample.di.library")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(project(":core:testing"))
}
