package software.tingle

import android.Manifest
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import software.tingle.api.authentication.OAuthClientCredentialProvider
import software.tingle.api.authentication.OAuthRequest


@RunWith(AndroidJUnit4::class)
class OAuthCredentialProviderTest {
    @get:Rule
    var permissionRule = GrantPermissionRule.grant(Manifest.permission.INTERNET)

    @Test
    fun authenticationWorks() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val provider = OAuthClientCredentialProvider(context = appContext, authRequest = OAuthRequest(
                "",
                "",
                "",
                ""
        ))
        // val response = provider.accessToken

        //assertNotSame(response, "")
    }
}
