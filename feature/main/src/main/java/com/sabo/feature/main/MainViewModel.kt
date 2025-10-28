package com.sabo.feature.main

import androidx.lifecycle.ViewModel
import com.sabo.core.navigator.NavigationEventHandler
import com.sabo.core.navigator.di.AuthNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @AuthNavigation private val navigationEventHandler: NavigationEventHandler
) : ViewModel() {
    val navigationEvent = navigationEventHandler.routeToNavigate
}