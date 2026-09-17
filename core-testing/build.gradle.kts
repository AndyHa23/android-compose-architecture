plugins {
    alias(libs.plugins.custom.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    id("kotlin-kapt")
}

android {
    namespace = "com.andyha.coretesting"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", ".aar"))))

    implementation(projects.coreUi)
    implementation(projects.coreData)
    implementation(projects.coreResource)
    implementation(projects.coreExtension)

    implementation(libs.bundles.unitTesting)
    implementation(libs.bundles.androidTesting)
    implementation(libs.bundles.networking)

//    testImplementations(ProjectDependencies.unitTesting)
//    androidTestImplementations(ProjectDependencies.androidTesting)
}
