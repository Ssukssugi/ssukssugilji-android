package com.sabo.feature.login

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.sabo.core.model.LoginType

@Stable
sealed interface LoginUiState {
    @Immutable
    data class BeforeLogin(
        val isShownTermsAgree: Boolean = false,
        val termsState: TermsAgreeState = TermsAgreeState()
    ) : LoginUiState

    data object SignUpLoading : LoginUiState

    @Immutable
    data class SuccessLogin(
        val type: LoginType,
        val isMarketingReceiveAccepted: Boolean = false
    ) : LoginUiState
}

sealed interface LoginEvent {
    data object GoToMain : LoginEvent
    data object GoToSignUp : LoginEvent
}

@Immutable
data class TermsAgreeState(
    val serviceTerms: Boolean = false,
    val privateTerms: Boolean = false,
    val ageTerms: Boolean = false,
    val marketingTerms: Boolean = false
) {
    fun isAllAgree() = serviceTerms && privateTerms && ageTerms && marketingTerms
    fun isRequiredAgree() = serviceTerms && privateTerms && ageTerms
}