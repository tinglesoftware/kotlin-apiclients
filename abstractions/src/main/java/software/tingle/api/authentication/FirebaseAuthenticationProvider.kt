package software.tingle.api.authentication

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Request
import java.util.*

open class FirebaseAuthenticationProvider : AuthenticationHeaderProvider() {

    /**
     * Authorize a request before executing
     * @param request the request message to be authorized
     * @return the parameter
     */
    override fun getParameter(request: Request.Builder): String {

        var accessToken: String
        runBlocking(Dispatchers.IO) {
            accessToken = acquireAccessToken()
        }
        return accessToken
    }


    private suspend fun acquireAccessToken(): String {
        var backoff = (BACKOFF_MILLI_SECONDS + sRandom.nextInt(1000)).toLong()
        val user = FirebaseAuth.getInstance().currentUser ?: return ""
        val tokenTask = user.getIdToken(false)

        for (i in 1..MAX_ATTEMPTS) {
            Log.d(TAG, "Attempt #$i to acquire Firebase Auth Token")

            try {
                tokenTask.await() // make the the token request and wait for the results

                if (tokenTask.isSuccessful) {
                    return tokenTask.result?.token
                            ?: "" // We are done here, the result was successful
                }

                Log.d(TAG, "Failed to acquire auth code on attempt #$i ")

                if (i == MAX_ATTEMPTS) {
                    break
                }

                try {
                    Log.v(TAG, "Sleeping for $backoff ms before retry")
                    delay(backoff)
                } catch (e1: InterruptedException) {
                    // finished before we complete - exit.
                    Log.d(TAG, "Thread interrupted: abort remaining retries!")
                    Thread.currentThread().interrupt()
                }

            } catch (e: Exception) {
                println(e)
            }

            // increase backoff exponentially
            backoff *= 2
        }
        return ""
    }

    companion object {
        private val TAG = FirebaseAuthenticationProvider::class.simpleName
        private const val MAX_ATTEMPTS = 3
        private const val BACKOFF_MILLI_SECONDS = 2000
        private val sRandom = Random()
    }
}