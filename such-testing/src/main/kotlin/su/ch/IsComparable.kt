package su.ch

import nl.jqno.equalsverifier.EqualsVerifier


class IsComparable {

    companion object {
        @JvmStatic fun <T> isComparable(cls: Class<T>): Unit {
            EqualsVerifier.forClass(cls).verify()
            return Unit.Unit
        }
    }
}