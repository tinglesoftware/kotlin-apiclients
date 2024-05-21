package software.tingle.api.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.internal.bind.util.ISO8601Utils
import java.lang.reflect.Type
import java.text.ParseException
import java.text.ParsePosition
import java.util.Date

/**
 * A @[JsonDeserializer] that (de)serializes a @[Date] in ISO8601.
 * Internally it utilizes @[ISO8601Utils] which is ported from Jackson databind
 */

class ISO8601DateAdapter : JsonDeserializer<Date>, JsonSerializer<Date> {

    @Synchronized
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type,
        jsonDeserializationContext: JsonDeserializationContext
    ): Date? {
        return try {
            ISO8601Utils.parse(jsonElement.asString, ParsePosition(0))
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }

    }

    override fun serialize(
        date: Date,
        type: Type,
        jsonSerializationContext: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(ISO8601Utils.format(date))
    }
}