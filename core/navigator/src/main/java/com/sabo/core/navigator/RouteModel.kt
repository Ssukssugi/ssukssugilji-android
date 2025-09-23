package com.sabo.core.navigator

import kotlinx.serialization.SerialName
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
    data class CategorySearch(val keyword: String) : RouteModel {
        companion object {
            const val EXTRA_KEY = "keyword"
        }
    }

    @Serializable
    data object Gallery : RouteModel

    @Serializable
    data class DiaryWrite(val imageUri: String) : RouteModel

    @Serializable
    data class DiaryDetail(val plantId: Long) : RouteModel

    @Serializable
    data object Profile : RouteModel

    @Serializable
    data object Settings : RouteModel

    @Serializable
    data object Policy : RouteModel

    @Serializable
    data class WebLink(val link: Link) : RouteModel {

        @Serializable
        enum class Link(val url: String, val title: String) {
            @SerialName("QNA")
            QNA("https://forms.gle/PhhnS6dLPQREXvrGA", "의견 보내기"),

            @SerialName("POLICY")
            POLICY("https://brazen-objective-f15.notion.site/244f509f94c9816bbbd6dd98ab92fb96?source=copy_link", "서비스 이용약관"),

            @SerialName("PRIVACY")
            PRIVACY("https://brazen-objective-f15.notion.site/244f509f94c981fa97d9edec16d75c2e?source=copy_link", "개인정보 수집 / 이용 동의서")
        }
    }
}