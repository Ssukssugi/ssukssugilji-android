package com.sabo.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkDiaryColors.green500,
    tertiary = DarkDiaryColors.gray900,
    error = DarkDiaryColors.red400
)

private val LightColorScheme = lightColorScheme(
    primary = LightDiaryColors.green400,
    tertiary = LightDiaryColors.gray900,
    error = LightDiaryColors.red500
)

@Composable
fun SsukssukDiaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val diaryColorPalette = if (darkTheme) DarkDiaryColors else LightDiaryColors

    CompositionLocalProvider(
        DiaryColorsPalette provides diaryColorPalette
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}