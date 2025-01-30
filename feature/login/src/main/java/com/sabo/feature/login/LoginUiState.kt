package com.sabo.feature.login

sealed interface LoginUiState {
    data object BeforeLogin : LoginUiState
    data object RedirectLoading : LoginUiState
    data class SuccessLogin(
        val type: LoginType,
        val isMarketingReceiveAccepted: Boolean = false
    ) : LoginUiState {
        enum class LoginType(val text: String) {
            KAKAO("카카오"), NAVER("네이버")
        }
    }
}