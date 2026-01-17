package com.sabo.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.core.model.NetworkErrorEvent

/**
 * 네트워크 에러 다이얼로그
 *
 * @param event 네트워크 에러 이벤트
 * @param onDismiss 다이얼로그 닫기
 * @param onRetry 재시도 버튼 클릭
 */
@Composable
fun NetworkErrorDialog(
    event: NetworkErrorEvent,
    onDismiss: () -> Unit = {},
    onRetry: () -> Unit = {}
) {
    val title = event.dialogTitle
    val message = event.dialogMessage

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 16.dp)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_notice_triangle),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = title,
                    style = DiaryTypography.subtitleLargeBold,
                    color = DiaryColorsPalette.current.gray900,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = message,
                    style = DiaryTypography.bodyLargeMedium,
                    color = DiaryColorsPalette.current.gray600,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(36.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = Color(0xFF03D379), shape = RoundedCornerShape(16.dp))
                        .clickable {
                            onRetry()
                            onDismiss()
                        }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "다시 시도하기",
                        style = DiaryTypography.subtitleMediumBold,
                        color = DiaryColorsPalette.current.gray50
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NetworkErrorDialogPreview() {
    SsukssukDiaryTheme {
        NetworkErrorDialog(
            event = NetworkErrorEvent.NoInternet,
            onDismiss = {},
            onRetry = {}
        )
    }
}