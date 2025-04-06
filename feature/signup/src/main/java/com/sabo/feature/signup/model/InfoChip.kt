package com.sabo.feature.signup.model

import androidx.compose.runtime.Immutable

abstract class InfoChip(
    open val isSelected: Boolean
) {
    abstract val text: String
}

@Immutable
data class AgeChip(
    override val isSelected: Boolean,
    val age: Age
) : InfoChip(isSelected) {
    enum class Age(val text: String) {
        TWENTY("20대"), THIRTY("30대"), FORTY("40대"), FIFTY("50대"), SIXTY("60대"), OLDER("70대 이상")
    }

    override val text: String = age.text
}

@Immutable
data class PlantReasonChip(
    override val isSelected: Boolean,
    val plantReason: PlantReason
) : InfoChip(isSelected) {
    enum class PlantReason(val text: String) {
        HEALING("힐링"), INTERIOR("인테리어"), FOOD("식재료 수확"), FEEL("뿌듯함"), ETC("기타")
    }

    override val text: String = plantReason.text
}

@Immutable
data class HowKnownChip(
    override val isSelected: Boolean,
    val howKnown: HowKnown
) : InfoChip(isSelected) {
    enum class HowKnown(val text: String) {
        APPSTORE("스토어 검색"), BLOG("블로그"), INSTAGRAM("인스타그램"), FRIEND("친구 추천"), ETC("기타")
    }

    override val text: String = howKnown.text
}