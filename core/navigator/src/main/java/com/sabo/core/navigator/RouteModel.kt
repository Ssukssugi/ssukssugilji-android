package com.sabo.core.navigator

import kotlinx.serialization.Serializable

sealed interface RouteModel {

    @Serializable
    data object Login : RouteModel

    @Serializable
    data object SignUp : RouteModel
}