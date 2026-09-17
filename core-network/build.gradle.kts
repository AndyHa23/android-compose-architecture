plugins {
    alias(libs.plugins.custom.library)
    alias(libs.plugins.custom.hilt)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.andyha.corenetwork"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", ".aar"))))

    implementation(projects.coreResource)
    implementation(projects.coreData)
    implementation(projects.coreExtension)
    implementation(projects.coreConfig)

    implementation(libs.bundles.supportLibs)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.networking)
    implementation(libs.bundles.firebase)
}
