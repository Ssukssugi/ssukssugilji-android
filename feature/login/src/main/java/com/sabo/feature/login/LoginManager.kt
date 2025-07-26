package com.sabo.feature.login

import android.content.Context

abstract class LoginManager(
    open val context: Context,
    open val callbackListener: CallbackListener
) {
    interface CallbackListener {
        fun onSuccess(token: String)
    }
}