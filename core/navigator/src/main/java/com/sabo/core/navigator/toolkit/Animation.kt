package com.sabo.core.navigator.toolkit

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.NavBackStackEntry

private const val ANIMATION_DURATION_MILLIS = 200

fun zoomOut(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    scaleOut(
        animationSpec = tween(
            durationMillis = ANIMATION_DURATION_MILLIS,
            easing = FastOutSlowInEasing
        ),
        targetScale = 0.95f,
        transformOrigin = TransformOrigin(0.5f, 0.5f)
    ) + fadeOut(
        animationSpec = tween(
            durationMillis = ANIMATION_DURATION_MILLIS,
            easing = FastOutSlowInEasing
        ),
        targetAlpha = 0.6f
    )
}

fun zoomIn(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    scaleIn(
        animationSpec = tween(
            durationMillis = ANIMATION_DURATION_MILLIS,
            easing = FastOutSlowInEasing
        ),
        initialScale = 0.95f,
        transformOrigin = TransformOrigin(0.5f, 0.5f)
    ) + fadeIn(
        animationSpec = tween(
            durationMillis = ANIMATION_DURATION_MILLIS,
            easing = FastOutSlowInEasing
        ),
        initialAlpha = 0.6f
    )
}

fun slideInFromEnd(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideIntoContainer(
        towards = SlideDirection.Left,
        animationSpec = tween(
            durationMillis = ANIMATION_DURATION_MILLIS,
            easing = FastOutSlowInEasing
        )
    )
}

fun slideOutToEnd(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutOfContainer(
        towards = SlideDirection.Right,
        animationSpec = tween(
            durationMillis = ANIMATION_DURATION_MILLIS,
            easing = FastOutSlowInEasing
        )
    )
}

fun slideInFromBottom(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideIntoContainer(
        towards = SlideDirection.Up,
        animationSpec = tween(
            durationMillis = ANIMATION_DURATION_MILLIS,
            easing = FastOutSlowInEasing
        )
    )
}

fun slideOutToBottom(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutOfContainer(
        towards = SlideDirection.Down,
        animationSpec = tween(
            durationMillis = ANIMATION_DURATION_MILLIS,
            easing = FastOutSlowInEasing
        )
    )
}

fun tabFadeIn(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    fadeIn(
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        )
    )
}

fun tabFadeOut(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    fadeOut(
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        )
    )
}
