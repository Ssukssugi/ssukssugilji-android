package com.sabo.core.navigator

import kotlinx.serialization.Serializable

sealed interface RouteModel {

    @Serializable
    data object Login : RouteModel
}