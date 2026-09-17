plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.tools.build.gradle)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("Application") {
            id = "custom.application"
            version = "1.0.0"
            implementationClass = "ApplicationConventionPlugin"
        }

        register("Library") {
            id = "custom.library"
            version = "1.0.0"
            implementationClass = "LibraryConventionPlugin"
        }

        register("Hilt") {
            id = "custom.hilt"
            version = "1.0.0"
            implementationClass = "HiltConventionPlugin"
        }

        register("Room") {
            id = "custom.room"
            version = "1.0.0"
            implementationClass = "RoomConventionPlugin"
        }
    }
}