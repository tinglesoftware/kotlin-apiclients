package software.tingle.api

import okhttp3.Headers

/**
 * Abstraction of a HTTP response to an API
 * @param <TResource> the type of resource to deserialize on successful requests
 * @param <TError> the type or error to deserialize on failed requests
</TError></TResource> */
open class CustomResourceResponse<TResource, TError>
/**
 * Creates an instance of @[CustomResourceResponse]
 * @param statusCode the status code of the response
 * @param headers the headers of the response
 * @param resource the de-serialized resource
 * @param error the error de-serialized from the response
 */
(val statusCode: Int, val headers: Headers, val resource: TResource?, val error: TError?) {

    open fun isUnauthorized(): Boolean = statusCode == 401

    open fun successful(): Boolean = statusCode in 200..299 && error == null
}
