package com.andyha.buildlogic

import AndroidConfig
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureLibrary(
    commonExtension: LibraryExtension,
) {
    commonExtension.apply {
        compileSdk = AndroidConfig.COMPILE_SDK_VERSION
        buildToolsVersion = AndroidConfig.BUILD_TOOL_VERSION

        defaultConfig {
            minSdk = AndroidConfig.MIN_SDK_VERSION
            lint.targetSdk = AndroidConfig.TARGET_SDK_VERSION
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            consumerProguardFiles("consumer-rules.pro")
        }

        buildTypes {
            release {
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
                )
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
            isCoreLibraryDesugaringEnabled = true
        }

        buildFeatures {
            compose = true
        }
    }

    configureKotlin()

    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())
    }
}