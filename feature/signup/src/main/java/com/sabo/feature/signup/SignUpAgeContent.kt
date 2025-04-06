package com.sabo.feature.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.feature.signup.component.SelectChipGroup
import com.sabo.feature.signup.component.SignUpTitle
import com.sabo.feature.signup.model.AgeChip
import com.sabo.feature.signup.model.SignUpUiState

@Composable
fun SignUpAgeContent(
    modifier: Modifier = Modifier,
    nickname: String,
    selectedAge: SignUpUiState.Age?,
    onClickItem: (AgeChip) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SignUpTitle(
            mainTitle = "${nickname}님\n연령대가 어떻게 되시나요",
            subTitle = "더 좋은 서비스를 제공하는데 도움이 돼요"
        )

        SelectChipGroup(
            items = AgeChip.Age.entries.map {
                AgeChip(
                    isSelected = selectedAge?.name == it.name,
                    age = it
                )
            },
            onClick = { onClickItem(it as AgeChip) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpExtraInfoScreenPreview() {
    SsukssukDiaryTheme {
        SignUpAgeContent(
            nickname = "씩씩한몬스테라",
            selectedAge = SignUpUiState.Age.TWENTY
        )
    }
}