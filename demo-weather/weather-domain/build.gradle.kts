plugins {
    alias(libs.plugins.custom.library)
    alias(libs.plugins.custom.hilt)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.andyha.weatherdomain"
}

dependencies {
    implementation(projects.coreData)
}