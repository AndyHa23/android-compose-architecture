plugins {
    alias(libs.plugins.custom.library)
    alias(libs.plugins.custom.hilt)
    alias(libs.plugins.custom.room)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.andyha.musicdomain"
}

dependencies {
    implementation(projects.coreData)

    implementation(libs.bundles.supportLibs)
    implementation(libs.bundles.networking)
    implementation(libs.bundles.media3)
}
