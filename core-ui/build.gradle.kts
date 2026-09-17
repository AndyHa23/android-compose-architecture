plugins {
    alias(libs.plugins.custom.library)
    alias(libs.plugins.custom.hilt)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.andyha.coreui"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", ".aar"))))

    implementation(projects.coreData)
    implementation(projects.coreUtils)
    implementation(projects.coreExtension)
    implementation(projects.coreResource)
    implementation(projects.coreNetwork)

    implementation(libs.bundles.coreUI)
    implementation(libs.bundles.supportLibs)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.networking)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.paging)
}
