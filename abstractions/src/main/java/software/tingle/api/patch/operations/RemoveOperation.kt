package software.tingle.api.patch.operations

import com.google.gson.annotations.Expose
import software.tingle.api.patch.JsonPatchOperation

class RemoveOperation(@Expose val path: String) : JsonPatchOperation("remove")
