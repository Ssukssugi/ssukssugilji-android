package com.sabo.feature.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.sabo.feature.login.LoginUiState.SuccessLogin.LoginType

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.BeforeLogin)
    val uiState = _uiState.asStateFlow()
    
    private val kakaoApiClient = UserApiClient.instance

    fun loginWithKakao() {
        onRedirectLogin()
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

    fun onRedirectLogin() {
        _uiState.value = LoginUiState.RedirectLoading
    }

    fun onSuccessNaverLogin(token: String) {
        _uiState.value = LoginUiState.SuccessLogin(type = LoginType.NAVER)
        viewModelScope.launch {

        }
    }
}