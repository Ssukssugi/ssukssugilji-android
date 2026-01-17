package com.sabo.core.toolkit

import com.sabo.core.model.NetworkErrorEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 전역 네트워크 에러 관리자
 *
 * - Channel을 사용하여 이벤트가 정확히 한 번만 소비되는 것을 보장
 * - 앱의 루트 레벨(MainActivity 등)에서 showDialog를 수집하여 다이얼로그 표시
 */
@Singleton
class NetworkErrorManager @Inject constructor() {

    private val _showDialog = Channel<NetworkErrorEvent>(Channel.RENDEZVOUS)
    val showDialog = _showDialog.receiveAsFlow()

    suspend fun sendDialogEvent(event: NetworkErrorEvent) {
        _showDialog.send(event)
    }
}
