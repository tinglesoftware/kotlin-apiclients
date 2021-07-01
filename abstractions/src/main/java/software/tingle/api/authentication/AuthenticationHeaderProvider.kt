package software.tingle.api.authentication

import okhttp3.Interceptor
import java.io.IOException

import okhttp3.Request
import okhttp3.Response

/**
 * Implementation of @[IAuthenticationProvider] which sets the Authorization header using the scheme and parameter separated by a space.
 * The parameter set is gotten from the abstract method @[.getParameter].
 */

abstract class AuthenticationHeaderProvider
/**
 * Creates an instance of @[AuthenticationHeaderProvider]
 * @param scheme the scheme to be used in the Authorization header as the prefix for the header value
 */
@JvmOverloads constructor(private val scheme: String = DEFAULT_SCHEME) : IAuthenticationProvider {

    /**
     * Set authorization data to a request before executing
     * @param chain the request execution chain
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain
                .request()
                .newBuilder()

        val parameter = getParameter(builder)
        val request = builder
                .header("Authorization", "$scheme $parameter")
                .build()

        return chain.proceed(request)
    }

    /**
     * Gets the authentication parameter value.
     * @param request the request message to be authorized
     * @return the parameter
     */
    protected abstract fun getParameter(request: Request.Builder): String

    companion object {
        const val DEFAULT_SCHEME = "Bearer"
    }
}