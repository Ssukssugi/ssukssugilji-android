package com.sabo.feature.signup.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.feature.signup.model.AgeChip
import com.sabo.feature.signup.model.InfoChip

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectChipGroup(
    modifier: Modifier = Modifier,
    items: List<InfoChip>,
    onClick: (InfoChip) -> Unit = {}
) {
    FlowRow(
        modifier = modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items.forEach {
            SelectChip(
                modifier = modifier,
                item = it,
                onClick = onClick
            )
        }
    }
}

@Composable
fun SelectChip(
    modifier: Modifier = Modifier,
    item: InfoChip,
    onClick: (InfoChip) -> Unit = {}
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(40.dp))
            .then(
                if (item.isSelected) {
                    modifier.background(color = DiaryColorsPalette.current.green400)
                } else {
                    modifier
                        .border(width = 1.dp, color = DiaryColorsPalette.current.gray200)
                        .background(color = DiaryColorsPalette.current.gray100)
                }
            )
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick(item)
            }
    ) {
        Text(
            text = item.text,
            color = if (item.isSelected) DiaryColorsPalette.current.gray50 else DiaryColorsPalette.current.gray600,
            style = DiaryTypography.bodyMediumMedium,
            modifier = modifier
                .align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun SelectChipIsSelectedPreview() {
    SsukssukDiaryTheme {
        SelectChip(
            item = AgeChip(isSelected = true, AgeChip.Age.TWENTY)
        )
    }
}

@Preview
@Composable
private fun SelectChipNonSelectedPreview() {
    SsukssukDiaryTheme {
        SelectChip(
            item = AgeChip(isSelected = false, AgeChip.Age.TWENTY)
        )
    }
}

@Preview
@Composable
private fun SelectChipGroupPreview() {
    SsukssukDiaryTheme { 
        SelectChipGroup(
            items = AgeChip.Age.entries.map {
                AgeChip(isSelected = false, age = it)
            }
        )
    }
}