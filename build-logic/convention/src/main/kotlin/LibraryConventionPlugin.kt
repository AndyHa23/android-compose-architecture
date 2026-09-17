import com.andyha.buildlogic.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import com.android.build.api.dsl.LibraryExtension
import com.andyha.buildlogic.configureLibrary

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jlleitschuh.gradle.ktlint")
            }

            extensions.configure<LibraryExtension> {
                defaultConfig.targetSdk = AndroidConfig.TARGET_SDK_VERSION
                configureLibrary(this)
                configureCompose(this)
            }
        }
    }
}
