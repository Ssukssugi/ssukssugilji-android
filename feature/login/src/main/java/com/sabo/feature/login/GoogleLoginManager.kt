package com.sabo.feature.login

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoogleLoginManager(
    override val context: Context,
    override val callbackListener: CallbackListener
) : LoginManager(context, callbackListener) {

    suspend fun requestToken(): Unit = withContext(Dispatchers.IO) {
        val credentialManager = CredentialManager.create(context)
        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_CLIENT_ID)
                .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        return@withContext try {
            val result = credentialManager.getCredential(context, request)
            handleSignIn(result)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleSignIn(response: GetCredentialResponse) {
        return when (val credential = response.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        callbackListener.onSuccess(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        e.printStackTrace()
                    }
                } else {
                    return
                }
            }
            else -> return
        }
    }
}