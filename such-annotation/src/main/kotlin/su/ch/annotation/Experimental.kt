package su.ch.annotation

/**
 * Code annotated as `Experimental` may change entirely or be removed.
 */
@Retention(AnnotationRetention.BINARY)
@Target(
        AnnotationTarget.ANNOTATION_CLASS,
        AnnotationTarget.CONSTRUCTOR,
        AnnotationTarget.FIELD,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.CLASS,
        AnnotationTarget.FILE)
@MustBeDocumented
annotation class Experimental
