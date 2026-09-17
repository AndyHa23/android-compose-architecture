dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
        create("modules"){
            version("core-ui", ":core-ui")
        }
    }
}

rootProject.name = "build-logic"

include(":convention")