import java.util.Properties

plugins {
    alias(libs.plugins.custom.application)
    alias(libs.plugins.custom.hilt)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.gsm.service)
}

// Signing credentials are never stored in version control. They are read from the
// environment first (CI) and fall back to an untracked local signing.properties file.
val signingProperties = Properties().apply {
    val file = rootProject.file("signing.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}

fun signingValue(key: String, envName: String): String? =
    System.getenv(envName) ?: signingProperties.getProperty(key)

val releaseStoreFilePath = signingValue("storeFilePath", "ANDROID_KEYSTORE_PATH")
// Accept a path relative to the project root, to this module, or an absolute path.
val releaseKeystoreFile = releaseStoreFilePath
    ?.takeIf { it.isNotBlank() }
    ?.let { path -> listOf(rootProject.file(path), project.file(path)).firstOrNull { it.exists() } }
val hasReleaseSigning = releaseKeystoreFile != null

android {
    namespace = "com.andyha.demomusic"

    defaultConfig {
        applicationId = "com.andyha.demomusic.app"
        versionCode = 1
        versionName = "0.0.1"
    }

    signingConfigs {
        if (hasReleaseSigning) {
            create("release") {
                storeFile = releaseKeystoreFile
                storePassword = signingValue("storePassword", "ANDROID_KEYSTORE_PASSWORD")
                keyAlias = signingValue("keyAlias", "ANDROID_KEY_ALIAS")
                keyPassword = signingValue("keyPassword", "ANDROID_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.findByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        create("releaseDebuggable") {
            initWith(getByName("release"))
            isDebuggable = true
            signingConfig = signingConfigs.findByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules-no-obfuscation.pro"
            )
            matchingFallbacks.add("release")
        }
    }

    flavorDimensions.add("env")
    productFlavors {
        val envConfigProperties = Properties()
        create("dev") {
            dimension = "env"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "_DEV"

            envConfigProperties.load(File("demo-music/demo-music-app/env-config/dev-env-config.properties").reader())
            buildConfigField("String", "baseUrl", envConfigProperties.getProperty("baseUrl"))
        }
        create("staging") {
            dimension = "env"
            applicationIdSuffix = ".stg"
            versionNameSuffix = "_STG"

            envConfigProperties.load(File("demo-music/demo-music-app/env-config/staging-env-config.properties").reader())
            buildConfigField("String", "baseUrl", envConfigProperties.getProperty("baseUrl"))
        }
        create("production") {
            dimension = "env"

            envConfigProperties.load(File("demo-music/demo-music-app/env-config/production-env-config.properties").reader())
            buildConfigField("String", "baseUrl", envConfigProperties.getProperty("baseUrl"))
        }
    }
}

dependencies {
    implementation(projects.coreUi)
    implementation(projects.coreNetwork)
    implementation(projects.coreResource)
    implementation(projects.coreExtension)
    implementation(projects.coreUtils)
    implementation(projects.coreData)
    implementation(projects.coreConfig)

    implementation(projects.demoMusic.featureMusicList)
    implementation(projects.demoMusic.featureMusicPlayer)
    implementation(projects.demoMusic.featureSettings)
    implementation(projects.demoMusic.musicCommon)
    implementation(projects.demoMusic.musicDomain)
    implementation(projects.demoMusic.musicService)
    implementation(projects.demoMusic.musicData)

    implementation(libs.bundles.coreUI)
    implementation(libs.bundles.supportLibs)
    implementation(libs.bundles.networking)
    implementation(libs.bundles.playServices)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.media3)
}
