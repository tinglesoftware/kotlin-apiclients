package software.tingle.api.patch.operations


import com.google.gson.annotations.Expose
import software.tingle.api.patch.JsonPatchOperation

class CopyOperation(@Expose val path: String, @Expose val from: String) : JsonPatchOperation("copy")
