package com.sabo.feature.signup.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sabo.core.designsystem.theme.DiaryTypography

@Composable
internal fun SignUpTitle(
    modifier: Modifier = Modifier,
    mainTitle: String,
    subTitle: String = ""
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth(),
            text = mainTitle,
            color = Color(0xFF333333),
            style = DiaryTypography.subtitleLargeBold
        )

        Spacer(modifier = modifier.height(8.dp))

        Text(
            modifier = modifier
                .fillMaxWidth(),
            text = subTitle,
            color = Color(0xFF777777),
            style = DiaryTypography.bodyLargeMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TitleTextPreview() {
    SignUpTitle(
        mainTitle = "원하는 닉네임이\n있으신가요?"
    )
}