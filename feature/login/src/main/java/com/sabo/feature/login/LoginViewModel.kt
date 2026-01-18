package com.sabo.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabo.core.data.handle
import com.sabo.core.data.repository.LoginRepository
import com.sabo.core.model.LoginType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
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

    private val _loginEvent = Channel<LoginEvent>(capacity = 1)
    val loginEvent = _loginEvent.receiveAsFlow()

    private var socialId: String? = null
    private var emailAddress: String? = null
    private var loginType: LoginType? = null

    init {
        checkInitialAuthentication()
    }

    private fun checkInitialAuthentication() {
        viewModelScope.launch {
            if (loginRepository.checkUserAuthentication()) {
                if (loginRepository.isUserProfileComplete()) {
                    _loginEvent.send(LoginEvent.GoToMain)
                } else {
                    _loginEvent.send(LoginEvent.GoToSignUp)
                }
            } else {
                val state = uiState.value as? LoginUiState.BeforeLogin ?: return@launch
                _uiState.value = state.copy(isInitializing = false)
            }
        }
    }

    fun onSuccessKakaoLogin(token: String) {
        val state = uiState.value as? LoginUiState.BeforeLogin ?: return
        viewModelScope.launch {
            loginRepository.requestKakaoLogin(token).handle(
                onSuccess = {
                    socialId = it.socialId
                    emailAddress = it.emailAddress
                    loginType = LoginType.KAKAO
                    if (it.isRegistered) {
                        _uiState.value = LoginUiState.SuccessLogin(type = LoginType.KAKAO, isRegisteredUser = it.existInfo)
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
                    socialId = it.socialId
                    emailAddress = it.emailAddress
                    loginType = LoginType.NAVER
                    if (it.isRegistered) {
                        _uiState.value = LoginUiState.SuccessLogin(type = LoginType.NAVER, isRegisteredUser = it.existInfo)
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
            loginRepository.requestGoogleLogin(token).handle(
                onSuccess = {
                    socialId = it.socialId
                    emailAddress = it.emailAddress
                    loginType = LoginType.GOOGLE
                    if (it.isRegistered) {
                        _uiState.value = LoginUiState.SuccessLogin(type = LoginType.GOOGLE, isRegisteredUser = it.existInfo)
                    } else {
                        _uiState.value = state.copy(isShownTermsAgree = true)
                    }
                }
            )
        }
    }

    fun applyTermsAgreement() {
        val state = uiState.value as? LoginUiState.BeforeLogin ?: return
        if (state.termsState.isRequiredAgree().not()) return
        val socialId = this.socialId ?: return
        val emailAddress = this.emailAddress ?: return
        val loginType = this.loginType ?: return
        viewModelScope.launch {
            _uiState.value = state.copy(isShownTermsAgree = false)
            _uiState.value = LoginUiState.SignUpLoading()
            loginRepository.applyTermsAgreement(
                socialId = socialId,
                emailAddress = emailAddress,
                type = loginType,
                isMarketingAgree = state.termsState.marketingTerms
            ).handle(
                onSuccess = {
                    val successState = LoginUiState.SuccessLogin(
                        type = loginType,
                        isMarketingReceiveAccepted = state.termsState.marketingTerms,
                        isRegisteredUser = false
                    )
                    val currentState = _uiState.value as? LoginUiState.SignUpLoading
                    if (currentState != null) {
                        _uiState.value = currentState.copy(
                            isServerResponseReady = true,
                            serverResponse = successState
                        )
                    } else {
                        _uiState.value = successState
                    }
                },
                onError = {
                }
            )
        }
    }

    fun onAnimationCompleted() {
        val state = uiState.value as? LoginUiState.SignUpLoading ?: return
        if (state.isServerResponseReady && state.serverResponse != null) {
            _uiState.value = state.serverResponse
        }
    }

    fun changeAgreeTermState(newState: TermsAgreeState) {
        val state = uiState.value as? LoginUiState.BeforeLogin ?: return
        _uiState.value = state.copy(termsState = newState)
    }
}