enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "android-compose-architecture"

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://jitpack.io")
        maven(url = "https://plugins.gradle.org/m2/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":core-ui")
include(":core-network")
include(":core-testing")
include(":core-utils")
include(":core-extension")
include(":core-resource")
include(":core-data")
include(":core-config")

include(":demo-music:demo-music-app")
include(":demo-music:feature-music-list")
include(":demo-music:feature-music-player")
include(":demo-music:music-domain")
include(":demo-music:music-data")
include(":demo-music:music-service")
include(":demo-music:music-common")
include(":demo-music:feature-settings")

include(":demo-weather:demo-weather-app")
include(":demo-weather:feature-login")
include(":demo-weather:feature-weather")
include(":demo-weather:weather-common")
include(":demo-weather:weather-domain")
include(":demo-weather:weather-data")
