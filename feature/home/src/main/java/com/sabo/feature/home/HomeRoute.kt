package com.sabo.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme

@Composable
internal fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeContent(
        modifier = modifier
    )
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DiaryColorsPalette.current.gray200)
    ) {
        HomeBottomBar(
            modifier = modifier
        )
    }
}

@Composable
private fun BoxScope.HomeBottomBar(
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(DiaryColorsPalette.current.gray900)
        .align(Alignment.BottomCenter)
    ) {
        Box(
            modifier = modifier
                .clip(shape = RoundedCornerShape(100.dp))
                .size(48.dp)
                .align(Alignment.Center)
                .background(DiaryColorsPalette.current.green500)
                .clickable {

                }
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