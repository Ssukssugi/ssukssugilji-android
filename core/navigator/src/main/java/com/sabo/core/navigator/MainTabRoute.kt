package com.sabo.core.navigator

sealed interface MainTabRoute {
    data object Diary: MainTabRoute
    data object Town: MainTabRoute
    data object Profile: MainTabRoute
}