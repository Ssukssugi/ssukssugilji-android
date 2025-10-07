package com.sabo.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.core.designsystem.toolkit.noRippleClickable
import com.sabo.core.mapper.DateMapper.toDisplayDayOfWeek
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDate

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToGallery: () -> Unit,
    navigateToPlantAdd: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToDiaryDetail: (Long) -> Unit
) {

    val state = viewModel.collectAsState().value

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedPlant by remember { mutableStateOf<PlantListItem.Plant?>(null) }

    viewModel.collectSideEffect {
        when (it) {
            is HomeEvent.NavigateToDiaryDetail -> navigateToDiaryDetail(it.plantId)
            is HomeEvent.ShowPlantOptions -> {
                selectedPlant = it.plant
                showBottomSheet = true
            }
        }
    }

    HomeContent(
        modifier = modifier,
        plantList = state.plantList,
        plantContent = state.plantContent,
        navigateToGallery = navigateToGallery,
        navigateToPlantAdd = navigateToPlantAdd,
        navigateToProfile = navigateToProfile,
        onClickDiaryDetail = viewModel::onClickDiaryDetail,
        onClickMore = viewModel::onClickMore,
        onClickOtherPlant = viewModel::onSelectPlant
    )

    if (showBottomSheet) {
        PlantOptionsModalBottomSheet(
            plant = selectedPlant,
            onEditClick = viewModel::onEditPlant,
            onDeleteClick = viewModel::onDeletePlant,
            onDismissRequest = {
                showBottomSheet = false
                selectedPlant = null
            }
        )
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    plantList: List<PlantListItem>,
    plantContent: PlantContent,
    navigateToGallery: () -> Unit = {},
    navigateToPlantAdd: () -> Unit = {},
    navigateToProfile: () -> Unit = {},
    onClickDiaryDetail: () -> Unit = {},
    onClickMore: (Long) -> Unit = {},
    onClickOtherPlant: (Long) -> Unit = {}
) {
    val storyRowState = rememberLazyListState()
    val contentColumnState = rememberLazyListState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .background(Color.White)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.img_home_logo),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_account),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .clickable { navigateToProfile() },
                    tint = DiaryColorsPalette.current.gray900
                )
            }

            PlantStory(
                plantList = plantList,
                scrollState = storyRowState,
                onClickAddPlant = navigateToPlantAdd,
                onClickPlant = onClickOtherPlant
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = DiaryColorsPalette.current.gray500,
                thickness = 1.dp
            )
            SelectedPlantContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true),
                scrollState = contentColumnState,
                data = plantContent,
                onClickDiaryDetail = onClickDiaryDetail,
                onClickMore = onClickMore
            )
        }
        WriteDiaryFAB(
            modifier = modifier,
            anyPlants = plantList.filterIsInstance<PlantListItem.Plant>().isNotEmpty(),
            navigateToGallery = navigateToGallery,
            navigateToAddPlant = navigateToPlantAdd
        )
    }
}

@Composable
private fun PlantStory(
    plantList: List<PlantListItem>,
    scrollState: LazyListState = rememberLazyListState(),
    onClickPlant: (Long) -> Unit = {},
    onClickAddPlant: () -> Unit = {}
) {
    LazyRow(
        state = scrollState,
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        items(
            items = plantList,
            key = {
                when (it) {
                    PlantListItem.AddPlant -> it.javaClass.name
                    is PlantListItem.Plant -> it.id
                }
            }
        ) {
            when (it) {
                PlantListItem.AddPlant -> AddPlantItem(onClickItem = onClickAddPlant)
                is PlantListItem.Plant -> PlantStoryItem(data = it, onClickItem = onClickPlant)
            }
        }
    }
}

@Composable
private fun SelectedPlantContent(
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
    data: PlantContent,
    onClickMore: (Long) -> Unit = {},
    onClickDiaryDetail: () -> Unit = {}
) {
    when (data) {
        PlantContent.Loading -> {
            Box(
                modifier = modifier
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(58.dp),
                    color = DiaryColorsPalette.current.green400
                )
            }
        }

        PlantContent.Empty -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = DiaryColorsPalette.current.gray700)
            )
        }

        is PlantContent.PlantInfo -> {
            LazyColumn(
                modifier = modifier,
                state = scrollState
            ) {
                item {
                    PlantInfoMain(
                        id = data.id,
                        image = data.image,
                        title = data.title,
                        name = data.name,
                        category = data.category,
                        onClickMore = onClickMore,
                        onClickDetail = onClickDiaryDetail
                    )
                }

                data.historyList.map {
                    plantMonthlyHistory(it)
                }
            }
        }
    }
}

@Composable
private fun PlantInfoMain(
    id: Long,
    image: String?,
    title: String,
    name: String,
    category: String,
    onClickMore: (Long) -> Unit = {},
    onClickDetail: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = DiaryColorsPalette.current.gray50)
            .clickable { onClickDetail() }
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(80.dp)
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color = DiaryColorsPalette.current.gray500, shape = CircleShape)
                        .size(80.dp)
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.BottomEnd)
                        .background(color = DiaryColorsPalette.current.red400, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.icon_temperature),
                        contentDescription = null,
                        tint = DiaryColorsPalette.current.gray50,
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = DiaryTypography.bodySmallRegular,
                    color = DiaryColorsPalette.current.gray600
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = name,
                    style = DiaryTypography.subtitleLargeBold,
                    color = DiaryColorsPalette.current.gray900
                )
                Text(
                    text = category,
                    style = DiaryTypography.bodyMediumRegular,
                    color = DiaryColorsPalette.current.gray700
                )
            }
        }

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.icon_more),
            contentDescription = null,
            tint = DiaryColorsPalette.current.gray500,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .clickable { onClickMore(id) }
        )
    }
}

private fun LazyListScope.plantMonthlyHistory(
    data: PlantHistory
) {
    item {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "${data.year}년 ${data.month}월",
            style = DiaryTypography.captionMediumRegular,
            color = DiaryColorsPalette.current.gray500,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillParentMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    items(
        items = data.diaryList,
        key = { it.id }
    ) {
        DiaryItem(it)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DiaryItem(
    diary: Diary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.width(52.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(color = DiaryColorsPalette.current.gray100, shape = CircleShape)
            ) {
                Text(
                    text = "${diary.date.dayOfMonth}일",
                    style = DiaryTypography.bodySmallSemiBold,
                    color = DiaryColorsPalette.current.gray700,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = diary.date.toDisplayDayOfWeek(),
                color = DiaryColorsPalette.current.gray600,
                style = DiaryTypography.captionSmallMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f, fill = true)
                .background(color = DiaryColorsPalette.current.gray50, shape = RoundedCornerShape(12.dp))
                .border(width = 1.dp, color = DiaryColorsPalette.current.gray200, shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                AsyncImage(
                    model = diary.image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .background(shape = RoundedCornerShape(6.dp), color = DiaryColorsPalette.current.gray50)
                        .clip(RoundedCornerShape(6.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier
                        .weight(1f, fill = true)
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        diary.cares.map { CareItem(type = it) }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = diary.content,
                        style = DiaryTypography.captionMediumRegular,
                        color = DiaryColorsPalette.current.gray600,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun CareItem(
    type: CareType
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(color = DiaryColorsPalette.current.gray500, shape = CircleShape)
    ) {
        Image(
            painter = painterResource(type.resId),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun AddPlantItem(
    onClickItem: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .size(68.dp)
            .noRippleClickable { onClickItem() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color = Color(0xFF161E2D).copy(alpha = 0.16f), shape = CircleShape)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icon_add_plant),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "식물 추가",
            style = DiaryTypography.captionMediumRegular,
            color = DiaryColorsPalette.current.gray600,
            modifier = Modifier.width(68.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PlantStoryItem(
    data: PlantListItem.Plant,
    onClickItem: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .size(68.dp)
            .noRippleClickable { onClickItem(data.id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = data.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(shape = CircleShape, color = Color(0xFF161E2D).copy(alpha = 0.16f))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = data.name,
            style = if (data.isSelected) DiaryTypography.captionMediumBold else DiaryTypography.captionMediumRegular,
            color = if (data.isSelected) DiaryColorsPalette.current.green600 else DiaryColorsPalette.current.gray600,
            modifier = Modifier.width(68.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BoxScope.WriteDiaryFAB(
    modifier: Modifier = Modifier,
    anyPlants: Boolean,
    navigateToGallery: () -> Unit = {},
    navigateToAddPlant: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 16.dp)
            .height(56.dp)
            .background(Color(0xFF00BA55).copy(alpha = 0.88f), CircleShape)
            .clip(CircleShape)
            .clickable {
                if (anyPlants) {
                    navigateToGallery()
                } else {
                    navigateToAddPlant()
                }
            }
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = R.drawable.img_logo_cat,
            contentDescription = null,
            modifier = modifier.size(40.dp)
        )
        Spacer(modifier = modifier.width(4.dp))
        Text(
            text = if (anyPlants) "쑥쑥일지 작성하기" else "식물 추가하기",
            color = Color(0xFFFFFFFF),
            style = DiaryTypography.bodyLargeBold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlantOptionsModalBottomSheet(
    plant: PlantListItem.Plant?,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(width = 56.dp, height = 4.dp)
                    .background(
                        color = Color(0xFFDDDDDD),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        },
        containerColor = Color(0xFFFFFFFF)
    ) {
        plant?.let {
            PlantOptionsBottomSheet(
                plant = it,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
private fun PlantOptionsBottomSheet(
    plant: PlantListItem.Plant,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = plant.image,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .background(color = Color(0xFFFFFFFF), shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = plant.name,
                style = DiaryTypography.subtitleMediumBold,
                color = DiaryColorsPalette.current.gray900
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            PlantBottomSheetEditItem(
                iconRes = R.drawable.icon_edit_diary,
                text = "수정하기",
                tint = DiaryColorsPalette.current.gray600,
                onClick = onEditClick
            )
            PlantBottomSheetEditItem(
                iconRes = R.drawable.icon_trash,
                text = "삭제하기",
                tint = DiaryColorsPalette.current.red400,
                onClick = onDeleteClick
            )
        }
    }
}

@Composable
private fun PlantBottomSheetEditItem(
    iconRes: Int,
    text: String,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = DiaryTypography.bodyLargeSemiBold,
            color = tint
        )
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    SsukssukDiaryTheme {
        HomeContent(
            plantList = listOf(
                PlantListItem.AddPlant, PlantListItem.Plant(-1, "name", "image", true)
            ),
            plantContent = PlantContent.PlantInfo(
                id = -1,
                title = "title",
                name = "name",
                category = "category",
                image = "image",
                shine = null,
                historyList = listOf(
                    PlantHistory(
                        year = 2025,
                        month = 6,
                        diaryList = listOf(
                            Diary(
                                id = -1,
                                date = LocalDate.of(2025, 6, 25),
                                content = "content",
                                image = "image",
                                cares = listOf(CareType.WATER, CareType.DIVIDING)
                            )
                        )
                    )
                )
            ),
            onClickMore = { }
        )
    }
}