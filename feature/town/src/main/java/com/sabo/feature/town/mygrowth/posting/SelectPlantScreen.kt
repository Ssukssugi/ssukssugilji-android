package com.sabo.feature.town.mygrowth.posting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.NavigationType
import com.sabo.core.designsystem.component.SsukssukTopAppBar
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.core.navigator.model.GrowthVariation
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun SelectPlantScreen(
    viewModel: SelectPlantViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    navigateToGrowthVariation: (GrowthVariation) -> Unit
) {

    val state = viewModel.collectAsState().value
    val listState = rememberLazyListState()

    viewModel.collectSideEffect {
        when (it) {
            is SelectPlantSideEffect.NavigateToGrowthVariation -> navigateToGrowthVariation(it.route)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
    ) {
        SsukssukTopAppBar(
            navigationType = NavigationType.CLOSE,
            onNavigationClick = onClickBack,
            containerColor = Color(0xFFFFFFFF)
        )

        when {
            state.isLoading -> LoadingContent(modifier = Modifier.weight(1f, fill = true))
            else -> {
                SelectPlantContent(
                    modifier = Modifier.weight(1f, fill = true),
                    listState = listState,
                    plantList = state.plantList,
                    onClickPlant = viewModel::selectPlant,
                    onClickComplete = viewModel::onClickComplete
                )
            }
        }

    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun SelectPlantContent(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    plantList: List<Plant> = emptyList(),
    onClickPlant: (Long) -> Unit = {},
    onClickComplete: () -> Unit = {}
) {

    val enableCompleteButton = remember(plantList) {
        plantList.any { it.isSelected }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item { Header() }
            items(
                items = plantList,
                key = { it.id }
            ) {
                PlantItem(
                    item = it,
                    onClick = onClickPlant
                )
            }
        }

        CompleteButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            enabled = enableCompleteButton,
            onClick = onClickComplete
        )
    }
}

@Composable
private fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "소개할 식물을 선택해주세요",
            color = DiaryColorsPalette.current.gray800,
            style = DiaryTypography.subtitleLargeBold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "일지를 2개 이상 작성한 식물만 소개할 수 있어요",
            color = DiaryColorsPalette.current.gray600,
            style = DiaryTypography.bodyLargeMedium
        )
    }
}

@Composable
private fun PlantItem(
    item: Plant,
    onClick: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = item.enabled) { onClick(item.id) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .then(
                    if (item.enabled.not()) Modifier.alpha(0.7f) else Modifier
                )
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f, fill = true)
        ) {
            Text(
                text = item.nickname,
                color = when {
                    item.isSelected -> DiaryColorsPalette.current.green400
                    item.enabled.not() -> DiaryColorsPalette.current.gray500
                    else -> DiaryColorsPalette.current.gray800
                },
                style = DiaryTypography.bodyLargeSemiBold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.category,
                color = if (item.enabled.not()) DiaryColorsPalette.current.gray400 else DiaryColorsPalette.current.gray600,
                style = DiaryTypography.bodyMediumRegular
            )
            if (item.enabled.not()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "작성한 일지가 2개 이상인 식물만 소개할 수 있어요",
                    color = DiaryColorsPalette.current.red400,
                    style = DiaryTypography.captionLargeMedium
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier.size(24.dp)
        ) {
            if (item.isSelected) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_check_24),
                    contentDescription = null,
                    tint = DiaryColorsPalette.current.green400,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
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
            text = "선택 완료",
            style = DiaryTypography.subtitleMediumBold,
            color = Color(0xFFFFFFFF),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectPlantContentPreview() {
    SsukssukDiaryTheme {
        SelectPlantContent(
            plantList = listOf(
                Plant(
                    id = 1,
                    nickname = "TEST",
                    category = "TESTEST",
                    image = "",
                    isSelected = true,
                    enabled = false
                )
            )
        )
    }
}