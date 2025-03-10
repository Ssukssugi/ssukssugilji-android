package com.sabo.feature.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.CreateNickname())
    val uiState = _uiState.asStateFlow()
}