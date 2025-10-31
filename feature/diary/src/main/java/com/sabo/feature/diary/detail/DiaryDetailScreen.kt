package com.sabo.feature.diary.detail

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
internal fun DiaryDetailScreen(
    viewModel: DiaryDetailViewModel = hiltViewModel(),
    onClickBack: () -> Unit = {}
) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect {

    }

    val lazyRowState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        DiaryDetailTopAppBar(
            profileImage = state.profileImage,
            nickname = state.nickname,
            onClickBack = onClickBack
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
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(
                    items = state.diaries,
                    key = { _, diary -> diary.diaryId }
                ) { index, diary ->
                    DiaryHistoryImage(
                        imageUrl = diary.image,
                        isSelected = index == state.selectedDiaryIndex,
                        onClick = { viewModel.onSelectDiary(index) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DiaryDetailTopAppBar(
    profileImage: String = "",
    nickname: String = "",
    onClickBack: () -> Unit = {}
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
                    modifier = Modifier.size(24.dp)
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
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .width(40.dp)
            .aspectRatio(3f / 5f)
            .background(
                color = if (isSelected) DiaryColorsPalette.current.gray900 else DiaryColorsPalette.current.gray500,
                shape = RoundedCornerShape(2.5.dp)
            )
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
        sheetDragHandle = {  },
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
