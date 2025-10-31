package com.sabo.feature.web.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sabo.core.navigator.model.WebLink
import com.sabo.core.navigator.toolkit.slideInFromBottom
import com.sabo.core.navigator.toolkit.slideOutToBottom
import com.sabo.core.navigator.toolkit.zoomIn
import com.sabo.core.navigator.toolkit.zoomOut
import com.sabo.feature.web.WebLinkScreen

fun NavController.navigateToWebLink(link: WebLink.Link) {
    navigate(WebLink(link))
}

fun NavGraphBuilder.webLinkScreen(
    onClickClose: () -> Unit
) {
    composable<WebLink>(
        enterTransition = slideInFromBottom(),
        exitTransition = zoomOut(),
        popEnterTransition = zoomIn(),
        popExitTransition = slideOutToBottom()
    ) { entry ->
        val route = entry.toRoute<WebLink>()
        WebLinkScreen(
            onClickClose = onClickClose,
            link = route.link
        )
    }
}
