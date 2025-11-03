package com.sabo.feature.diary.detail

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.NavigationType
import com.sabo.core.designsystem.component.SsukssukTopAppBar
import com.sabo.core.designsystem.component.TopSnackBar
import com.sabo.core.designsystem.component.rememberSnackBarState
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.core.navigator.model.DiaryEdit
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs

@Composable
internal fun DiaryDetailScreen(
    viewModel: DiaryDetailViewModel = hiltViewModel(),
    onClickBack: () -> Unit = {},
    navigateToEditDiary: (DiaryEdit) -> Unit = {},
    popBackStackWithResult: () -> Unit = {}
) {
    val state = viewModel.collectAsState().value
    var snackBarState by rememberSnackBarState()

    viewModel.collectSideEffect {
        when (it) {
            is DiaryDetailUiEvent.NavigateToEditDiary -> navigateToEditDiary(it.route)
            DiaryDetailUiEvent.ShowDeleteDiarySnackBar -> {
                snackBarState = snackBarState.copy(isVisible = false)
                snackBarState = snackBarState.copy(
                    message = "삭제가 완료되었습니!",
                    iconRes = R.drawable.icon_circle_check,
                    isVisible = true
                )
            }

            DiaryDetailUiEvent.PopBackStack -> popBackStackWithResult()
        }
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    var showDiaryDeleteDialog by remember { mutableStateOf(false) }
    val lazyRowState = rememberLazyListState()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val selectedItemWidth = 40.dp
    val itemSpacing = 6.dp
    val sidePadding = (screenWidth - selectedItemWidth) / 2

    LaunchedEffect(state.selectedDiaryIndex, state.diaries.size) {
        if (state.diaries.isNotEmpty() && state.selectedDiaryIndex >= 0) {
            lazyRowState.scrollToItem(state.selectedDiaryIndex)
        }
    }

    LaunchedEffect(lazyRowState, state.selectedDiaryIndex) {
        snapshotFlow { lazyRowState.isScrollInProgress }
            .distinctUntilChanged()
            .filter { !it }
            .collect {
                val layoutInfo = lazyRowState.layoutInfo
                val screenCenter = layoutInfo.viewportSize.width / 2f
                val contentPaddingPx = layoutInfo.beforeContentPadding

                val itemDistances = layoutInfo.visibleItemsInfo.map { item ->
                    val itemVisualCenter = contentPaddingPx + item.offset + item.size / 2f
                    val distance = abs(itemVisualCenter - screenCenter)
                    item.index to distance
                }

                val centerIndex = itemDistances.minByOrNull { it.second }?.first

                if (centerIndex != null && centerIndex != state.selectedDiaryIndex) {
                    viewModel.onSelectDiary(centerIndex)
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        DiaryDetailTopAppBar(
            profileImage = state.profileImage,
            nickname = state.nickname,
            onClickBack = onClickBack,
            onClickMore = { showBottomSheet = true }
        )

        Box(
            modifier = Modifier
                .weight(1f, fill = true)
        ) {
            DiaryDetailContent(
                content = state.content
            )
        }

        if (state.diaries.isNotEmpty()) {
            LazyRow(
                state = lazyRowState,
                modifier = Modifier
                    .height(80.dp)
                    .background(color = Color.Transparent),
                contentPadding = PaddingValues(
                    vertical = 12.dp,
                    horizontal = sidePadding
                ),
                horizontalArrangement = Arrangement.spacedBy(itemSpacing)
            ) {
                itemsIndexed(
                    items = state.diaries,
                    key = { _, diary -> diary.diaryId }
                ) { index, diary ->
                    DiaryHistoryImage(
                        imageUrl = diary.image,
                        isSelected = index == state.selectedDiaryIndex,
                        onClick = {
                            viewModel.onSelectDiary(index)
                        }
                    )
                }
            }
        }
    }

    if (showBottomSheet) {
        DiaryOptionModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            onEditClick = viewModel::navigateToDiaryEdit,
            onDeleteClick = { showDiaryDeleteDialog = true }
        )
    }

    if (showDiaryDeleteDialog) {
        DiaryDeleteDialog(
            onDismiss = { showDiaryDeleteDialog = false },
            onConfirm = viewModel::deleteDiary
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
private fun DiaryDetailTopAppBar(
    profileImage: String = "",
    nickname: String = "",
    onClickBack: () -> Unit = {},
    onClickMore: () -> Unit = {}
) {
    SsukssukTopAppBar(
        navigationType = NavigationType.BACK,
        onNavigationClick = onClickBack,
        containerColor = Color.White,
        content = { modifier ->
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = profileImage,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .background(color = DiaryColorsPalette.current.gray500, shape = CircleShape)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = nickname,
                    color = DiaryColorsPalette.current.gray900,
                    style = DiaryTypography.bodySmallSemiBold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_more),
                    contentDescription = null,
                    tint = DiaryColorsPalette.current.gray500,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .clickable { onClickMore() }
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
    )
}

@Composable
private fun DiaryHistoryImage(
    imageUrl: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val imageWidth by animateDpAsState(
        targetValue = if (isSelected) 40.dp else 32.dp,
        label = "imageWidth"
    )

    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .width(imageWidth)
            .aspectRatio(3f / 5f)
            .background(
                color = if (isSelected) DiaryColorsPalette.current.gray900 else DiaryColorsPalette.current.gray500,
                shape = RoundedCornerShape(2.5.dp)
            )
            .clip(RoundedCornerShape(2.5.dp))
            .clickable { onClick() },
        contentScale = ContentScale.Crop
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiaryDetailContent(
    content: Content
) {
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 280.dp,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContainerColor = Color.White,
        sheetShadowElevation = 0.dp,
        sheetDragHandle = { },
        sheetContent = {
            DiaryInfoBottomSheet(content = content)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = (content as? Content.Success)?.image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = DiaryColorsPalette.current.gray100),
                    contentScale = ContentScale.Fit,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White)
            )
        }
    }
}

@Composable
private fun DiaryInfoBottomSheet(
    content: Content
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                (content as? Content.Success)?.careTypes?.forEach { care ->
                    Image(
                        painter = painterResource(care.iconRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .background(color = Color.Transparent, shape = CircleShape)
                    )
                }
            }

            val updatedAt = (content as? Content.Success)?.updatedAt
            val timeText = if (updatedAt != null) {
                val days = ChronoUnit.DAYS.between(LocalDate.parse(updatedAt, DateTimeFormatter.ISO_DATE), LocalDate.now())
                when {
                    days == 0L -> "오늘"
                    days == 1L -> "어제"
                    days < 7 -> "${days}일 전"
                    else -> updatedAt
                }
            } else {
                ""
            }
            Text(
                text = timeText,
                style = DiaryTypography.bodySmallRegular,
                color = DiaryColorsPalette.current.gray600
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = (content as? Content.Success)?.diary ?: "",
            style = DiaryTypography.bodyMediumRegular,
            color = DiaryColorsPalette.current.gray700
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiaryOptionModalBottomSheet(
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
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
        DiaryOptionBottomSheetContent(
            onEditClick = {
                onEditClick()
                onDismissRequest()
            },
            onDeleteClick = {
                onDeleteClick()
                onDismissRequest()
            }
        )
    }
}

@Composable
private fun DiaryOptionBottomSheetContent(
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        DiaryBottomSheetItem(
            iconRes = R.drawable.icon_edit_diary,
            text = "수정하기",
            tint = DiaryColorsPalette.current.gray600,
            onClick = onEditClick
        )
        DiaryBottomSheetItem(
            iconRes = R.drawable.icon_trash,
            text = "삭제하기",
            tint = DiaryColorsPalette.current.red400,
            onClick = onDeleteClick
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DiaryBottomSheetItem(
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
private fun DiaryDeleteDialog(
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
                text = "이 일지를 정말 삭제할까요?",
                style = DiaryTypography.subtitleLargeBold,
                color = DiaryColorsPalette.current.gray900,
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 8.dp)
            )
            Text(
                text = "삭제한 일지는 다시 복구할 수 없어요!\n신중하게 고민 후 삭제를 진행해주세요.",
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

@Preview
@Composable
private fun DiaryDetailTopAppBarPreview() {
    SsukssukDiaryTheme {
        DiaryDetailTopAppBar()
    }
}

@Preview
@Composable
private fun DiaryDetailContentPreview() {
    SsukssukDiaryTheme {
        DiaryDetailContent(
            content = Content.Loading
        )
    }
}
