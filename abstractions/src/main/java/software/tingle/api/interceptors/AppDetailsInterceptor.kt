package software.tingle.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * An @[Interceptor] that adds headers for package id, version name and version code to a request before sending
 */

class AppDetailsInterceptor(
        private val packageId: String,
        private val versionName: String,
        private val versionCode: Int) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain
                .request()
                .newBuilder()
                .header("X-App-Package-Id", packageId)
                .header("X-App-Version-Name", versionName)
                .header("X-App-Version-Code", "$versionCode")
                .build()

        return chain.proceed(request)
    }
}
