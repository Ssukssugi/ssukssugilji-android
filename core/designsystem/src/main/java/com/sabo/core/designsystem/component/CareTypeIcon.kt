package com.sabo.core.designsystem.component

import androidx.annotation.DrawableRes
import com.sabo.core.designsystem.R
import com.sabo.core.model.CareType

enum class CareTypeIcon(
    val type: CareType,
    @param:DrawableRes val iconRes: Int
) {
    WATER(
        CareType.WATER,
        R.drawable.img_care_water
    ),

    DIVIDING(
        CareType.DIVIDING,
        R.drawable.img_care_dividing
    ),

    NUTRIENT(
        CareType.NUTRIENT,
        R.drawable.img_care_medicine
    ),

    PRUNING(
        CareType.PRUNING,
        R.drawable.img_care_cut
    )
}