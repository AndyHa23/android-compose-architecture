import com.andyha.buildlogic.libs
import com.andyha.buildlogic.roomBundle
import com.andyha.buildlogic.roomCompilerLib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class RoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.kapt")
            }

            dependencies {
                add("implementation", libs.roomBundle())
                add("kapt", libs.roomCompilerLib())
                add("kaptAndroidTest", libs.roomCompilerLib())
                add("kaptTest", libs.roomCompilerLib())
            }

            val kaptExtension = extensions.getByType<KaptExtension>()
            kaptExtension.apply {
                correctErrorTypes = true
            }
        }
    }
}