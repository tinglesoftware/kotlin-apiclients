package software.tingle.api.patch

import com.google.gson.annotations.Expose

abstract class JsonPatchOperation(@Expose val op: String)
