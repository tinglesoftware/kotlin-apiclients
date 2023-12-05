package software.tingle.api.authentication

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import software.tingle.utils.CachingUtils
import java.util.*

/**
 * Authentication provider for OAuth Client Credentials flow (see the OAuth 2.0 spec for more details).
 * This provider supports caching
 */

class OAuthClientCredentialProvider

/**
 * Creates an instance of @[OAuthClientCredentialProvider]
 * @param scheme the scheme to be used in the Authorization header as the prefix for the header value
 * @param authRequest the request model to be used to request a token. Contains parameters like clientId, clientSecret e.t.c
 */
    (
    private val context: Context,
    scheme: String = DEFAULT_SCHEME,
    private val authRequest: OAuthRequest
) : AuthenticationHeaderProvider(scheme) {

    /**
     * Creates an instance of @[OAuthClientCredentialProvider]
     * @param scheme the scheme to be used in the Authorization header as the prefix for the header value
     * @param authenticationEndpoint the authentication endpoint to be used to request a token
     * @param clientId the client identifier (client_id) to be used in the token request
     * @param clientSecret the client secret (client_secret) to be used in the token request
     * @param resource the resource to be requested for in the token request
     */
    constructor(
        context: Context,
        scheme: String = DEFAULT_SCHEME,
        authenticationEndpoint: String? = null,
        clientId: String? = null,
        clientSecret: String? = null,
        resource: String? = null,
    ) : this(
        context,
        scheme,
        OAuthRequest(authenticationEndpoint, clientId, clientSecret, resource)
    )


    private val backChannel: OkHttpClient = OkHttpClient
        .Builder()
        .build()

    /**
     * Authorize a request before executing
     * @param request the request message to be authorized
     * @return the authentication parameter
     */
    override fun getParameter(request: Request.Builder): String {
        return accessToken
    }

    /**
     * Requests an OAuth token
     * @return the oAuth response
     */
    protected val requestOAuthToken: OAuthResponse?
        get() {
            // prepare the request body
            val requestBody = FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", authRequest.clientId!!)
                .add("client_secret", authRequest.clientSecret!!)
                .add("resource", authRequest.resource!!)
                .build()

            // prepare the request
            val request = Request.Builder()
                .url(authRequest.authenticationEndpoint!!)
                .post(requestBody)
                .build()

            try {
                val response = backChannel.newCall(request).execute()

                val body = response.body
                if (response.isSuccessful && body != null) {
                    val oAuthResponse =
                        Gson().fromJson(body.charStream(), OAuthResponse::class.java)
                    body.close()
                    return oAuthResponse
                }
            } catch (e: Exception) {
                Log.e(TAG, "OAuth Token Request failed", e)
            }

            return null
        }

    private val accessToken: String
        get() {
            var backoff = (BACKOFF_MILLI_SECONDS + sRandom.nextInt(1000)).toLong()

            for (i in 1..MAX_ATTEMPTS) {
                Log.d(TAG, "Attempt #$i to acquire Auth Token")

                // Check if we have an existing token and the token's validity.
                val token = CachingUtils.getAccessToken(context)
                if (!TextUtils.isEmpty(token) && !CachingUtils.hasAccessTokenExpired(context)) {
                    Log.d(
                        TAG,
                        "Valid token in cache, expiry time: ${
                            Date(
                                CachingUtils.getAccessTokenExpiry(context)
                            )
                        } "
                    )
                    return token!!
                }


                // let's request for a new token
                Log.i(TAG, "Making token acquisition request, number of attempts = $i")
                val oAuthResponse = runBlocking(Dispatchers.IO) { requestOAuthToken }

                if (oAuthResponse != null) {
                    Log.d(
                        TAG,
                        "Valid token was acquired, expiry time: ${Date(oAuthResponse.expiresOn)} "
                    )

                    CachingUtils.setAccessToken(context, oAuthResponse.accessToken!!)
                    CachingUtils.setAccessTokenExpiry(context, oAuthResponse.expiresIn)

                    return oAuthResponse.accessToken!!
                }

                Log.d(TAG, "Failed to acquire auth code on attempt #$i ")

                if (i == MAX_ATTEMPTS) {
                    break
                }

                try {
                    Log.v(TAG, "Sleeping for $backoff ms before retry")
                    Thread.sleep(backoff)
                } catch (e1: InterruptedException) {
                    // finished before we complete - exit.
                    Log.d(TAG, "Thread interrupted: abort remaining retries!")
                    Thread.currentThread().interrupt()
                }

                // increase backoff exponentially
                backoff *= 2
            }
            return ""
        }

    companion object {
        private val TAG = OAuthClientCredentialProvider::class.simpleName
        private const val MAX_ATTEMPTS = 3
        private const val BACKOFF_MILLI_SECONDS = 2000
        private val sRandom = Random()
    }

}

class OAuthResponse {
    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("expires_on")
    var expiresOn: Long = 3600

    @SerializedName("expires_in")
    var expiresIn: Int = 0
}

data class OAuthRequest(
    //  the authentication endpoint to be used to request a token
    var authenticationEndpoint: String? = null,
    // the client identifier (client_id) to be used in the token request
    var clientId: String? = null,
    // the client secret (client_secret) to be used in the token request
    var clientSecret: String? = null,
    // the resource to be requested for in the token request
    var resource: String? = null
)