plugins {
    alias(libs.plugins.custom.library)
    alias(libs.plugins.custom.hilt)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.andyha.feature.musiclist"
}

dependencies {

    implementation(projects.coreUi)
    implementation(projects.coreNetwork)
    implementation(projects.coreResource)
    implementation(projects.coreExtension)
    implementation(projects.coreUtils)
    implementation(projects.coreData)
    implementation(projects.coreConfig)

    implementation(projects.demoMusic.musicDomain)
    implementation(projects.demoMusic.musicCommon)
    implementation(projects.demoMusic.musicService)

    implementation(libs.bundles.coreUI)
    implementation(libs.bundles.supportLibs)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.media3)
}
