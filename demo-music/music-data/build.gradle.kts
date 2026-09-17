plugins {
    alias(libs.plugins.custom.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.custom.hilt)
    alias(libs.plugins.custom.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
//    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.andyha.musicdata"
}

dependencies {
    implementation(projects.coreNetwork)
    implementation(projects.demoMusic.musicDomain)

    implementation(libs.bundles.coreUI)
    implementation(libs.bundles.supportLibs)
    implementation(libs.bundles.networking)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.room)
    implementation(libs.bundles.media3)

    testImplementation(libs.bundles.unitTesting)
}
