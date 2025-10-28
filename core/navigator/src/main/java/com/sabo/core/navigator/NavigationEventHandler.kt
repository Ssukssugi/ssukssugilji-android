package com.sabo.core.navigator

import com.sabo.core.navigator.model.RouteModel
import kotlinx.coroutines.flow.SharedFlow

interface NavigationEventHandler {
    val routeToNavigate : SharedFlow<RouteModel>
    suspend fun triggerRouteToNavigate()
}