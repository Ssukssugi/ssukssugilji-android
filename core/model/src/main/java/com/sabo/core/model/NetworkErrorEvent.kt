package com.sabo.core.model

sealed interface NetworkErrorEvent {

    val dialogTitle: String
    val dialogMessage: String
    val screenTitle: String
    val screenMessage: String

    data object NoInternet : NetworkErrorEvent {
        override val dialogTitle: String = "인터넷이 연결되지 않았어요."
        override val dialogMessage: String = "인터넷 연결 상태가 좋지 않아요. 인터넷 연결 확인 후 다시 시도해주세요"
        override val screenTitle: String = "인터넷 연결 상태가 좋지 않아요."
        override val screenMessage: String = "인터넷 연결 확인 후 다시 시도해주세요"
    }

    data object Timeout : NetworkErrorEvent {
        override val dialogTitle: String = "연결 시간이 초과되었어요."
        override val dialogMessage: String = "네트워크 연결이 불안정해요. 잠시 후 다시 시도해주세요"
        override val screenTitle: String = "연결 시간이 초과되었어요."
        override val screenMessage: String = "잠시 후 다시 시도해주세요"
    }
}
