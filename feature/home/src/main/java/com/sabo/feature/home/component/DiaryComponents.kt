package com.sabo.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.mapper.DateMapper.toDisplayDayOfWeek
import com.sabo.core.model.PlantEnvironmentPlace
import com.sabo.feature.home.CareType
import com.sabo.feature.home.Diary
import com.sabo.feature.home.PlantContent
import com.sabo.feature.home.PlantHistory

@Composable
internal fun SelectedPlantContent(
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
                state = scrollState,
                contentPadding = PaddingValues(bottom = 80.dp)
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

internal fun LazyListScope.plantMonthlyHistory(
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
                        diary.cares.forEach { CareItem(type = it) }
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
