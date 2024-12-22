package com.sabo.feature.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val kakaoApiClient = UserApiClient.instance

    fun loginWithKakao() {
        viewModelScope.launch {
            if (kakaoApiClient.isKakaoTalkLoginAvailable(context)) {
                kakaoApiClient.loginWithKakaoTalk(context) { token, error ->

                }
            } else {
                kakaoApiClient.loginWithKakaoAccount(context) { token, error ->  

                }
            }
        }
    }

    fun onSuccessNaverLogin() {

    }
}