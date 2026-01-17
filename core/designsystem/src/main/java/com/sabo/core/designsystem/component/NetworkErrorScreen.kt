package com.sabo.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.core.model.NetworkErrorEvent

/**
 * 네트워크 에러 전체 화면
 *
 * 여러 feature 모듈에서 재사용 가능한 네트워크 에러 화면
 *
 * @param event 네트워크 에러 이벤트 (타이틀, 메시지 포함)
 * @param onRetry 다시 시도하기 버튼 클릭 콜백
 * @param modifier Modifier
 */
@Composable
fun NetworkErrorScreen(
    event: NetworkErrorEvent,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.icon_wifi_off),
            contentDescription = "네트워크 에러",
            modifier = Modifier.size(64.dp),
            tint = DiaryColorsPalette.current.gray400
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = event.screenTitle,
            style = DiaryTypography.headlineSmallBold,
            color = DiaryColorsPalette.current.gray900,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = event.screenMessage,
            style = DiaryTypography.subtitleMediumBold,
            color = DiaryColorsPalette.current.gray600,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(36.dp))

        Box(
            modifier = Modifier
                .width(224.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    color = DiaryColorsPalette.current.green500,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onRetry() }
                .padding(vertical = 18.dp),
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

@Preview(showBackground = true)
@Composable
private fun NetworkErrorScreenPreview() {
    SsukssukDiaryTheme {
        NetworkErrorScreen(
            event = NetworkErrorEvent.NoInternet,
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NetworkErrorScreenTimeoutPreview() {
    SsukssukDiaryTheme {
        NetworkErrorScreen(
            event = NetworkErrorEvent.Timeout,
            onRetry = {}
        )
    }
}
