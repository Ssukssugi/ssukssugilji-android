package com.sabo.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.NavigationType
import com.sabo.core.designsystem.component.SsukssukTopAppBar
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.core.navigator.WebLink

@Composable
internal fun PolicyScreen(
    onClickBack: () -> Unit,
    navigateToWebLink: (WebLink.Link) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        SsukssukTopAppBar(
            navigationType = NavigationType.BACK,
            containerColor = Color(0xFFFFFFFF),
            content = {
                Text(
                    text = "약관 및 정책",
                    color = DiaryColorsPalette.current.gray900,
                    style = DiaryTypography.bodySmallBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(end = 52.dp)
                        .fillMaxWidth()
                )
            },
            onNavigationClick = onClickBack
        )

        Spacer(modifier = Modifier.height(8.dp))
        LinkContent(
            text = "서비스 이용약관",
            link = WebLink.Link.POLICY,
            onClick = navigateToWebLink
        )

        LinkContent(
            text = "개인정보 수집 / 이용 동의서",
            link = WebLink.Link.PRIVACY,
            onClick = navigateToWebLink
        )

    }
}

@Composable
private fun LinkContent(
    text: String,
    link: WebLink.Link,
    onClick: (WebLink.Link) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(link) }
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = text,
            color = DiaryColorsPalette.current.gray800,
            style = DiaryTypography.bodyMediumSemiBold,
            modifier = Modifier
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.icon_arrow_right_24),
            contentDescription = null,
            tint = Color(0xFF000000),
            modifier = Modifier
                .align(Alignment.CenterEnd)
        )
    }
}

@Preview
@Composable
private fun PolicyScreenPreview() {
    SsukssukDiaryTheme {
        PolicyScreen(
            onClickBack = {},
            navigateToWebLink = {}
        )
    }
}