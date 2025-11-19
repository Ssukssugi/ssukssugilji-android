package com.sabo.feature.town.mygrowth.variation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.component.NavigationType
import com.sabo.core.designsystem.component.SsukssukTopAppBar
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.feature.town.mygrowth.variation.component.DefaultContent
import com.sabo.feature.town.mygrowth.variation.component.VariationImageCard
import com.sabo.feature.town.mygrowth.variation.component.VariationImageType
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun VariationScreen(
    viewModel: VariationViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onNavigateToHomeWithSuccess: () -> Unit
) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is VariationSideEffect.NavigateToHomeWithSuccess -> {
                onNavigateToHomeWithSuccess()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
    ) {
        SsukssukTopAppBar(
            navigationType = NavigationType.CLOSE,
            containerColor = Color(0xFFFFFFFF),
            content = {
                Text(
                    text = "${state.plantNickname} 소개하기",
                    color = DiaryColorsPalette.current.gray900,
                    style = DiaryTypography.bodyMediumBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f, fill = true)
                )
                Spacer(modifier = Modifier.width(50.dp))
            },
            onNavigationClick = onClickBack
        )

        VariationContent(
            modifier = Modifier.weight(1f, fill = true),
            state = state,
            onImageClick = { historyImage ->
                viewModel.onImageClick(historyImage)
            },
            onCompleteClick = viewModel::requestToSaveGrowth
        )
    }
}

@Composable
private fun VariationContent(
    modifier: Modifier = Modifier,
    state: VariationState,
    onImageClick: (HistoryImage) -> Unit = {},
    onCompleteClick: () -> Unit = {}
) {
    val isBeforeEnabled = remember(state.beforeImage) {
        state.beforeImage == null
    }
    val isAfterEnabled = remember(state.beforeImage, state.afterImage) {
        state.beforeImage != null && state.afterImage == null
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp, alignment = Alignment.CenterHorizontally)
        ) {
            VariationImageCard(
                imageUrl = state.beforeImage?.url,
                type = VariationImageType.BEFORE,
                isEnabled = isBeforeEnabled
            ) {
                DefaultContent()
            }

            VariationImageCard(
                imageUrl = state.afterImage?.url,
                type = VariationImageType.AFTER,
                isEnabled = isAfterEnabled
            ) {
                DefaultContent()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            modifier = Modifier.weight(1f, fill = true),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(
                items = state.imageList,
                key = { it.url }
            ) { historyImage ->
                HistoryImageItem(
                    historyImage = historyImage,
                    isBeforeBadgeActive = state.beforeImage != null && state.afterImage == null,
                    isAfterBadgeActive = state.afterImage != null,
                    onClick = { onImageClick(historyImage) }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFFFFFFF))
        ) {
            CompleteButton(
                onClick = onCompleteClick,
                enabled = state.beforeImage != null && state.afterImage != null
            )
        }
    }
}

@Composable
private fun HistoryImageItem(
    historyImage: HistoryImage,
    isBeforeBadgeActive: Boolean,
    isAfterBadgeActive: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(0.75f)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = historyImage.url,
            contentDescription = "History image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        historyImage.selectedType?.let { type ->
            val labelText = when (type) {
                VariationImageType.BEFORE -> "Before"
                VariationImageType.AFTER -> "After"
            }

            val labelColor = when (type) {
                VariationImageType.BEFORE -> {
                    if (isBeforeBadgeActive) DiaryColorsPalette.current.green400
                    else DiaryColorsPalette.current.gray500
                }
                VariationImageType.AFTER -> {
                    if (isAfterBadgeActive) DiaryColorsPalette.current.green400
                    else DiaryColorsPalette.current.gray500
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFF000000).copy(alpha = 0.5f))
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(labelColor)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = labelText,
                    style = DiaryTypography.captionMediumRegular,
                    color = DiaryColorsPalette.current.gray50
                )
            }
        }
    }
}

@Composable
private fun CompleteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = false
) {
    Box(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .fillMaxWidth()
            .background(
                color = if (enabled) DiaryColorsPalette.current.green400 else DiaryColorsPalette.current.green100,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(enabled = enabled) { onClick() }
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "소개 완료",
            style = DiaryTypography.subtitleMediumBold,
            color = Color(0xFFFFFFFF),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun VariationContentPreview() {
    SsukssukDiaryTheme {
        VariationContent(
            state = VariationState(
                isLoading = false,
                plantNickname = "식물 명일이",
                beforeImage = HistoryImage(url = "https://example.com/1.jpg", selectedType = VariationImageType.BEFORE, diaryId = 0),
                afterImage = null,
                imageList = listOf(
                    HistoryImage(url = "https://example.com/1.jpg", selectedType = VariationImageType.BEFORE, diaryId = 0),
                    HistoryImage(url = "https://example.com/2.jpg", selectedType = null, diaryId = 1),
                    HistoryImage(url = "https://example.com/3.jpg", selectedType = null, diaryId = 2),
                    HistoryImage(url = "https://example.com/4.jpg", selectedType = VariationImageType.AFTER, diaryId = 3),
                    HistoryImage(url = "https://example.com/5.jpg", selectedType = null, diaryId = 4),
                    HistoryImage(url = "https://example.com/6.jpg", selectedType = null, diaryId = 5),
                )
            )
        )
    }
}
