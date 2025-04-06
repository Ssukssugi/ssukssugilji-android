package com.sabo.feature.signup.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography

@Composable
internal fun SignUpContentTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        text = text,
        color = DiaryColorsPalette.current.green400,
        style = DiaryTypography.bodyMediumRegular
    )
}