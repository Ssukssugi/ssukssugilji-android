package com.sabo.feature.main

import androidx.annotation.DrawableRes
import com.sabo.core.designsystem.R
import com.sabo.core.navigator.MainTabRoute

internal enum class MainTab(
    val route: MainTabRoute,
    val label: String,
    @DrawableRes val iconRes: Int
) {
    DIARY(
        route = MainTabRoute.Diary,
        label = "쑥쑥일지",
        iconRes = R.drawable.icon_bottom_nav_diary_24
    ),
    TOWN(
        route = MainTabRoute.Town,
        label = "쑥쑥자랑",
        iconRes = R.drawable.icon_bottom_nav_town_24
    ),
    PROFILE(
        route = MainTabRoute.Profile,
        label = "나의 집사 프로필",
        iconRes = R.drawable.icon_bottom_nav_profile_24
    );

    companion object {
        val START_TAB = DIARY

        fun fromRoute(route: MainTabRoute?): MainTab {
            return entries.find { it.route == route } ?: START_TAB
        }
    }
}
