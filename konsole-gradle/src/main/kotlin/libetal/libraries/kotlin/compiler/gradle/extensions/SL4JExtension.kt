package libetal.libraries.kotlin.compiler.gradle.extensions

import org.gradle.api.model.ObjectFactory

open class SL4JExtension(objects: ObjectFactory) : GradleExtension(objects) {
    var version by createProperty<String?>(null)
    var enabled by createProperty<Boolean>(true)
    var jvmTargetName by createProperty<String?>(null)
}
