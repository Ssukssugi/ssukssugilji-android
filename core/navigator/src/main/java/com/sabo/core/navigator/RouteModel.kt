package com.sabo.core.navigator

import kotlinx.serialization.Serializable

sealed interface RouteModel {

    @Serializable
    data object Login : RouteModel

    @Serializable
    data object SignUp : RouteModel

    @Serializable
    data object Home : RouteModel

    @Serializable
    data object PlantAdd : RouteModel

    @Serializable
    data class CategorySearch(val keyword: String): RouteModel {
        companion object {
            const val EXTRA_KEY = "keyword"
        }
    }

    @Serializable
    data object Gallery : RouteModel
}