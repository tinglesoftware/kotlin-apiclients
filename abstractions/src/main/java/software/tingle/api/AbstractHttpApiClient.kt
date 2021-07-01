package software.tingle.api

import android.text.TextUtils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import software.tingle.api.authentication.EmptyAuthenticationProvider
import software.tingle.api.authentication.IAuthenticationProvider
import java.io.IOException

/**
 * An abstraction of a HTTP client for accessing APIs built by TINGLE
 */
abstract class AbstractHttpApiClient
/**
 * Creates an instance of @[AbstractHttpApiClient]
 *
 * @param authenticationProvider the provider to use for authentication
 */
constructor(private val authenticationProvider: IAuthenticationProvider?) {

    private val gson = Gson()

    private val backChannel: OkHttpClient by lazy {
        val provider = authenticationProvider ?: EmptyAuthenticationProvider()
        val builder = OkHttpClient.Builder()
                .addInterceptor(provider)

        buildBackChannel(builder)
    }

    /**
     * Builds the back channel @[OkHttpClient] to be used by the client
     *
     * @param builder the prepared builder to extend
     * @return an initialized back channel
     */
    protected open fun buildBackChannel(builder: OkHttpClient.Builder): OkHttpClient {
        return builder.build()
    }

    protected fun makeJson(o: Any?): String {
        return gson.toJson(o)
    }

    /**
     * Executes a request synchronously
     *
     * @param builder        the builder of the request
     * @param classOfTResult the class of the result to be de-serialized
     * @param <TResult>      the type to be de-serialized
     * @return a resource response
     * @throws IOException if the request could not be executed due to cancellation, a connectivity
     * problem or timeout. Because networks can fail during an exchange, it is possible that the
     * remote server accepted the request before the failure.</TResult>
     */
    @Throws(IOException::class)
    protected fun <TResult> execute(
            builder: Request.Builder,
            classOfTResult: Class<TResult>?): ResourceResponse<TResult> {

        val func = object : Function<Int, Headers, TResult, HttpApiResponseProblem, ResourceResponse<TResult>> {
            override fun apply(t1: Int, t2: Headers, t3: TResult?, t4: HttpApiResponseProblem?): ResourceResponse<TResult> {
                return ResourceResponse(t1, t2, t3, t4)
            }

        }
        return execute(builder, classOfTResult, HttpApiResponseProblem::class.java, func)
    }

    /**
     * Executes a request asynchronously
     * 'suspend' suspends execution until the result is returned, while waiting for the result
     * It unblocks the thread (Main or Background) that it’s running on so other functions or coroutines can run
     *
     * @param builder        the builder of the request
     * @param classOfTResult the class of the result to be de-serialized
     * @param <TResult>      the type to be de-serialized
     * @return a resource response
     * @throws IOException if the request could not be executed due to cancellation, a connectivity
     * problem or timeout. Because networks can fail during an exchange, it is possible that the
     * remote server accepted the request before the failure.</TResult>
     */
    @Throws(IOException::class)
    protected suspend fun <TResult> executeAsync(
            builder: Request.Builder,
            classOfTResult: Class<TResult>?): ResourceResponse<TResult> {

        val func = object : Function<Int, Headers, TResult, HttpApiResponseProblem, ResourceResponse<TResult>> {
            override fun apply(t1: Int, t2: Headers, t3: TResult?, t4: HttpApiResponseProblem?): ResourceResponse<TResult> {
                return ResourceResponse(t1, t2, t3, t4)
            }

        }

        return withContext(Dispatchers.IO) {
            execute(builder, classOfTResult, HttpApiResponseProblem::class.java, func)
        }
    }

    /**
     * Executes a request synchronously
     *
     * @param builder        the builder of the request
     * @param classOfTResult the class of the result to be de-serialized
     * @param classOfTError  the class of the error to be de-serialized
     * @param <TResult>      the type of result to be de-serialized
     * @param <TError>       the type or error to be de-seriallized
     * @return a resource response
     * @throws IOException if the request could not be executed due to cancellation, a connectivity
     * problem or timeout. Because networks can fail during an exchange, it is possible that the
     * remote server accepted the request before the failure.
    </TError></TResult> */
    @Throws(IOException::class)
    protected fun <TResult, TError> execute(
            builder: Request.Builder,
            classOfTResult: Class<TResult>?,
            classOfTError: Class<TError>): CustomResourceResponse<TResult, TError> {

        val func = object : Function<Int, Headers, TResult, TError, CustomResourceResponse<TResult, TError>> {
            override fun apply(t1: Int, t2: Headers, t3: TResult?, t4: TError?): CustomResourceResponse<TResult, TError> {
                return CustomResourceResponse(t1, t2, t3, t4)
            }

        }
        return execute(builder, classOfTResult, classOfTError, func)
    }


    /**
     * Executes a request asynchronously
     * 'suspend' suspends execution until the result is returned, while waiting for the result
     * It unblocks the thread (Main or Background) that it’s running on so other functions or coroutines can run
     *
     * @param builder        the builder of the request
     * @param classOfTResult the class of the result to be de-serialized
     * @param classOfTError  the class of the error to be de-serialized
     * @param <TResult>      the type of result to be de-serialized
     * @param <TError>       the type or error to be de-seriallized
     * @return a resource response
     * @throws IOException if the request could not be executed due to cancellation, a connectivity
     * problem or timeout. Because networks can fail during an exchange, it is possible that the
     * remote server accepted the request before the failure.
    </TError></TResult> */
    @Throws(IOException::class)
    protected suspend fun <TResult, TError> executeAsync(
            builder: Request.Builder,
            classOfTResult: Class<TResult>?,
            classOfTError: Class<TError>): CustomResourceResponse<TResult, TError> {

        val func = object : Function<Int, Headers, TResult, TError, CustomResourceResponse<TResult, TError>> {
            override fun apply(t1: Int, t2: Headers, t3: TResult?, t4: TError?): CustomResourceResponse<TResult, TError> {
                return CustomResourceResponse(t1, t2, t3, t4)
            }

        }

        return withContext(Dispatchers.IO) {
            execute(builder, classOfTResult, classOfTError, func)
        }
    }

    /**
     * Executes a request synchronously
     *
     * @param builder               the builder of the request
     * @param classOfTResult        the class of the result to be de-serialized
     * @param classOfTError         the class of the error to be de-serialized
     * @param resultBuilderFunction the function to use in building the result
     * @param <TResource>           the type of result to be de-serialized
     * @param <TError>              the type or error to be de-serialized
     * @param <TResourceResponse>   the type or resource-response to return
     * @return a resource response
     * @throws IOException if the request could not be executed due to cancellation, a connectivity
     * problem or timeout. Because networks can fail during an exchange, it is possible that the
     * remote server accepted the request before the failure.
    </TResourceResponse></TError></TResource> */
    @Throws(IOException::class)
    protected fun <TResource, TError, TResourceResponse : CustomResourceResponse<TResource, TError>> execute(
            builder: Request.Builder,
            classOfTResult: Class<TResource>?,
            classOfTError: Class<TError>,
            resultBuilderFunction: Function<Int, Headers, TResource, TError, TResourceResponse>): TResourceResponse {

        val request = builder.build()
        val response = backChannel.newCall(request).execute()

        var errorModel: TError? = null
        var resource: TResource? = null

        val body = response.body
        val rc = response.code
        if (body != null) {
            when (rc) {
                200,
                201,
                204 -> {
                    val contentType = body.contentType()
                    val subType = contentType?.subtype
                    val hasJsonSubType = !TextUtils.isEmpty(subType) && subType!!.contains("json", true)
                    if (hasJsonSubType && classOfTResult != null) {
                        resource = gson.fromJson(body.charStream(), classOfTResult)
                    }
                }
                400 -> errorModel = gson.fromJson(body.charStream(), classOfTError)
            }

            // close the body stream
            body.close()
        }

        return resultBuilderFunction.apply(rc, response.headers, resource, errorModel)
    }

    @FunctionalInterface
    protected interface Function<T1, T2, T3, T4, R> {
        fun apply(t1: T1, t2: T2, t3: T3?, t4: T4?): R
    }

    companion object {
        /**
         * The @[MediaType] for JavaScript Object Notation (JSON) with UTF-8 encoding
         */
        val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaType()
        val MEDIA_TYPE_TEXT_JSON = "text/json".toMediaType()
        val MEDIA_TYPE_PATH_JSON = "application/json-path+json".toMediaType()
        val MEDIA_TYPE_PLUS_JSON = "application/*+json".toMediaType()
    }
}
