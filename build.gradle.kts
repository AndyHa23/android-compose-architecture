buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://plugins.gradle.org/m2/")
    }

    dependencies{
        classpath(libs.google.services)
        classpath(libs.ktlint.gradle)
        classpath(libs.kotlin.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.google.gsm.service) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ktlint) apply false

    // custom plugins
    alias(libs.plugins.custom.application) apply false
    alias(libs.plugins.custom.library) apply false
    alias(libs.plugins.custom.hilt) apply false
    alias(libs.plugins.custom.room) apply false
}
