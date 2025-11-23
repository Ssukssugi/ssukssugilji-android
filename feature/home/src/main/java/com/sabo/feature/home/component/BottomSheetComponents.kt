package com.sabo.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.feature.home.PlantListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlantOptionsModalBottomSheet(
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
                onEditClick = {
                    onDismissRequest()
                    onEditClick()
                },
                onDeleteClick = {
                    onDismissRequest()
                    onDeleteClick()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserReportOptionsBottomSheet(
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
