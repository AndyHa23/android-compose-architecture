plugins {
    alias(libs.plugins.custom.library)
    alias(libs.plugins.custom.hilt)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.andyha.weathercommon"
}

dependencies {
    implementation(projects.coreUi)
    implementation(projects.demoWeather.weatherDomain)

    implementation(libs.bundles.coreUI)
    implementation(libs.bundles.supportLibs)
}
