package com.sabo.core.toolkit

import com.sabo.core.model.NetworkErrorEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkErrorManager @Inject constructor() {

    private val _showDialog = Channel<NetworkErrorEvent>(Channel.BUFFERED)
    val showDialog = _showDialog.receiveAsFlow()

    private val _showScreen = Channel<NetworkErrorEvent>(Channel.BUFFERED)
    val showScreen = _showScreen.receiveAsFlow()

    // retry 이벤트는 여러 ViewModel에서 수집해야 하므로 SharedFlow 사용
    private val _retryRequested = MutableSharedFlow<Unit>()
    val retryRequested = _retryRequested.asSharedFlow()

    suspend fun sendDialogEvent(event: NetworkErrorEvent) {
        _showDialog.send(event)
    }

    suspend fun sendScreenEvent(event: NetworkErrorEvent) {
        _showScreen.send(event)
    }

    suspend fun requestRetry() {
        _retryRequested.emit(Unit)
    }
}
