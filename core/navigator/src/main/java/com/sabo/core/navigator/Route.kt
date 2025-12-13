package com.sabo.core.navigator

import com.sabo.core.model.PlantEnvironmentPlace
import com.sabo.core.navigator.model.RouteModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Login : RouteModel

    @Serializable
    data object SignUp : RouteModel

    sealed interface PlantAddEdit : Route

    @Serializable
    data object PlantAdd : PlantAddEdit

    @Serializable
    data class PlantEdit(
        val plantId: Long,
        val name: String,
        val category: String,
        val shine: Int?,
        val place: PlantEnvironmentPlace
    ) : PlantAddEdit {
        companion object {
            const val EXTRA_PLANT_ID = "plant_id"
        }
    }

    @Serializable
    data class CategorySearch(
        val keyword: String
    ) : Route {
        companion object {
            const val EXTRA_KEYWORD = "keyword"
        }
    }

    @Serializable
    data class Gallery(
        val plantId: Long?
    ) : Route

    @Serializable
    data class DiaryWrite(
        val plantId: Long,
        val imageUrl: String
    ) : Route

    @Serializable
    data object MyGrowth : Route

    @Serializable
    data object SelectGrowthPlant : Route

    @Serializable
    data class GrowthVariation(
        val plantId: Long,
        val plantName: String
    ) : Route

    @Serializable
    data object Profile : Route

    @Serializable
    data class ChangeProfile(
        val name: String
    ) : Route {
        companion object {
            const val RESULT_PROFILE_UPDATED = "profile_updated"
        }
    }

    @Serializable
    data object Settings : Route

    @Serializable
    data object UserDelete : Route

    @Serializable
    data object Policy : Route

    @Serializable
    data class WebLink(val link: Link): Route {
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