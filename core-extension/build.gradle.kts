plugins {
    alias(libs.plugins.custom.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.andyha.coreextension"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", ".aar"))))

    implementation(projects.coreResource)

    implementation(libs.bundles.coreUI)
    implementation(libs.bundles.extrasLibs)
    implementation(libs.bundles.supportLibs)
    implementation(libs.bundles.networking)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.lifecycle)
}
