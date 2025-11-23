package com.sabo.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.toolkit.shimmer
import com.sabo.feature.home.TownContent
import com.sabo.feature.home.TownListItem
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
internal fun TownListContent(
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
                MyGrowthButton(onClickMyPost = onClickMyPost)
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
    }
}

@Composable
private fun MyGrowthButton(
    onClickMyPost: () -> Unit = {}
) {
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
