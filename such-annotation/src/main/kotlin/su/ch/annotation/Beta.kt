package su.ch.annotation

/**
 * Code annotated as `Beta` should be reliable but may undergo significant change.
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
annotation class Beta
