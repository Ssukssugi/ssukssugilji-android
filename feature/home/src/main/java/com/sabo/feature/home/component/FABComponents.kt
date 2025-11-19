package com.sabo.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryTypography

@Composable
internal fun BoxScope.WriteDiaryFAB(
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
