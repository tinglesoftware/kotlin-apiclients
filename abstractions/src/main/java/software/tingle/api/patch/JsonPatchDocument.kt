package software.tingle.api.patch

import com.google.gson.annotations.Expose
import java.util.ArrayList

import software.tingle.api.patch.operations.AddOperation
import software.tingle.api.patch.operations.CopyOperation
import software.tingle.api.patch.operations.MoveOperation
import software.tingle.api.patch.operations.RemoveOperation
import software.tingle.api.patch.operations.ReplaceOperation
import software.tingle.api.patch.operations.TestOperation

class JsonPatchDocument {
    @Expose
    private var operations: MutableList<JsonPatchOperation> = ArrayList()

    /**
     * Add Operation. Will result in, for example
     * { "op": "add", "path": "/a/b/c", "value": [ "foo", "bar" ] }
     *
     * @param path  target location
     * @param value value to be added
     */
    fun add(path: String, value: Any): JsonPatchDocument {
        operations.add(AddOperation(path, value))
        return this
    }

    /**
     * Remove value at target location, Will result in, for example,
     * { "op": "remove", "path": "/a/b/c" }
     *
     * @param path target location
     */
    fun remove(path: String): JsonPatchDocument {
        operations.add(RemoveOperation(path))
        return this
    }

    /**
     * Replace value, Will result in, for example,
     * { "op": "replace", "path": "/a/b/c", "value": foo }
     *
     * @param path  target location
     * @param value value to be replaced
     */
    fun replace(path: String, value: Any?): JsonPatchDocument {
        operations.add(ReplaceOperation(path, value))
        return this
    }

    /**
     * Test value, will result in, for example,
     * { "op": "test", "path": "/a/b/c", "value": foo }
     *
     * @param path  target location
     * @param value test value
     */
    fun test(path: String, value: Any): JsonPatchDocument {
        operations.add(TestOperation(path, value))
        return this
    }

    /**
     * Removes a value from a specified location and adds it to the target location, will result in,
     * for example,
     * { "op": "move", "from": "/a/b/c", "path": "/a/b/d" }
     *
     * @param from source location
     * @param path target location
     */
    fun move(from: String, path: String): JsonPatchDocument {
        operations.add(MoveOperation(from, path))
        return this
    }

    /**
     * Copy the value at a specified location to the target location, will result in, for example,
     * { "op": "copy", "from": "/a/b/c", "path": "/a/b/e" }
     *
     * @param from source location
     * @param path target location
     */
    fun copy(from: String, path: String): JsonPatchDocument {
        operations.add(CopyOperation(from, path))
        return this
    }

    fun getOperations(): List<JsonPatchOperation> = operations
}
