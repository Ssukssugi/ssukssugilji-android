package com.sabo.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme

@Composable
internal fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToGallery: () -> Unit
) {
    HomeContent(
        modifier = modifier,
        navigateToGallery = navigateToGallery
    )
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    navigateToGallery: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DiaryColorsPalette.current.gray200)
    ) {
        WriteDiaryFAB(
            modifier = modifier,
            navigateToGallery = navigateToGallery
        )
    }
}

@Composable
private fun BoxScope.WriteDiaryFAB(
    modifier: Modifier = Modifier,
    navigateToGallery: () -> Unit = {}
) {

    Row(
        modifier = modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 16.dp)
            .background(DiaryColorsPalette.current.green400, CircleShape)
            .clip(CircleShape)
            .clickable { navigateToGallery() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(com.sabo.core.designsystem.R.drawable.icon_photo_camera_32),
            contentDescription = null,
            tint = DiaryColorsPalette.current.gray50,
            modifier = modifier.size(24.dp)
        )
        Spacer(modifier = modifier.width(6.dp))
        Text(
            text = "일지 작성하기",
            color = DiaryColorsPalette.current.gray50,
            style = DiaryTypography.bodySmallBold
        )
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    SsukssukDiaryTheme {
        HomeContent()
    }
}