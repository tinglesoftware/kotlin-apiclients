package software.tingle.models

import java.util.Locale

abstract class AbstractSyncableModel {
    abstract val importId: String
    abstract val importHash: String

    protected fun computeWeakHash(string: String): String {
        return String.format(Locale.US, "%08x%08x", string.hashCode(), string.length)
    }
}
