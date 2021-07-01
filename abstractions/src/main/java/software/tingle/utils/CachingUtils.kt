package software.tingle.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.Log
import androidx.preference.PreferenceManager

class CachingUtils {
    companion object {

        private val TAG = CachingUtils::class.java.simpleName
        private const val PREF_ACCESS_TOKEN = "access_token_"
        private const val PREF_ACCESS_TOKEN_EXPIRY = "access_token_expiry_"

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }

        private fun createPreferencesEditor(context: Context): SharedPreferences.Editor {
            return getSharedPreferences(context).edit()
        }


        fun getAccessToken(context: Context): String? {
            val sp = getSharedPreferences(context)
            return sp.getString(PREF_ACCESS_TOKEN, null)
        }

        fun setAccessToken(context: Context, accessToken: String): Boolean {
            Log.d(TAG,
                    "Access token of length ${if (TextUtils.isEmpty(accessToken)) 0 else accessToken.length}"
            )
            return createPreferencesEditor(context).putString(
                    PREF_ACCESS_TOKEN,
                    accessToken
            ).commit()
        }

        fun setAccessTokenExpiry(context: Context, accessTokenExpiry: Int): Boolean {
            val expiresIn_ms = accessTokenExpiry * DateUtils.SECOND_IN_MILLIS
            val expiresAt = System.currentTimeMillis() + expiresIn_ms - 10 * DateUtils.SECOND_IN_MILLIS
            Log.v(TAG, "Access Token Expiry: $expiresAt")

            return createPreferencesEditor(context)
                    .putLong(PREF_ACCESS_TOKEN_EXPIRY, expiresAt)
                    .commit()
        }

        fun getAccessTokenExpiry(context: Context): Long {
            val sp = getSharedPreferences(context)
            return sp.getLong(PREF_ACCESS_TOKEN_EXPIRY, 0)
        }

        fun hasAccessTokenExpired(context: Context): Boolean {
            val now = System.currentTimeMillis()
            val expires = getAccessTokenExpiry(context)
            Log.d(TAG, "Expiry compared between now=$now expires=$expires")
            return now >= expires
        }
    }
}