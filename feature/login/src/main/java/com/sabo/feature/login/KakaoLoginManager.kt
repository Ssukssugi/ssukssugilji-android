package com.sabo.feature.login

import android.content.Context
import com.kakao.sdk.user.UserApiClient

class KakaoLoginManager(
    override val context: Context,
    override val callbackListener: CallbackListener
): LoginManager(context, callbackListener) {

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