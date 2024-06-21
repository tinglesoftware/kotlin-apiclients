package software.tingle.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.junit.Test

class JsonMergePatchTest {

    private var gson = Gson()

    private fun makeJson(o: Any?, builder: GsonBuilder? = null): String {
        if (builder != null) {
            gson = builder.create()
        }
        return gson.toJson(o)
    }

    data class Person(
        val firstName: String? = "John",
        var secondName: String? = "Doe",
        var address: Address? = Address()
    )

    data class Address(
        var street: String? = "123 Main St",
        var city: String? = "Anytown",
        var state: String? = "CA",
        var zip: String? = "12345"
    )

    @Test
    fun testRemovingSecondNameWorks() {
        val expected =
            "{\"firstName\":\"John\",\"secondName\":null,\"address\":{\"street\":\"123 Main St\",\"city\":\"Anytown\",\"state\":\"CA\",\"zip\":\"12345\"}}"
        val actual = makeJson(Person(secondName = null), builder = GsonBuilder().serializeNulls())

        assert(expected == actual)
    }

    @Test
    fun testAddingNameWorks() {
        val person = Person(secondName = null)
        val expected = "{\"firstName\":\"John\",\"secondName\":\"Doe\",\"address\":{\"street\":\"123 Main St\",\"city\":\"Anytown\",\"state\":\"CA\",\"zip\":\"12345\"}}"
        val actual =
            makeJson(person.copy(secondName = "Doe"), builder = GsonBuilder().serializeNulls())

        assert(expected == actual)
    }
}