plugins {
    alias(libs.plugins.custom.library)
    alias(libs.plugins.custom.hilt)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.andyha.musicservice"
}

dependencies {
    implementation(projects.coreData)
    implementation(projects.coreNetwork)
    implementation(projects.demoMusic.musicDomain)
    implementation(projects.demoMusic.musicCommon)

    implementation(libs.bundles.coreUI)
    implementation(libs.bundles.supportLibs)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.media3)
}
