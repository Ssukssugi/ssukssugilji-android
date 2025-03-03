package com.sabo.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val SansSerifStyle = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal
)

internal val DiaryTypography = LocalDiaryTypography(
    // BOLD
    headlineLargeBold = SansSerifStyle.copy(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    ),
    headlineMediumBold = SansSerifStyle.copy(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    ),
    headlineSmallBold = SansSerifStyle.copy(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    subtitleLargeBold = SansSerifStyle.copy(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    subtitleMediumBold = SansSerifStyle.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    ),
    bodyLargeBold = SansSerifStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    ),
    bodyMediumBold = SansSerifStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    ),
    bodySmallBold = SansSerifStyle.copy(
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold
    ),
    captionLargeBold = SansSerifStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
    ),
    captionMediumBold = SansSerifStyle.copy(
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
    ),
    captionSmallBold = SansSerifStyle.copy(
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold
    ),

    // SEMI-BOLD
    headlineLargeSemiBold = SansSerifStyle.copy(
        fontSize = 32.sp,
        fontWeight = FontWeight.SemiBold
    ),
    headlineMediumSemiBold = SansSerifStyle.copy(
        fontSize = 28.sp,
        fontWeight = FontWeight.SemiBold
    ),
    headlineSmallSemiBold = SansSerifStyle.copy(
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold
    ),
    subtitleLargeSemiBold = SansSerifStyle.copy(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold
    ),
    subtitleMediumSemiBold = SansSerifStyle.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold
    ),
    bodyLargeSemiBold = SansSerifStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    ),
    bodyMediumSemiBold = SansSerifStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold
    ),
    bodySmallSemiBold = SansSerifStyle.copy(
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold
    ),
    captionLargeSemiBold = SansSerifStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
    ),
    captionMediumSemiBold = SansSerifStyle.copy(
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold
    ),
    captionSmallSemiBold = SansSerifStyle.copy(
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold
    ),

    // MEDIUM
    headlineLargeMedium = SansSerifStyle.copy(
        fontSize = 32.sp,
        fontWeight = FontWeight.Medium
    ),
    headlineMediumMedium = SansSerifStyle.copy(
        fontSize = 28.sp,
        fontWeight = FontWeight.Medium
    ),
    headlineSmallMedium = SansSerifStyle.copy(
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium
    ),
    subtitleLargeMedium = SansSerifStyle.copy(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
    ),
    subtitleMediumMedium = SansSerifStyle.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium
    ),
    bodyLargeMedium = SansSerifStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    ),
    bodyMediumMedium = SansSerifStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    ),
    bodySmallMedium = SansSerifStyle.copy(
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
    ),
    captionLargeMedium = SansSerifStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    ),
    captionMediumMedium = SansSerifStyle.copy(
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium
    ),
    captionSmallMedium = SansSerifStyle.copy(
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium
    ),

    // REGULAR
    headlineLargeRegular = SansSerifStyle.copy(
        fontSize = 32.sp
    ),
    headlineMediumRegular = SansSerifStyle.copy(
        fontSize = 28.sp
    ),
    headlineSmallRegular = SansSerifStyle.copy(
        fontSize = 24.sp
    ),
    subtitleLargeRegular = SansSerifStyle.copy(
        fontSize = 20.sp
    ),
    subtitleMediumRegular = SansSerifStyle.copy(
        fontSize = 18.sp
    ),
    bodyLargeRegular = SansSerifStyle.copy(
        fontSize = 16.sp
    ),
    bodyMediumRegular = SansSerifStyle.copy(
        fontSize = 14.sp
    ),
    bodySmallRegular = SansSerifStyle.copy(
        fontSize = 13.sp
    ),
    captionLargeRegular = SansSerifStyle.copy(
        fontSize = 12.sp
    ),
    captionMediumRegular = SansSerifStyle.copy(
        fontSize = 11.sp
    ),
    captionSmallRegular = SansSerifStyle.copy(
        fontSize = 10.sp
    )
)

@Immutable
data class LocalDiaryTypography(
    // BOLD
    val headlineLargeBold: TextStyle,
    val headlineMediumBold: TextStyle,
    val headlineSmallBold: TextStyle,

    val subtitleLargeBold: TextStyle,
    val subtitleMediumBold: TextStyle,

    val bodyLargeBold: TextStyle,
    val bodyMediumBold: TextStyle,
    val bodySmallBold: TextStyle,

    val captionLargeBold: TextStyle,
    val captionMediumBold: TextStyle,
    val captionSmallBold: TextStyle,

    // SEMI-BOLD
    val headlineLargeSemiBold: TextStyle,
    val headlineMediumSemiBold: TextStyle,
    val headlineSmallSemiBold: TextStyle,

    val subtitleLargeSemiBold: TextStyle,
    val subtitleMediumSemiBold: TextStyle,

    val bodyLargeSemiBold: TextStyle,
    val bodyMediumSemiBold: TextStyle,
    val bodySmallSemiBold: TextStyle,

    val captionLargeSemiBold: TextStyle,
    val captionMediumSemiBold: TextStyle,
    val captionSmallSemiBold: TextStyle,

    // MEDIUM
    val headlineLargeMedium: TextStyle,
    val headlineMediumMedium: TextStyle,
    val headlineSmallMedium: TextStyle,

    val subtitleLargeMedium: TextStyle,
    val subtitleMediumMedium: TextStyle,

    val bodyLargeMedium: TextStyle,
    val bodyMediumMedium: TextStyle,
    val bodySmallMedium: TextStyle,

    val captionLargeMedium: TextStyle,
    val captionMediumMedium: TextStyle,
    val captionSmallMedium: TextStyle,

    // REGULAR
    val headlineLargeRegular: TextStyle,
    val headlineMediumRegular: TextStyle,
    val headlineSmallRegular: TextStyle,

    val subtitleLargeRegular: TextStyle,
    val subtitleMediumRegular: TextStyle,

    val bodyLargeRegular: TextStyle,
    val bodyMediumRegular: TextStyle,
    val bodySmallRegular: TextStyle,

    val captionLargeRegular: TextStyle,
    val captionMediumRegular: TextStyle,
    val captionSmallRegular: TextStyle
)