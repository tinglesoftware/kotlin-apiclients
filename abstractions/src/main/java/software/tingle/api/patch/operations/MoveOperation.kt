package software.tingle.api.patch.operations

import com.google.gson.annotations.Expose
import software.tingle.api.patch.JsonPatchOperation

class MoveOperation(@Expose val from: String, @Expose val path: String) : JsonPatchOperation("move")
