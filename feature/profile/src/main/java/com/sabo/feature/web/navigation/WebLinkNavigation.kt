package com.sabo.feature.web.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sabo.core.navigator.model.WebLink
import com.sabo.feature.web.WebLinkScreen

fun NavController.navigateToWebLink(link: WebLink.Link) {
    navigate(WebLink(link))
}

fun NavGraphBuilder.webLinkScreen(
    onClickClose: () -> Unit
) {
    composable<WebLink> { entry ->
        val route = entry.toRoute<WebLink>()
        WebLinkScreen(
            onClickClose = onClickClose,
            link = route.link
        )
    }
}
