package libetal.libraries.kotlin.compiler.gradle.extensions


import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory

open class KonsoleGradleExtension(objects: ObjectFactory) : GradleExtension(objects) {

    var enabled by createProperty<Boolean>(true)

    var enableInfoProperty by createProperty<Boolean>(enabled)

    internal var sl4j: SL4JExtension? = null

    var enableInfo
        get() = enabled && enableInfoProperty
        set(value) {
            enableInfoProperty = enabled && value
        }

    var enableDebugProperty by createProperty<Boolean>(enabled)

    var enableDebug
        get() = enabled && enableDebugProperty
        set(value) {
            enableDebugProperty = enabled && value
        }

    var enableWarningProperty by createProperty<Boolean>(enabled)

    var enableWarning
        get() = enabled && enableWarningProperty
        set(value) {
            enableWarningProperty = enabled && value
        }

    var enableWTFProperty by createProperty<Boolean>(enabled)

    var enableWTF
        get() = enabled && enableWTFProperty
        set(value) {
            enableWTFProperty = enabled && value
        }

    fun sl4j(action: Action<SL4JExtension>) {
        sl4j = SL4JExtension(objects).also {
            action.execute(it)
        }
    }

}
