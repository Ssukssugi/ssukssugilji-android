package com.sabo.feature.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sabo.feature.signup.component.SelectChipGroup
import com.sabo.feature.signup.component.SignUpTitle
import com.sabo.feature.signup.model.PlantReasonChip
import com.sabo.feature.signup.model.SignUpUiState

@Composable
internal fun SignUpPlantReasonContent(
    modifier: Modifier = Modifier,
    selectedItems: Set<SignUpUiState.PlantReason> = emptySet(),
    onClickItem: (PlantReasonChip) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SignUpTitle(
            mainTitle = "식물을 키우시는 이유가\n긍금해요",
            subTitle = "더 좋은 서비스를 제공하는데 도움이 돼요"
        )

        SelectChipGroup(
            items = PlantReasonChip.PlantReason.entries.map { chip ->
                PlantReasonChip(
                    isSelected = selectedItems.any { it.name == chip.name },
                    plantReason = chip
                )
            },
            onClick = { onClickItem(it as PlantReasonChip) }
        )
    }
}