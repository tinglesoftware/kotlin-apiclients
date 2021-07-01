package software.tingle.api.authentication

import android.text.TextUtils
import android.util.Base64
import okhttp3.Request
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Implementation of @[IAuthenticationProvider] for SharedKey authentication scheme.
 * This is mainly used for Tingle APIs/Services but can be modified to be used with Microsoft APIs.
 * The implementation generates a token based on the HTTP method, path, time, content length and
 * content type, then hashing using the pre-shared key (PSK). The hashing algorithm is HMACSHA256.
 */
open class SharedKeyAuthenticationProvider
/**
 * Creates an instance of @[SharedKeyAuthenticationProvider]
 * @param scheme the scheme to be used in the Authorization header as the prefix for the header value
 * @param dateHeaderName the header name for the date header
 * @param keyBytes the bytes for the key used for signing
 */
(scheme: String, private val dateHeaderName: String, private val keyBytes: ByteArray) : AuthenticationHeaderProvider(scheme) {

    /**
     * Creates an instance of @[SharedKeyAuthenticationProvider]
     * @param base64Key the key for signing encoded in base 64
     */
    constructor(base64Key: String) : this(DATE_HEADER_NAME, base64Key)

    /**
     * Creates an instance of @[SharedKeyAuthenticationProvider]
     * @param keyBytes the bytes for the key used for signing
     */
    constructor(keyBytes: ByteArray) : this(DATE_HEADER_NAME, keyBytes)

    /**
     * Creates an instance of @[SharedKeyAuthenticationProvider]
     * @param dateHeaderName the header name for the date header
     * @param base64Key the key for signing encoded in base 64
     */
    constructor(dateHeaderName: String, base64Key: String) : this(DEFAULT_SCHEME, dateHeaderName, base64Key)

    /**
     * Creates an instance of @[SharedKeyAuthenticationProvider]
     * @param dateHeaderName the header name for the date header
     * @param keyBytes the bytes for the key used for signing
     */
    constructor(dateHeaderName: String, keyBytes: ByteArray) : this(DEFAULT_SCHEME, dateHeaderName, keyBytes)

    /**
     * Creates an instance of @[SharedKeyAuthenticationProvider]
     * @param scheme the scheme to be used in the Authorization header as the prefix for the header value
     * @param dateHeaderName the header name for the date header
     * @param base64Key the key for signing encoded in base 64
     */
    constructor(scheme: String, dateHeaderName: String, base64Key: String) : this(scheme, dateHeaderName, Base64.decode(base64Key, Base64.NO_WRAP))

    /**
     * Authorize a request before executing
     * @param request the request message to be authorized
     * @return the parameter
     */
    override fun getParameter(request: Request.Builder): String {
        val temp = request.build()

        val method = temp.method
        val path = temp.url.toUrl().toURI().path
        var contentType = ""
        var contentLength: Long = 0

        val body = temp.body
        if (body != null) {
            try {
                contentLength = body.contentLength()
                val mediaType = body.contentType()
                if (mediaType != null) {
                    contentType = mediaType.toString()
                }
            } catch (e: IOException) {
                // TODO: 07/03/2018 log this exception
            }

        }

        var rfcDate = temp.header(dateHeaderName)
        if (TextUtils.isEmpty(rfcDate)) {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat(RFC_DATE_FORMAT, Locale.US)
            dateFormat.timeZone = TimeZone.getTimeZone(RFC_DATE_TIMEZONE)
            rfcDate = dateFormat.format(calendar.time)
            request.header(dateHeaderName, rfcDate!!)
        }

        return sign(method, contentLength, contentType, rfcDate!!, path)!!
    }

    /**
     * Generates the SHA256 signature hash
     * @param method the method for the request
     * @param contentLength the length of the content
     * @param contentType the type of the content
     * @param date the date value in the header
     * @param resource the resource to be included
     * @return the signed result
     */
    protected open fun sign(method: String, contentLength: Long, contentType: String, date: String, resource: String): String? {
        try {
            // generate secret key
            val secretKey = SecretKeySpec(keyBytes, 0, keyBytes.size, ALGORITHM)
            val mac = Mac.getInstance(ALGORITHM)
            mac.init(secretKey)

            // generate bytes to hash
            val parts = arrayOf(method, contentLength, contentType, "$dateHeaderName:$date", resource)
            val stringToHash = TextUtils.join("\n", parts)
            val bytesToHash = stringToHash.toByteArray(StandardCharsets.US_ASCII)

            // hash the bytes
            val calculatedHash = mac.doFinal(bytesToHash)

            // encode to base 64
            return Base64.encodeToString(calculatedHash, Base64.NO_WRAP)
        } catch (ex: Exception) {
            // TODO: 07/03/2018 log this exception
        }

        return null
    }

    companion object {
        private const val ALGORITHM = "HmacSHA256"
        private const val RFC_DATE_TIMEZONE = "GMT"
        const val RFC_DATE_FORMAT = "EEE, dd MMM yyyy HH':'mm':'ss 'GMT'"
        const val DEFAULT_SCHEME = "SharedKey"
        const val DATE_HEADER_NAME = "x-ms-date"
    }
}
