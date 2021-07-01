package software.tingle.api.authentication

import okhttp3.Interceptor

/**
 * Contract for implementing authorization providers
 */

interface IAuthenticationProvider : Interceptor
