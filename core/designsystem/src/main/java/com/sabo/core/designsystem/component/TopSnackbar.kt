package com.sabo.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import kotlinx.coroutines.delay

@Composable
fun TopSnackBar(
    modifier: Modifier = Modifier,
    message: String,
    iconRes: Int = -1,
    iconTint: Color = Color.Unspecified,
    visible: Boolean = true,
    duration: Long = 1000L,
    backgroundTint : Color = Color(0xB3000000),
    textColor : Color = DiaryColorsPalette.current.gray50,
    dismissOnLifecycleStop: Boolean = true,
    onDismiss: () -> Unit = {}
) {
    var dismissed by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }
    var prevVisible by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (dismissOnLifecycleStop && event == Lifecycle.Event.ON_STOP) {
                dismissed = true
                isVisible = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(visible) {
        if (visible && !prevVisible) {
            dismissed = false
            isVisible = true
            delay(duration)
            if (!dismissed) {
                isVisible = false
                delay(300)
                onDismiss()
            }
        } else if (!visible) {
            isVisible = false
        }
        prevVisible = visible
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = backgroundTint,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(iconRes),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = message,
                    style = DiaryTypography.bodyLargeMedium,
                    color = textColor
                )
            }
        }
    }
}

data class SnackBarState(
    val message: String = "",
    val iconRes: Int = -1,
    val iconTint: Color = Color.Unspecified,
    val isVisible: Boolean = false
)

@Composable
fun rememberSnackBarState(): MutableState<SnackBarState> {
    return remember { mutableStateOf(SnackBarState()) }
}

@Preview
@Composable
private fun TopSnackBarPreview() {
    SsukssukDiaryTheme {
        TopSnackBar(
            message = "test"
        )
    }
}