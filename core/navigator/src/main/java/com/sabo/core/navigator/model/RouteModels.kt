package com.sabo.core.navigator.model

import com.sabo.core.model.CareType
import com.sabo.core.model.PlantEnvironmentPlace
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data object Login : RouteModel

@Serializable
data object SignUp : RouteModel

@Serializable
data object Home : RouteModel

@Serializable
sealed interface PlantAddEdit : RouteModel {

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
            const val EXTRA_PLANT_ID = "plant_Id"
        }
    }
}

@Serializable
data class CategorySearch(val keyword: String) : RouteModel {
    companion object {
        const val EXTRA_KEY = "keyword"
    }
}

@Serializable
data class Gallery(val plantId: Long) : RouteModel

@Serializable
data class DiaryWrite(val plantId: Long, val imageUri: String) : RouteModel

@Serializable
data class DiaryEdit(
    val plantId: Long,
    val diaryId: Long,
    val imageUri: String,
    val date: String,
    val careType: List<CareType>,
    val content: String
): RouteModel

@Serializable
data class DiaryDetail(val plantId: Long, val diaryId: Long) : RouteModel

@Serializable
data object MyGrowth : RouteModel

@Serializable
data object SelectGrowthPlant : RouteModel

@Serializable
data class GrowthVariation(
    val plantId: Long,
    val plantName: String
) : RouteModel

@Serializable
data object Profile : RouteModel

@Serializable
data object Settings : RouteModel

@Serializable
data object UserDelete : RouteModel

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

@Serializable
data class ChangeProfile(val name: String) : RouteModel {
    companion object {
        const val RESULT_PROFILE_UPDATED = "profile_updated"
    }
}