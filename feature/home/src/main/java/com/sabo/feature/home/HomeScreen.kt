package com.sabo.feature.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.TopSnackBar
import com.sabo.core.designsystem.component.rememberSnackBarState
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.core.designsystem.toolkit.noRippleClickable
import com.sabo.core.mapper.DateMapper.toDisplayDayOfWeek
import com.sabo.core.model.PlantEnvironmentPlace
import com.sabo.core.navigator.model.PlantAddEdit
import kotlinx.coroutines.flow.distinctUntilChanged
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
    navigateToDiaryDetail: (Long, Long) -> Unit,
    navigateToPlantEdit: (PlantAddEdit.PlantEdit) -> Unit,
    navigateToMyGrowths: () -> Unit
) {

    val state = viewModel.collectAsState().value

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedPlant by remember { mutableStateOf<PlantListItem.Plant?>(null) }
    var showPlantDeleteDialog by remember { mutableStateOf(false) }

    var selectedGrowthId by remember { mutableStateOf<Long?>(null) }
    var showPostOptionBottomSheet by remember { mutableStateOf(false) }
    var showUserReportDialog by remember { mutableStateOf(false) }

    var snackBarState by rememberSnackBarState()

    viewModel.collectSideEffect {
        when (it) {
            is HomeEvent.NavigateToDiaryDetail -> navigateToDiaryDetail(it.plantId, it.diaryId)
            is HomeEvent.ShowPlantOptions -> {
                selectedPlant = it.plant
                showBottomSheet = true
            }

            is HomeEvent.NavigateToPlantEdit -> {
                showBottomSheet = false
                navigateToPlantEdit(it.route)
            }

            HomeEvent.ShowSnackBarDeletePlant -> {
                snackBarState = snackBarState.copy(isVisible = false)
                snackBarState = snackBarState.copy(
                    message = "삭제가 완료되었습니다!",
                    iconRes = R.drawable.icon_circle_check,
                    isVisible = true
                )
            }

            HomeEvent.ShowSnackBarReportGrowth -> {
                snackBarState = snackBarState.copy(isVisible = false)
                snackBarState = snackBarState.copy(
                    message = "게시글 신고가 완료되었어요!",
                    iconRes = R.drawable.icon_circle_check,
                    isVisible = true
                )
            }

            is HomeEvent.ShowPostOptions -> {
                selectedGrowthId = it.growthId
                showPostOptionBottomSheet = true
            }
        }
    }

    HomeContent(
        modifier = modifier,
        plantList = state.plantList,
        content = state.homeContent,
        navigateToGallery = navigateToGallery,
        navigateToPlantAdd = navigateToPlantAdd,
        navigateToProfile = navigateToProfile,
        onClickDiaryDetail = viewModel::onClickDiaryDetail,
        onClickMore = viewModel::onClickMore,
        onClickOtherPlant = viewModel::onSelectPlant,
        onClickTown = viewModel::onSelectTown,
        onLoadMoreTown = viewModel::loadMoreTownGrowth,
        onClickPostMore = viewModel::onClickGrowthPostMore,
        navigateToMyGrowths = navigateToMyGrowths
    )

    if (showBottomSheet) {
        PlantOptionsModalBottomSheet(
            plant = selectedPlant,
            onEditClick = viewModel::onEditPlant,
            onDeleteClick = { showPlantDeleteDialog = true },
            onDismissRequest = {
                showBottomSheet = false
                selectedPlant = null
            }
        )
    }

    if (showPlantDeleteDialog) {
        PlantDeleteDialog(
            onDismiss = { showPlantDeleteDialog = false },
            onConfirm = viewModel::onDeletePlant
        )
    }

    if (showPostOptionBottomSheet) {
        UserReportOptionsBottomSheet(
            onDismissRequest = {
                showPostOptionBottomSheet = false
                selectedGrowthId = null
            },
            onReportClick = { showUserReportDialog = true }
        )
    }

    if (showUserReportDialog) {
        UserReportDialog(
            onDismiss = {
                showUserReportDialog = false
                selectedGrowthId = null
            },
            onConfirm = {
                selectedGrowthId?.let { viewModel.reportGrowthPost(it) }
                showPostOptionBottomSheet = false
            }
        )
    }

    if (snackBarState.isVisible) {
        TopSnackBar(
            message = snackBarState.message,
            iconRes = snackBarState.iconRes,
            iconTint = snackBarState.iconTint,
            onDismiss = { snackBarState = snackBarState.copy(isVisible = false) }
        )
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    plantList: List<PlantListItem>,
    content: HomeContent,
    navigateToGallery: () -> Unit = {},
    navigateToPlantAdd: () -> Unit = {},
    navigateToProfile: () -> Unit = {},
    onClickDiaryDetail: (Long) -> Unit = {},
    onClickMore: (Long) -> Unit = {},
    onClickOtherPlant: (Long) -> Unit = {},
    onClickTown: () -> Unit = {},
    onLoadMoreTown: (Long) -> Unit = {},
    onClickPostMore: (Long) -> Unit = {},
    navigateToMyGrowths: () -> Unit = {}
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
                isTownSelected = content is HomeContent.Town,
                scrollState = storyRowState,
                onClickAddPlant = navigateToPlantAdd,
                onClickPlant = onClickOtherPlant,
                onClickTown = onClickTown
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = DiaryColorsPalette.current.gray500,
                thickness = 1.dp
            )

            when (content) {
                is HomeContent.Diary -> {
                    SelectedPlantContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = true),
                        scrollState = contentColumnState,
                        data = content.plantContent,
                        onClickDiaryDetail = onClickDiaryDetail,
                        onClickMore = onClickMore
                    )
                }

                is HomeContent.Town -> {
                    TownListContent(
                        state = content.townContent,
                        onLoadMore = onLoadMoreTown,
                        onClickPostMore = onClickPostMore,
                        onClickMyPost = navigateToMyGrowths
                    )
                }
            }
        }
        if (content is HomeContent.Diary) {
            WriteDiaryFAB(
                modifier = modifier,
                anyPlants = plantList.filterIsInstance<PlantListItem.Plant>().isNotEmpty(),
                navigateToGallery = navigateToGallery,
                navigateToAddPlant = navigateToPlantAdd
            )
        }
    }
}

@Composable
private fun PlantStory(
    plantList: List<PlantListItem>,
    isTownSelected: Boolean = false,
    scrollState: LazyListState = rememberLazyListState(),
    onClickPlant: (Long) -> Unit = {},
    onClickAddPlant: () -> Unit = {},
    onClickTown: () -> Unit = {}
) {
    LazyRow(
        state = scrollState,
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        item {
            TownButton(
                isSelected = isTownSelected,
                onClickTown = onClickTown
            )
        }

        item {
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .width(1.dp)
                    .height(40.dp)
                    .background(color = DiaryColorsPalette.current.gray400)
            )
            Spacer(modifier = Modifier.width(9.dp))
        }

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
private fun TownButton(
    isSelected: Boolean = false,
    onClickTown: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .noRippleClickable { onClickTown() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.img_logo_cat),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "쑥쑥마을",
            style = DiaryTypography.captionLargeBold,
            color = if (isSelected) DiaryColorsPalette.current.gray800 else DiaryColorsPalette.current.gray500
        )
    }
}

@Composable
private fun TownListContent(
    state: TownContent,
    onLoadMore: (Long) -> Unit = {},
    onClickMyPost: () -> Unit = {},
    onClickPostMore: (Long) -> Unit = {},
) {
    val listState = rememberLazyListState()
    var isLoadingMore by remember { mutableStateOf(false) }

    LaunchedEffect(listState, state.dataList.size) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.let {
                val totalItemsCount = listState.layoutInfo.totalItemsCount
                it.index >= totalItemsCount - 2
            } ?: false
        }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !isLoadingMore && !state.isLoading) {
                    val loadMoreItem = state.dataList.lastOrNull()
                    if (loadMoreItem is TownListItem.LoadMore) {
                        isLoadingMore = true
                        onLoadMore(loadMoreItem.lastId)
                    }
                }
            }
    }

    LaunchedEffect(state.dataList.size) {
        isLoadingMore = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Row(
                    modifier = Modifier
                        .padding(top = 9.dp, bottom = 9.dp, start = 24.dp, end = 20.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f, fill = true))
                    Row(
                        modifier = Modifier.clickable { onClickMyPost() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "나의 게시글",
                            style = DiaryTypography.bodySmallSemiBold,
                            color = DiaryColorsPalette.current.gray600,
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.icon_arrow_right_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp),
                            tint = DiaryColorsPalette.current.gray600
                        )
                    }
                }
            }

            if (state.isNewUser) {
                item {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 27.dp, vertical = 16.dp)
                            .fillMaxWidth()
                            .background(color = DiaryColorsPalette.current.gray100, shape = CircleShape)
                            .clip(CircleShape)
                            .clickable { }
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = "쑥쑥성장 소개하기",
                            style = DiaryTypography.bodyLargeBold,
                            color = DiaryColorsPalette.current.green400,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }
            }

            items(
                items = state.dataList,
                key = {
                    when (it) {
                        is TownListItem.Post -> it.id
                        is TownListItem.LoadMore -> "load_more_${it.lastId}"
                    }
                }
            ) { data ->
                when (data) {
                    is TownListItem.Post -> TownListItem(
                        data = data,
                        onClickGrowthItemMore = onClickPostMore
                    )

                    is TownListItem.LoadMore -> {
                        LoadingShimmerEffect {
                            TownListItemSkeleton()
                        }
                    }
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(56.dp)
            )
        }
    }
}

@Composable
private fun TownListItem(
    data: TownListItem.Post,
    onClickGrowth: (Long) -> Unit = {},
    onClickGrowthItemMore: (Long) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
            .background(color = DiaryColorsPalette.current.gray50, shape = RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = DiaryColorsPalette.current.gray200, shape = RoundedCornerShape(16.dp))
            .clip(shape = RoundedCornerShape(16.dp))
            .clickable { onClickGrowth(data.id) }
            .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AsyncImage(
                    model = data.profile,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = data.plantName,
                    style = DiaryTypography.bodyMediumSemiBold,
                    color = DiaryColorsPalette.current.gray600
                )
                Text(
                    text = data.nickName,
                    style = DiaryTypography.captionSmallMedium,
                    color = DiaryColorsPalette.current.gray500
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_more),
                    contentDescription = null,
                    tint = DiaryColorsPalette.current.gray500,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .clickable { onClickGrowthItemMore(data.id) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TownGrowthPlantImage(imageUrl = data.oldImage)
                    TownGrowthPlantImage(imageUrl = data.newImage)
                }

                Text(
                    text = "${data.dateDiff}일 뒤",
                    style = DiaryTypography.captionMediumBold,
                    color = DiaryColorsPalette.current.green400,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(bottom = 12.dp)
                        .background(color = Color(0xB3161E2D), shape = RoundedCornerShape(50.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
private fun RowScope.TownGrowthPlantImage(imageUrl: String) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .background(
                color = DiaryColorsPalette.current.gray200,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
    )
}

@Composable
fun LoadingShimmerEffect(content: @Composable (Brush) -> Unit) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    content(brush)
}

@Composable
private fun TownListItemSkeleton() {
    LoadingShimmerEffect { brush ->
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .fillMaxWidth()
                .background(color = DiaryColorsPalette.current.gray50, shape = RoundedCornerShape(16.dp))
                .border(width = 1.dp, color = DiaryColorsPalette.current.gray200, shape = RoundedCornerShape(16.dp))
                .clip(shape = RoundedCornerShape(16.dp))
                .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(brush)
                    )
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(brush)
                    )
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(brush)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(brush)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(brush)
                        )
                    }
                }
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
    onClickDiaryDetail: (Long) -> Unit = {}
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
                        place = data.place,
                        name = data.name,
                        category = data.category,
                        onClickMore = onClickMore
                    )
                }

                data.historyList.map {
                    plantMonthlyHistory(
                        data = it,
                        onClickDiaryDetail = onClickDiaryDetail
                    )
                }
            }
        }
    }
}

@Composable
private fun PlantInfoMain(
    id: Long,
    image: String?,
    place: PlantEnvironmentPlace,
    name: String,
    category: String,
    onClickMore: (Long) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = DiaryColorsPalette.current.gray50)
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
                    text = "${place.display}에서 무럭무럭 자라는 중!",
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
    data: PlantHistory,
    onClickDiaryDetail: (Long) -> Unit = {}
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
        DiaryItem(
            diary = it,
            onClickDiaryDetail = onClickDiaryDetail
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DiaryItem(
    diary: Diary,
    onClickDiaryDetail: (Long) -> Unit = {}
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
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClickDiaryDetail(diary.id) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserReportOptionsBottomSheet(
    onDismissRequest: () -> Unit,
    onReportClick: () -> Unit
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFFFFFFF))
                .padding(vertical = 24.dp)
        ) {
            PlantBottomSheetEditItem(
                iconRes = R.drawable.icon_trash,
                text = "신고하기",
                tint = DiaryColorsPalette.current.red400,
                onClick = { onReportClick() }
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
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(24.dp)
                    .background(color = Color(0xFFFFFFFF), shape = CircleShape)
                    .clip(CircleShape)
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

@Composable
private fun PlantDeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icon_notice_triangle),
                contentDescription = null,
                tint = DiaryColorsPalette.current.red400,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "이 식물을 정말 삭제할까요?",
                style = DiaryTypography.subtitleLargeBold,
                color = DiaryColorsPalette.current.gray900,
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 8.dp)
            )
            Text(
                text = "삭제시 등록한 식물정보와\n지금까지 작성한 일지가 모두 사라져요.",
                style = DiaryTypography.bodyLargeMedium,
                color = DiaryColorsPalette.current.gray600
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DiaryColorsPalette.current.green50)
                        .clickable {
                            onConfirm()
                            onDismiss()
                        }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "삭제하기",
                        color = DiaryColorsPalette.current.green600,
                        style = DiaryTypography.subtitleMediumBold
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DiaryColorsPalette.current.green400)
                        .clickable { onDismiss() }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "돌아가기",
                        color = DiaryColorsPalette.current.gray50,
                        style = DiaryTypography.subtitleMediumBold
                    )
                }
            }
        }
    }
}

@Composable
private fun UserReportDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icon_notice_triangle),
                contentDescription = null,
                tint = DiaryColorsPalette.current.red400,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "이 게시글을 정말 신고할까요?",
                style = DiaryTypography.subtitleLargeBold,
                color = DiaryColorsPalette.current.gray900,
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 8.dp)
            )
            Text(
                text = "신고 접수 시, 운영자의 검토 후\n게시글이 처리될 예정입니다.",
                style = DiaryTypography.bodyLargeMedium,
                color = DiaryColorsPalette.current.gray600
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DiaryColorsPalette.current.green50)
                        .clickable {
                            onConfirm()
                            onDismiss()
                        }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "신고하기",
                        color = DiaryColorsPalette.current.green600,
                        style = DiaryTypography.subtitleMediumBold
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DiaryColorsPalette.current.green400)
                        .clickable { onDismiss() }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "돌아가기",
                        color = DiaryColorsPalette.current.gray50,
                        style = DiaryTypography.subtitleMediumBold
                    )
                }
            }
        }
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
            content = HomeContent.Diary(
                PlantContent.PlantInfo(
                    id = -1,
                    place = PlantEnvironmentPlace.ROOM,
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
                )
            ),
            onClickMore = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TownContentPreview() {
    SsukssukDiaryTheme {
        TownListContent(
            state = TownContent(
                isLoading = true,
                dataList = emptyList(),
                isNewUser = true
            )
        )
    }
}

@Preview
@Composable
private fun TownContentItemPreview() {
    SsukssukDiaryTheme {
        TownListItem(
            data = TownListItem.Post(
                id = 1,
                profile = "test",
                plantName = "Plant Name",
                nickName = "Nick Name",
                oldImage = "",
                newImage = "",
                dateDiff = 6
            )
        )
    }
}

@Preview
@Composable
private fun TownListItemSkeletonPreview() {
    SsukssukDiaryTheme {
        TownListItemSkeleton()
    }
}