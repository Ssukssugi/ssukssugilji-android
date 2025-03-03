package com.sabo.core.designsystem.theme

import android.annotation.SuppressLint
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LightDiaryColors = DiaryColors(
    green900 = Color(0xFF007426),
    green800 = Color(0xFF00953B),
    green700 = Color(0xFF00A647),
    green600 = Color(0xFF00BA55),
    green500 = Color(0xFF00C86E),
    green400 = Color(0xFF03D379),
    green300 = Color(0xFF59DC92),
    green200 = Color(0xFF92E6B2),
    green100 = Color(0xFFBEF0D0),
    green50 = Color(0xFFE0F9E9),
    gray900 = Color(0xFF161E2D),
    gray800 = Color(0xFF39404D),
    gray700 = Color(0xFF454B57),
    gray600 = Color(0xFF686D77),
    gray500 = Color(0xFFAEB0B6),
    gray400 = Color(0xFFD0D2D5),
    gray300 = Color(0xFFDCDDE0),
    gray200 = Color(0xFFECEDEE),
    gray100 = Color(0xFFF8F8F9),
    gray50 = Color(0xFFFFFFFF),
    red900 = Color(0xFFC13525),
    red800 = Color(0xFFD03F30),
    red700 = Color(0xFFDD4537),
    red600 = Color(0xFFF04F3E),
    red500 = Color(0xFFFF593F),
    red400 = Color(0xFFFB695D),
    red300 = Color(0xFFF68480),
    red200 = Color(0xFFFBABA8),
    red100 = Color(0xFFFEDBD9),
    red50 = Color(0xFFFBE9E8)
)

val DarkDiaryColors = DiaryColors(
    green900 = Color(0xFFE4F9EC),
    green800 = Color(0xFFBEF0D0),
    green700 = Color(0xFF92E6B2),
    green600 = Color(0xFF59DC92),
    green500 = Color(0xFF03D379),
    green400 = Color(0xFF00C86E),
    green300 = Color(0xFF00BA55),
    green200 = Color(0xFF00A647),
    green100 = Color(0xFF00953B),
    green50 = Color(0xFF007426),
    gray900 = Color(0xFFFDFDFD),
    gray800 = Color(0xFFFDFDFD),
    gray700 = Color(0xFFF8F8F9),
    gray600 = Color(0xFFECEDEE),
    gray500 = Color(0xFFDCDDE0),
    gray400 = Color(0xFFD0D2D5),
    gray300 = Color(0xFFAEB0B6),
    gray200 = Color(0xFF686D77),
    gray100 = Color(0xFF454B57),
    gray50 = Color(0xFF39404D),
    red900 = Color(0xFFFBE9E8),
    red800 = Color(0xFFFEDBD9),
    red700 = Color(0xFFFBABA8),
    red600 = Color(0xFFF68480),
    red500 = Color(0xFFFB695D),
    red400 = Color(0xFFFF593F),
    red300 = Color(0xFFF04F3E),
    red200 = Color(0xFFDD4537),
    red100 = Color(0xFFD03F30),
    red50 = Color(0xFFC13525)
)

@Immutable
data class DiaryColors(
    val green900: Color = Color.Unspecified,
    val green800: Color = Color.Unspecified,
    val green700: Color = Color.Unspecified,
    val green600: Color = Color.Unspecified,
    val green500: Color = Color.Unspecified,
    val green400: Color = Color.Unspecified,
    val green300: Color = Color.Unspecified,
    val green200: Color = Color.Unspecified,
    val green100: Color = Color.Unspecified,
    val green50: Color = Color.Unspecified,
    val gray900: Color = Color.Unspecified,
    val gray800: Color = Color.Unspecified,
    val gray700: Color = Color.Unspecified,
    val gray600: Color = Color.Unspecified,
    val gray500: Color = Color.Unspecified,
    val gray400: Color = Color.Unspecified,
    val gray300: Color = Color.Unspecified,
    val gray200: Color = Color.Unspecified,
    val gray100: Color = Color.Unspecified,
    val gray50: Color = Color.Unspecified,
    val red900: Color = Color.Unspecified,
    val red800: Color = Color.Unspecified,
    val red700: Color = Color.Unspecified,
    val red600: Color = Color.Unspecified,
    val red500: Color = Color.Unspecified,
    val red400: Color = Color.Unspecified,
    val red300: Color = Color.Unspecified,
    val red200: Color = Color.Unspecified,
    val red100: Color = Color.Unspecified,
    val red50: Color = Color.Unspecified,
)

@SuppressLint("CompositionLocalNaming")
val DiaryColorsPalette = staticCompositionLocalOf { DiaryColors() }