package software.tingle.api

import okhttp3.Headers

/**
 * Abstraction of a HTTP response to an API
 * @param <TResource> the type of resource
</TResource> */
open class ResourceResponse<TResource>
/**
 * Creates an instance of @[CustomResourceResponse]
 * @param statusCode the status code of the response
 * @param headers    the headers of the response
 * @param tResource  the de-serialized resource
 * @param error the error de-serialized from the response
 */
(statusCode: Int, headers: Headers, tResource: TResource?, error: HttpApiResponseProblem?)
    : CustomResourceResponse<TResource, HttpApiResponseProblem>(statusCode, headers, tResource, error)
