package com.andyha.buildlogic

import AndroidConfig
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude


internal fun Project.configureApplication(
    applicationExtension: ApplicationExtension,
) {
    applicationExtension.apply {
        compileSdk = AndroidConfig.COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = AndroidConfig.MIN_SDK_VERSION
            targetSdk = AndroidConfig.TARGET_SDK_VERSION

            buildToolsVersion = AndroidConfig.BUILD_TOOL_VERSION

            multiDexEnabled = true

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            vectorDrawables {
                useSupportLibrary = true
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
            isCoreLibraryDesugaringEnabled = true
        }

        lint {
            abortOnError = false
            htmlReport = true
        }

        testOptions {
            unitTests {
                isReturnDefaultValues = false
                isIncludeAndroidResources = true
            }
        }

        buildFeatures {
            compose = true
            buildConfig = true
        }

        configurations.all {
            exclude(module = "javax.annotation")
            resolutionStrategy {
                exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-debug")
                exclude(group = "org.mockito", module = "mockito-inline")
                exclude(group = "org.mockito", module = "mockito-android")
                eachDependency {
                    if (requested.group == "androidx.work") {
                        useVersion("2.8.0-alpha02")
                    }
                }
            }
        }
    }

    configureKotlin()

    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())
    }
}