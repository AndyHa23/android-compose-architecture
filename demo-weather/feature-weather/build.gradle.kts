plugins {
    alias(libs.plugins.custom.library)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.custom.hilt)
}

android {
    namespace = "com.andyha.feature.weather"
}

dependencies {
    implementation(projects.coreUi)
    implementation(projects.coreData)
    implementation(projects.coreNetwork)
    implementation(projects.coreUtils)
    implementation(projects.coreExtension)
    implementation(projects.coreResource)
    implementation(projects.coreConfig)
    implementation(projects.demoWeather.weatherCommon)
    implementation(projects.demoWeather.weatherDomain)

    implementation(libs.bundles.coreUI)
    implementation(libs.bundles.supportLibs)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.di)
    implementation(libs.bundles.logging)
    implementation(libs.coil.kt.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
}