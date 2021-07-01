package software.tingle.api;

import com.google.gson.annotations.SerializedName

/**
 * A machine-readable format for specifying errors in HTTP API responses based on
 * https://tools.ietf.org/html/rfc7807.
 * This is normally used when the response status code does not indicate success such as 400 etc
 */
open class HttpApiResponseProblem {
    /**
     * A URI reference [RFC3986] that identifies the problem type. This specification encourages
     * that, when dereferenced, it provide human-readable documentation for the problem type
     * (e.g., using HTML [W3C.REC-html5-20141028]).  When this member is not present, its value
     * is assumed to be "about:blank".
     */
    @SerializedName("type")
    open var type: String? = null

    /**
     * A short, human-readable summary of the problem type.It SHOULD NOT change from occurrence
     * to occurrence of the problem, except for purposes of localization(e.g., using proactive
     * content negotiation; see[RFC7231], Section 3.4).
     */
    @SerializedName("title")
    open var title: String? = null

    /**
     * A human-readable explanation specific to this occurrence of the problem.
     */
    @SerializedName("detail")
    open var detail: String? = null

    /**
     * A URI reference that identifies the specific occurrence of the problem.It may or may
     * not yield further information if dereferenced.
     */
    @SerializedName("instance")
    open var instance: String? = null

    /**
     * Gets the validation errors associated with this instance of ProblemDetails.
     */
    @SerializedName("errors")
    open var errors: HashMap<String, Array<String>>? = null


    /**
     * The standard code for the error
     */
    @SerializedName("error_code")
    open var legacyCode: String? = null

    /**
     * The detailed description for the error.
     * Where provided, it can be used as a display message to the user in an interactive environment.
     */
    @SerializedName("error_description")
    open var legacyDescription: String? = null

    /**
     * Gets the value for error_code if @[title] is not set
     */
    open val code: String? get() = title ?: legacyCode

    /**
     * Gets the value for error_code if @[detail] is not set
     */
    open val description: String? get() = detail ?: legacyDescription ?: errorLabels

    /**
     * Gets the value for error_code if @[errors] is not set
     */
    private val errorLabels: String
        get() {
            val labels = errors?.values ?: emptyList()
            return if (labels.isEmpty()) {
                ""
            } else {
                labels.first().first()
            }
        }

}
