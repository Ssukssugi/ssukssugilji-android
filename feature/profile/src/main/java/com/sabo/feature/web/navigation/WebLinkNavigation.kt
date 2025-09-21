package com.sabo.feature.web.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sabo.core.navigator.RouteModel
import com.sabo.feature.web.WebLinkScreen

fun NavController.navigateToWebLink(link: RouteModel.WebLink.Link) {
    navigate(RouteModel.WebLink(link))
}

fun NavGraphBuilder.webLinkScreen(
    onClickClose: () -> Unit
) {
    composable<RouteModel.WebLink> { entry ->
        val route = entry.toRoute<RouteModel.WebLink>()
        WebLinkScreen(
            onClickClose = onClickClose,
            link = route.link
        )
    }
}
