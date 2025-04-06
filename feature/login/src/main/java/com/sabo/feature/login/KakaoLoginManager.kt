package com.sabo.feature.login

import android.content.Context
import com.kakao.sdk.user.UserApiClient

class KakaoLoginManager(
    private val context: Context,
    private val callbackListener: CallbackListener
) {

    interface CallbackListener {
        fun onSuccess(token: String)
    }
    private val kakaoApiClient = UserApiClient.instance

    fun requestToken() {
        if (kakaoApiClient.isKakaoTalkLoginAvailable(context)) {
            kakaoApiClient.loginWithKakaoTalk(context) { token, error ->
                token?.accessToken?.let { callbackListener.onSuccess(it) }
            }
        } else {
            kakaoApiClient.loginWithKakaoAccount(context) { token, error ->
                token?.accessToken?.let { callbackListener.onSuccess(it) }
            }
        }
    }
}