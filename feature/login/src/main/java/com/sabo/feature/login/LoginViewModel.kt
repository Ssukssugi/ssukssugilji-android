package com.sabo.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabo.core.domain.handle
import com.sabo.core.domain.model.LoginType
import com.sabo.core.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.BeforeLogin())
    val uiState = _uiState.asStateFlow()

    private val _loginEvent = Channel<LoginEvent>(Channel.RENDEZVOUS)
    val loginEvent = _loginEvent.receiveAsFlow()

    private var socialAccessToken: String? = null
    private var loginType: LoginType? = null

    fun onSuccessKakaoLogin(token: String) {
        val state = uiState.value as? LoginUiState.BeforeLogin ?: return
        viewModelScope.launch {
            loginRepository.requestKakaoLogin(token).handle(
                onSuccess = {
                    socialAccessToken = token
                    loginType = LoginType.KAKAO
                    if (it.isRegistered) {
                        navigateAfterLogin(
                            loginType = LoginType.KAKAO,
                            isAlreadyMember = it.existInfo
                        )
                    } else {
                        _uiState.value = state.copy(isShownTermsAgree = true)
                    }
                },
                onError = {
                },
                onFinish = {

                }
            )
        }
    }

    fun onSuccessNaverLogin(token: String) {
        val state = uiState.value as? LoginUiState.BeforeLogin ?: return
        viewModelScope.launch {
            loginRepository.requestNaverLogin(token).handle(
                onSuccess = {
                    socialAccessToken = token
                    loginType = LoginType.NAVER
                    if (it.isRegistered) {
                        navigateAfterLogin(
                            loginType = LoginType.NAVER,
                            isAlreadyMember = it.existInfo
                        )
                    } else {
                        _uiState.value = state.copy(isShownTermsAgree = true)
                    }
                },
                onError = {
                },
                onFinish = {
                }
            )
        }
    }

    fun onSuccessGoogleLogin(token: String) {
        val state = uiState.value as? LoginUiState.BeforeLogin ?: return
        viewModelScope.launch {
            loginRepository.requestGoogleLogin(token = token).handle(
                onSuccess = {
                    socialAccessToken = token
                    loginType = LoginType.GOOGLE
                    if (it.isRegistered) {
                        navigateAfterLogin(
                            loginType = LoginType.GOOGLE,
                            isAlreadyMember = it.existInfo
                        )
                    } else {
                        _uiState.value = state.copy(isShownTermsAgree = true)
                    }
                }
            )
        }
    }

    fun applyTermsAgreement() {
        val state = uiState.value as? LoginUiState.BeforeLogin ?: return
        val accessToken = socialAccessToken ?: return
        val loginType = this.loginType ?: return
        viewModelScope.launch {
            _uiState.value = state.copy(isShownTermsAgree = false)
            _uiState.value = LoginUiState.SignUpLoading
            loginRepository.applyTermsAgreement(
                accessToken,
                loginType,
                state.termsState.marketingTerms
            ).handle(
                onSuccess = {
                    _uiState.value = LoginUiState.SuccessLogin(type = loginType)
                    launch {
                        _loginEvent.send(LoginEvent.GoToSignUp)
                    }
                },
                onError = {
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