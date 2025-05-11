package com.sabo.feature.login

import android.content.Context

abstract class LoginManager(
    private val context: Context,
    private val callbackListener: CallbackListener
) {
    interface CallbackListener {
        fun onSuccess(token: String)
    }
}