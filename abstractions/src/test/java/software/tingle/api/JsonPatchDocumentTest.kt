package software.tingle.api

import com.google.gson.Gson
import org.junit.Assert
import org.junit.Test
import software.tingle.api.patch.JsonPatchDocument

class JsonPatchDocumentTest {

    @Test
    fun testAddOperation(){
        val document = JsonPatchDocument()
            .add("/a/b/c", arrayOf("foo", "bar"))

        val expectedJson = "[{\"path\":\"/a/b/c\",\"value\":[\"foo\",\"bar\"],\"op\":\"add\"}]"
        val actualJson = Gson().toJson(document.getOperations())
        Assert.assertEquals(expectedJson, actualJson)
    }

    @Test
    fun testRemoveOperation(){
        val document = JsonPatchDocument()
            .remove("/a/b/c")

        val expectedJson = "[{\"path\":\"/a/b/c\",\"op\":\"remove\"}]"
        val actualJson = Gson().toJson(document.getOperations())
        Assert.assertEquals(expectedJson, actualJson)
    }

    @Test
    fun testReplaceOperation(){
        val document = JsonPatchDocument()
            .replace("/a/b/c", "foo")

        val expectedJson = "[{\"path\":\"/a/b/c\",\"value\":\"foo\",\"op\":\"replace\"}]"
        val actualJson = Gson().toJson(document.getOperations())
        Assert.assertEquals(expectedJson, actualJson)
    }

    @Test
    fun testTestOperation(){
        val document = JsonPatchDocument()
            .test("/a/b/c", "foo")

        val expectedJson = "[{\"path\":\"/a/b/c\",\"value\":\"foo\",\"op\":\"test\"}]"
        val actualJson = Gson().toJson(document.getOperations())
        Assert.assertEquals(expectedJson, actualJson)
    }

    @Test
    fun testMoveOperation(){
        val document = JsonPatchDocument()
            .move("/a/b/c", "/a/b/d")

        val expectedJson = "[{\"from\":\"/a/b/c\",\"path\":\"/a/b/d\",\"op\":\"move\"}]"
        val actualJson = Gson().toJson(document.getOperations())
        Assert.assertEquals(expectedJson, actualJson)
    }

    @Test
    fun testCopyOperation(){
        val document = JsonPatchDocument()
            .copy("/a/b/c", "/a/b/e")

        val expectedJson = "[{\"path\":\"/a/b/c\",\"from\":\"/a/b/e\",\"op\":\"copy\"}]"
        val actualJson = Gson().toJson(document.getOperations())
        Assert.assertEquals(expectedJson, actualJson)
    }
}