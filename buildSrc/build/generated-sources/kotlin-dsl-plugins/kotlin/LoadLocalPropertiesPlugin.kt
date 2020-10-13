/**
 * Precompiled [load-local-properties.gradle.kts][Load_local_properties_gradle] script plugin.
 *
 * @see Load_local_properties_gradle
 */
class LoadLocalPropertiesPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Load_local_properties_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
