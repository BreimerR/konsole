package libetal.libraries.kotlin.compiler.gradle.extensions

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import kotlin.reflect.KProperty


abstract class GradleExtension(val objects: ObjectFactory) {

    protected inline fun <reified T> createProperty(default: T): Property<T> =
        objects.property(T::class.java).apply {
            if (default != null) convention(default)
        }

}


internal operator fun <T> Property<T>.getValue(gradleExtension: GradleExtension, property: KProperty<*>): T = get() as T

internal operator fun <T> Property<T>.setValue(
    gradleExtension: GradleExtension,
    property: KProperty<*>,
    value: T
) = set(value)
