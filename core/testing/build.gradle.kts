
plugins {
    id("moviesexample.jvm.library")
}
listOf(
    "assembleDebug",
    "assembleDebugAndroidTest",
    "assembleDebugUnitTest",
    "assembleReleaseUnitTest"
).forEach { taskName ->
    tasks.register(taskName) {
        dependsOn("assemble", "testClasses")
    }
}
dependencies {
    implementation(projects.core.domain)
    implementation(libs.coroutines.test)
    implementation(libs.junit)
}
