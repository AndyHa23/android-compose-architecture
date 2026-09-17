plugins {
    alias(libs.plugins.custom.library)
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.andyha.coreutils"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", ".aar"))))

    implementation(projects.coreResource)
    implementation(projects.coreExtension)
    implementation(projects.coreNetwork)
    implementation(projects.coreData)

    implementation(libs.bundles.coreUI)
    implementation(libs.bundles.supportLibs)
    implementation(libs.bundles.networking)
    implementation(libs.bundles.logging)

    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.21")
}
