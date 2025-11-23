package com.sabo.feature.town.mygrowth.main

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.NavigationType
import com.sabo.core.designsystem.component.SsukssukTopAppBar
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun MyGrowthScreen(
    viewModel: MyGrowthViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onClickPosting: () -> Unit
) {

    val state = viewModel.collectAsState().value
    var showGrowthMoreBottomSheet by remember { mutableStateOf<Long?>(null) }
    var showGrowthDeleteDialog by remember { mutableStateOf<Long?>(null) }

    viewModel.collectSideEffect {
        when (it) {
            is MyGrowthSideEffect.ShowDeleteDialog -> {
                showGrowthDeleteDialog = it.growthId
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
    ) {
        MyGrowthTopAppBar(
            onNavigationClick = onClickBack
        )
        MyGrowthContent(
            modifier = Modifier.weight(1f, fill = true),
            state = state,
            onClickPosting = onClickPosting,
            onClickGrowthItemMore = { showGrowthMoreBottomSheet = it }
        )
    }

    showGrowthMoreBottomSheet?.let { growthId ->
        GrowthDeleteBottomSheet(
            selectedGrowthId = growthId,
            onDismissRequest = { showGrowthMoreBottomSheet = null },
            onClickDelete = viewModel::showGrowthDeleteDialog
        )
    }

    showGrowthDeleteDialog?.let { growthId ->
        GrowthDeleteConfirmDialog(
            selectedGrowthId = growthId,
            onDismiss = { showGrowthDeleteDialog = null },
            onConfirm = viewModel::deleteGrowth
        )
    }
}

@Composable
private fun MyGrowthContent(
    modifier: Modifier = Modifier,
    state: MyGrowthState,
    onClickPosting: () -> Unit = {},
    onClickGrowthItemMore: (Long) -> Unit = {}
) {
    val listState = rememberLazyListState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.Center)
                )
            }

            state.isEmpty -> {
                EmptyContent()
            }

            else -> {
                MyGrowthList(
                    modifier = modifier,
                    listState = listState,
                    items = state.growthList,
                    onClickGrowthItemMore = onClickGrowthItemMore
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF).copy(alpha = 0f),
                            Color(0xFFFFFFFF)
                        )
                    )
                )
        ) {
            PostingButton(
                onClick = onClickPosting
            )
        }
    }
}

@Composable
private fun MyGrowthList(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    items: List<Growth>,
    onClickGrowthItemMore: (Long) -> Unit = {}
) {
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(
            items = items,
            key = { it.id }
        ) {
            GrowthListItem(
                data = it,
                onClickGrowthItemMore = onClickGrowthItemMore
            )
        }
    }
}

@Composable
private fun GrowthListItem(
    data: Growth,
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
                    GrowthPlantImage(imageUrl = data.oldImage)
                    GrowthPlantImage(imageUrl = data.newImage)
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
private fun RowScope.GrowthPlantImage(imageUrl: String) {
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
private fun EmptyContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "아직 소개한 쑥쑥이가 없어요.",
            color = Color(0xFF333333),
            style = DiaryTypography.subtitleLargeSemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "아래 버튼을 눌러,\n마을에 쑥쑥이를 소개해주세요 :)",
            color = Color(0xFF777777),
            style = DiaryTypography.bodyLargeMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(3f))
    }
}

@Composable
private fun PostingButton(
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
            .background(
                color = DiaryColorsPalette.current.green400,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "쑥쑥성장 소개하기",
            style = DiaryTypography.subtitleMediumBold,
            color = Color(0xFFFFFFFF),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun MyGrowthTopAppBar(
    onNavigationClick: () -> Unit = {}
) {
    SsukssukTopAppBar(
        navigationType = NavigationType.BACK,
        containerColor = Color(0xFFFFFFFF),
        contentColor = DiaryColorsPalette.current.gray900,
        onNavigationClick = onNavigationClick,
        content = {
            Text(
                text = "나의 게시글",
                color = DiaryColorsPalette.current.gray900,
                style = DiaryTypography.bodyMediumBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f, fill = true)
            )
            Spacer(modifier = Modifier.width(50.dp))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GrowthDeleteBottomSheet(
    selectedGrowthId: Long,
    onClickDelete: (Long) -> Unit,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .clickable {
                    onClickDelete(selectedGrowthId)
                    onDismissRequest()
                }
                .padding(vertical = 12.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icon_trash),
                contentDescription = null,
                tint = DiaryColorsPalette.current.red400,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "삭제하기",
                style = DiaryTypography.bodyLargeSemiBold,
                color = DiaryColorsPalette.current.red500
            )
        }
    }
}

@Composable
private fun GrowthDeleteConfirmDialog(
    selectedGrowthId: Long,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(top = 40.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icon_notice_triangle),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = DiaryColorsPalette.current.red400
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "이 게시글을 정말 삭제할까요?",
                color = DiaryColorsPalette.current.gray900,
                style = DiaryTypography.subtitleLargeBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "삭제한 게시글을 다시 복구할 수 없어요!\n신중하게 고민 후 삭제를 진행해주세요.",
                color = DiaryColorsPalette.current.gray600,
                style = DiaryTypography.bodyLargeMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = DiaryColorsPalette.current.green50)
                        .clickable {
                            onConfirm(selectedGrowthId)
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
                        color = Color.White,
                        style = DiaryTypography.subtitleMediumBold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MyGrowthTopAppBarPreview() {
    SsukssukDiaryTheme {
        MyGrowthTopAppBar()
    }
}

@Preview
@Composable
private fun MyGrowthContentPreview() {
    SsukssukDiaryTheme {
        MyGrowthContent(
            state = MyGrowthState(isLoading = true)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyContentPreview() {
    SsukssukDiaryTheme {
        EmptyContent()
    }

}