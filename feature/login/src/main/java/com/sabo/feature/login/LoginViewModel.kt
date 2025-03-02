package com.sabo.feature.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.sabo.core.domain.handle
import com.sabo.core.domain.repository.LoginRepository
import com.sabo.feature.login.LoginUiState.SuccessLogin.LoginType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.BeforeLogin)
    val uiState = _uiState.asStateFlow()

    private val _loginEvent = Channel<LoginEvent>(Channel.RENDEZVOUS)
    val loginEvent = _loginEvent.receiveAsFlow()

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
        _uiState.value = LoginUiState.RedirectLoading
        viewModelScope.launch {
            loginRepository.requestNaverLogin(token).handle(
                onSuccess = {

                },
                onError = {
                },
                onFinish = {
                    //FIXME : Move to on Success
                    navigateAfterLogin(loginType = LoginType.NAVER)
                }
            )
        }
    }

    private fun CoroutineScope.navigateAfterLogin(
        loginType: LoginType,
        delayTime: Long = 200L,
        isAlreadyMember: Boolean = false
    ) {
        launch {
            _uiState.value = LoginUiState.SuccessLogin(type = loginType)
            delay(delayTime)
            _loginEvent.send(if (isAlreadyMember) LoginEvent.GoToMain else LoginEvent.GoToSignUp)
        }
    }
}