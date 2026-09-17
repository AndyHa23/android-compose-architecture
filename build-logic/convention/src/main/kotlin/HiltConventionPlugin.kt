import com.andyha.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(DAGGER_HILT_PLUGIN)
                apply(KOTLIN_KAPT_PLUGIN)
            }

            dependencies {
                add("implementation", libs.findLibrary(DAGGER_HILT_LIB).get())
                add("kapt", libs.findLibrary(DAGGER_HILT_COMPILER).get())
                add("kaptAndroidTest", libs.findLibrary(DAGGER_HILT_COMPILER).get())
                add("kaptTest", libs.findLibrary(DAGGER_HILT_COMPILER).get())
            }

            val kaptExtension = extensions.getByType<KaptExtension>()
            kaptExtension.apply {
                correctErrorTypes = true
            }
        }
    }

    companion object{
        const val DAGGER_HILT_PLUGIN = "dagger.hilt.android.plugin"
        const val KOTLIN_KAPT_PLUGIN = "org.jetbrains.kotlin.kapt"

        const val DAGGER_HILT_LIB = "dagger.hilt.android"
        const val DAGGER_HILT_COMPILER = "dagger.hilt.compiler"
    }
}