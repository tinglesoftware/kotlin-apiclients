package software.tingle.api.patch.operations


import com.google.gson.annotations.Expose
import software.tingle.api.patch.JsonPatchOperation

class ReplaceOperation(@Expose val path: String, @Expose val value: Any?) : JsonPatchOperation("replace")
