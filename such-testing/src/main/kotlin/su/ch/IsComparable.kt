package su.ch

import nl.jqno.equalsverifier.EqualsVerifier


object IsComparable {

    @JvmStatic fun <T> isComparable(cls: Class<T>) = EqualsVerifier.forClass(cls).verify()
}