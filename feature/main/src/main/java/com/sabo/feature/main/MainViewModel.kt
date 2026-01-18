package com.sabo.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabo.core.model.NetworkErrorEvent
import com.sabo.core.navigator.NavigationEventHandler
import com.sabo.core.navigator.di.AuthNavigation
import com.sabo.core.toolkit.NetworkConnectivityObserver
import com.sabo.core.toolkit.NetworkErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @AuthNavigation private val navigationEventHandler: NavigationEventHandler,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val networkErrorManager: NetworkErrorManager
) : ViewModel() {
    
    val navigationEvent = navigationEventHandler.routeToNavigate
    val showNetworkErrorDialog = networkErrorManager.showDialog

    private val _isNetworkConnected = MutableStateFlow(networkConnectivityObserver.isCurrentlyConnected())
    val isNetworkConnected: StateFlow<Boolean> = _isNetworkConnected.asStateFlow()

    init {
        checkInitialNetworkState()
    }

    private fun checkInitialNetworkState() {
        if (_isNetworkConnected.value.not()) {
            viewModelScope.launch {
                networkErrorManager.sendDialogEvent(NetworkErrorEvent.NoInternet)
            }
        }
    }

    fun onRetryClicked() {
        val isConnected = networkConnectivityObserver.isCurrentlyConnected()
        _isNetworkConnected.value = isConnected
        
        if (isConnected) {
            viewModelScope.launch {
                networkErrorManager.requestRetry()
            }
        }
    }
}