package com.sabo.feature.town.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.core.designsystem.toolkit.noRippleClickable
import com.sabo.core.designsystem.toolkit.shimmer
import com.sabo.feature.town.TownContent
import com.sabo.feature.town.TownListItem
import com.sabo.feature.town.TownTab
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.roundToInt

@Composable
internal fun TownListContent(
    state: TownContent,
    selectedTab: TownTab,
    onTabSelected: (TownTab) -> Unit = {},
    onLoadMore: (Long) -> Unit = {},
    onClickPostMore: (Long) -> Unit = {},
) {
    val listState = rememberLazyListState()
    var isLoadingMore by remember { mutableStateOf(false) }

    val density = LocalDensity.current
    var tabHeaderHeightPx by remember { mutableFloatStateOf(0f) }
    var tabHeaderOffset by remember { mutableFloatStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = tabHeaderOffset + delta
                tabHeaderOffset = newOffset.coerceIn(-tabHeaderHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

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

    val tabHeaderHeightDp = with(density) { tabHeaderHeightPx.toDp() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .nestedScroll(nestedScrollConnection)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(top = tabHeaderHeightDp)
        ) {
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

            if (state.isLoading) {
                items(
                    count = 3
                ) {
                    TownListItemSkeleton()
                }
            } else {
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
                            TownListItemSkeleton()
                        }
                    }
                }
            }
        }

        TownTabHeader(
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            offsetY = tabHeaderOffset,
            onHeightMeasured = { tabHeaderHeightPx = it }
        )
    }
}

@Composable
private fun TownTabHeader(
    selectedTab: TownTab,
    onTabSelected: (TownTab) -> Unit,
    offsetY: Float,
    onHeightMeasured: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(0, offsetY.roundToInt()) }
            .onGloballyPositioned { coordinates ->
                onHeightMeasured(coordinates.size.height.toFloat())
            }
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TownTab(
            text = "전체",
            isSelected = selectedTab == TownTab.ALL,
            onClick = { onTabSelected(TownTab.ALL) }
        )
        TownTab(
            text = "나의 게시글",
            isSelected = selectedTab == TownTab.MY_POSTS,
            onClick = { onTabSelected(TownTab.MY_POSTS) }
        )
    }
}

@Composable
private fun TownTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = text,
        style = DiaryTypography.subtitleMediumBold,
        color = if (isSelected) DiaryColorsPalette.current.green400 else DiaryColorsPalette.current.gray500,
        modifier = Modifier.noRippleClickable { onClick() }
    )
}

@Composable
private fun TownListItem(
    data: TownListItem.Post,
    onClickGrowthItemMore: (Long) -> Unit = {}
) {
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
internal fun TownListItemSkeleton() {
    val shape16 = remember { RoundedCornerShape(16.dp) }
    val shape8 = remember { RoundedCornerShape(8.dp) }
    val shape4 = remember { RoundedCornerShape(4.dp) }

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
            .background(color = DiaryColorsPalette.current.gray50, shape = shape16)
            .border(width = 1.dp, color = DiaryColorsPalette.current.gray200, shape = shape16)
            .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SkeletonHeader(shape4)
            Spacer(modifier = Modifier.height(12.dp))
            SkeletonImages(shape8)
        }
    }
}

@Composable
private fun SkeletonHeader(shape: Shape) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .shimmer()
        )
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(16.dp)
                .clip(shape)
                .shimmer()
        )
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(14.dp)
                .clip(shape)
                .shimmer()
        )
    }
}

@Composable
private fun SkeletonImages(shape: Shape) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .clip(shape)
                .shimmer()
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .clip(shape)
                .shimmer()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TownTabPreview() {
    SsukssukDiaryTheme {
        TownListContent(
            state = TownContent(
                isLoading = false,
                dataList = emptyList(),
                isNewUser = false
            ),
            selectedTab = TownTab.ALL
        )
    }
}