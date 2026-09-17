plugins {
    alias(libs.plugins.custom.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.custom.hilt)
    alias(libs.plugins.custom.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.andyha.weatherdata"
}

dependencies {
    implementation(projects.coreNetwork)
    implementation(projects.coreData)
    implementation(projects.coreUtils)
    implementation(projects.coreExtension)
    implementation(projects.demoWeather.weatherDomain)

    implementation(libs.bundles.coreUI)
    implementation(libs.bundles.supportLibs)
    implementation(libs.bundles.networking)
    implementation(libs.bundles.playServices)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.security)
    implementation(libs.bundles.room)

    testImplementation(libs.bundles.unitTesting)
}
