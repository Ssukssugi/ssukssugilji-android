package com.sabo.feature.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sabo.feature.signup.component.SelectChipGroup
import com.sabo.feature.signup.component.SignUpTitle
import com.sabo.feature.signup.model.HowKnownChip
import com.sabo.feature.signup.model.SignUpUiState

@Composable
internal fun SignUpHowKnowContent(
    modifier: Modifier = Modifier,
    selectedItems: Set<SignUpUiState.HowKnown> = emptySet(),
    onClickItem: (HowKnownChip) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SignUpTitle(
            mainTitle = "쑥쑥일지를 어떻게\n알게 되셨나요?",
            subTitle = "더 좋은 서비스를 제공하는데 도움이 돼요"
        )

        SelectChipGroup(
            items = HowKnownChip.HowKnown.entries.map { chip ->
                HowKnownChip(
                    isSelected = selectedItems.any { it.name == chip.name },
                    howKnown = chip
                )
            },
            onClick = { onClickItem(it as HowKnownChip) }
        )
    }
}