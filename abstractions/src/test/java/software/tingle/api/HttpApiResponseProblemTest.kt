package software.tingle.api

import com.google.gson.Gson
import org.junit.Assert
import org.junit.Test

class HttpApiResponseProblemTest {

    @Test
    fun serializationProducedExpectedJson() {
        val problem = HttpApiResponseProblem()
        problem.title = "insufficient_balance"
        problem.detail = "Add more money!"
        val map = HashMap<String, Array<String>>()
        map["SessionId"] = arrayOf("The SessionId is required")
        problem.errors = map

        // serialize the problem and ensure that the produced value is the same
        val expectedJson = "{\"title\":\"insufficient_balance\",\"detail\":\"Add more money!\",\"errors\":{\"SessionId\":[\"The SessionId is required\"]}}"
        val actualJson = Gson().toJson(problem)
        Assert.assertEquals(expectedJson, actualJson)
    }

    @Test
    fun deserializationWorksWithErrors() {
        // serialize the problem and ensure that the produced value is the same
        val json = "{\"title\":\"insufficient_balance\",\"detail\":\"Add more money!\",\"errors\":{\"SessionId\":[\"The SessionId is required\"]}}"
        val problem = Gson().fromJson<HttpApiResponseProblem>(json, HttpApiResponseProblem::class.java)
        Assert.assertEquals("insufficient_balance", problem.title)
        Assert.assertEquals("insufficient_balance", problem.code)
        Assert.assertEquals("Add more money!", problem.detail)
        Assert.assertEquals("Add more money!", problem.description)
        Assert.assertFalse(problem.errors.isNullOrEmpty())
        val map = problem.errors as Map<String, Array<String>>
        Assert.assertTrue(map.containsKey("SessionId"))
        val values = map["SessionId"]
        Assert.assertArrayEquals(arrayOf("The SessionId is required"), values)
    }

    @Test
    fun deserializationWorksWithErrorsNoProblemDetail() {
        val json = "{\"errors\":{\"label\":[\"The Label field is required.\"]},\"type\":\"https://tools.ietf.org/html/rfc7231#section-6.5.1\",\"title\":\"One or more validation errors occurred.\",\"status\":400,\"traceId\":\"00-74ffb625470bef45bb83714bf9670384-f477cc2a8825074d-00\"}"
        val problem = Gson().fromJson(json, HttpApiResponseProblem::class.java)
        Assert.assertEquals("One or more validation errors occurred.", problem.title)
        Assert.assertEquals("The Label field is required.", problem.description)
    }

    @Test
    fun deserializationWorksWithNoErrors() {
        // serialize the problem and ensure that the produced value is the same
        val json = "{\"title\":\"insufficient_balance\",\"detail\":\"Add more money!\"}"
        val problem = Gson().fromJson<HttpApiResponseProblem>(json, HttpApiResponseProblem::class.java)
        Assert.assertEquals("insufficient_balance", problem.title)
        Assert.assertEquals("insufficient_balance", problem.code)
        Assert.assertEquals("Add more money!", problem.detail)
        Assert.assertEquals("Add more money!", problem.description)
        Assert.assertNull(problem.errors)
    }

    @Test
    fun problemDetailsPrioritizesRFC3986() {
        // serialize the problem and ensure that the produced value is the same
        val json = "{\"title\":\"insufficient_balance\",\"detail\":\"who cares\",\"error_code\":\"zero_balance\",\"error_description\":\"go away\"}"
        val problem = Gson().fromJson<HttpApiResponseProblem>(json, HttpApiResponseProblem::class.java)

        Assert.assertEquals("insufficient_balance", problem.title)
        Assert.assertEquals("insufficient_balance", problem.code)
        Assert.assertEquals("zero_balance", problem.legacyCode)

        Assert.assertEquals("who cares", problem.detail)
        Assert.assertEquals("who cares", problem.description)
        Assert.assertEquals("go away", problem.legacyDescription)
    }

    @Test
    fun problemDetailsPrioritizesFallsBackToLegacy() {
        // serialize the problem and ensure that the produced value is the same
        val json = "{\"title\":null,\"detail\":null,\"error_code\":\"zero_balance\",\"error_description\":\"go away\"}"
        val problem = Gson().fromJson<HttpApiResponseProblem>(json, HttpApiResponseProblem::class.java)

        Assert.assertNull(problem.title)
        Assert.assertEquals("zero_balance", problem.code)
        Assert.assertEquals("zero_balance", problem.legacyCode)


        Assert.assertNull(problem.detail)
        Assert.assertEquals("go away", problem.description)
        Assert.assertEquals("go away", problem.legacyDescription)
    }
}