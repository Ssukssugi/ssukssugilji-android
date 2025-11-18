package com.sabo.feature.town.mygrowth.variation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography

enum class VariationImageType {
    BEFORE, AFTER
}

sealed interface DisplayMode {
    data object EmptyEnabled : DisplayMode
    data object EmptyDisabled : DisplayMode
    data object ImageFilled : DisplayMode
}

@Stable
interface VariationImageScope {
    val imageUrl: String?
    val isEnabled: Boolean
    val type: VariationImageType
    val displayMode: DisplayMode

    val hasImage: Boolean
        get() = imageUrl != null

    val typeLabel: String
        get() = when (type) {
            VariationImageType.BEFORE -> "Before"
            VariationImageType.AFTER -> "After"
        }
}

@Stable
private class DefaultVariationImageScope(
    override val imageUrl: String?,
    override val isEnabled: Boolean,
    override val type: VariationImageType
) : VariationImageScope {
    override val displayMode: DisplayMode = when {
        imageUrl != null -> DisplayMode.ImageFilled
        isEnabled -> DisplayMode.EmptyEnabled
        else -> DisplayMode.EmptyDisabled
    }
}

@Composable
fun VariationImageCard(
    imageUrl: String?,
    type: VariationImageType,
    isEnabled: Boolean = true,
    content: @Composable VariationImageScope.() -> Unit
) {
    val scope = remember(imageUrl, type, isEnabled) { DefaultVariationImageScope(imageUrl = imageUrl, isEnabled = isEnabled, type = type) }
    scope.content()
}

@Composable
fun VariationImageScope.Container(
    content: @Composable BoxScope.() -> Unit
) {
    val borderColor = when (displayMode) {
        DisplayMode.EmptyEnabled -> DiaryColorsPalette.current.green400
        else -> Color.Transparent
    }

    val backgroundColor = when (displayMode) {
        DisplayMode.EmptyEnabled -> DiaryColorsPalette.current.gray600
        DisplayMode.EmptyDisabled -> DiaryColorsPalette.current.gray200
        DisplayMode.ImageFilled -> Color.White
    }

    Box(
        modifier = Modifier
            .size(124.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .then(
                if (borderColor != Color.Transparent) {
                    Modifier.border(
                        width = 2.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun VariationImageScope.PlaceholderIcon(
    modifier: Modifier = Modifier
) {
    val iconTint = when (displayMode) {
        DisplayMode.EmptyEnabled -> Color.White
        else -> DiaryColorsPalette.current.gray400
    }

    Box(
        modifier = modifier.size(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(
                id = R.drawable.icon_plant_outline_32
            ),
            contentDescription = "Plant placeholder",
            modifier = Modifier.size(32.dp),
            tint = iconTint
        )
    }
}

@Composable
fun VariationImageScope.ActualImage(
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "$typeLabel image",
        modifier = modifier
            .size(150.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun VariationImageScope.Label(
    modifier: Modifier = Modifier
) {
    val labelBackgroundColor = when (displayMode) {
        DisplayMode.EmptyEnabled -> DiaryColorsPalette.current.green400
        DisplayMode.EmptyDisabled -> DiaryColorsPalette.current.gray500
        DisplayMode.ImageFilled -> DiaryColorsPalette.current.gray500
    }

    val labelTextColor = DiaryColorsPalette.current.gray50

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(40.dp))
            .background(labelBackgroundColor)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = typeLabel,
            style = DiaryTypography.captionMediumRegular,
            color = labelTextColor
        )
    }
}

@Composable
fun VariationImageScope.ShowWhenEmpty(
    content: @Composable () -> Unit
) {
    if (!hasImage) {
        content()
    }
}

@Composable
fun VariationImageScope.ShowWhenFilled(
    content: @Composable () -> Unit
) {
    if (hasImage) {
        content()
    }
}

@Composable
fun VariationImageScope.DefaultContent() {
    Container {
        ShowWhenEmpty {
            PlaceholderIcon()
        }
        ShowWhenFilled {
            ActualImage()
        }
        Label(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )
    }
}