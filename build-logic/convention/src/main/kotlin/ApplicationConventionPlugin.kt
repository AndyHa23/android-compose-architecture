import com.andyha.buildlogic.configureCompose
import com.andyha.buildlogic.configureApplication
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jlleitschuh.gradle.ktlint")
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig.targetSdk = AndroidConfig.TARGET_SDK_VERSION
                configureApplication(this)
                configureCompose(this)
            }
        }
    }
}
