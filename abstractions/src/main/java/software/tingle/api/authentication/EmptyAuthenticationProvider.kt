package software.tingle.api.authentication

import okhttp3.Interceptor
import java.io.IOException

import okhttp3.Response

/**
 * The default implementation for @[IAuthenticationProvider] which does not modify the request message at all.
 * Use this to make requests to services that do not need authentication or to test authentication.
 * This can also be used to find out the supported authentication methods as is presented in the 'WWW-Authentication' header
 * of a response message @[software.tingle.api.ResourceResponse]
 */

class EmptyAuthenticationProvider : IAuthenticationProvider {

    /**
     * Set authorization data to a request before executing
     * @param chain the request execution chain
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}
