package com.sabo.core.designsystem.theme.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme

enum class NavigationType(@DrawableRes val iconResId: Int?) {
    BACK(R.drawable.icon_arrow_left), CLOSE(R.drawable.icon_close_24), NONE(null)
}

@Composable
fun SsukssukTopAppBar(
    modifier: Modifier = Modifier,
    navigationType: NavigationType = NavigationType.NONE,
    onNavigationClick: () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.surfaceDim,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        val icon: @Composable (Modifier, imageVector: ImageVector) -> Unit = { modifier, imageVector ->
            IconButton(
                onClick = onNavigationClick,
                modifier = modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = contentColor
                )
            }
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(color = containerColor)
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            navigationType.iconResId?.let {
                icon(
                    modifier.align(Alignment.CenterStart),
                    ImageVector.vectorResource(id = it)
                )
            }
        }
    }
}

@Preview
@Composable
private fun SsukssukTopAppBarPreview() {
    SsukssukDiaryTheme {
        SsukssukTopAppBar(
            navigationType = NavigationType.BACK
        )
    }
}