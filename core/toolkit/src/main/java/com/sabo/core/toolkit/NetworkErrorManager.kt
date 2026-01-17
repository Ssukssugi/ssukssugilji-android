package com.sabo.core.toolkit

import com.sabo.core.model.NetworkErrorEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkErrorManager @Inject constructor() {

    private val _showDialog = Channel<NetworkErrorEvent>(Channel.RENDEZVOUS)
    val showDialog = _showDialog.receiveAsFlow()

    private val _showScreen = Channel<NetworkErrorEvent>(Channel.RENDEZVOUS)
    val showScreen = _showScreen.receiveAsFlow()

    suspend fun sendDialogEvent(event: NetworkErrorEvent) {
        _showDialog.send(event)
    }

    suspend fun sendScreenEvent(event: NetworkErrorEvent) {
        _showScreen.send(event)
    }
}
