package com.sabo.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.toolkit.noRippleClickable
import com.sabo.feature.home.PlantListItem

@Composable
internal fun PlantStory(
    plantList: List<PlantListItem>,
    scrollState: LazyListState = rememberLazyListState(),
    onClickPlant: (Long) -> Unit = {},
    onClickAddPlant: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxWidth()
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
        )
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
