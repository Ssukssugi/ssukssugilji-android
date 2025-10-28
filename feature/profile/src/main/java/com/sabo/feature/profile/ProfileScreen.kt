package com.sabo.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.NavigationType
import com.sabo.core.designsystem.component.SsukssukTopAppBar
import com.sabo.core.designsystem.component.TopSnackBar
import com.sabo.core.designsystem.component.rememberSnackBarState
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onClickBack: () -> Unit = {},
    onClickSetting: () -> Unit = {},
    onClickFAQ: () -> Unit = {},
    onClickPolicy: () -> Unit = {},
    onClickProfile: (String) -> Unit = {}
) {

    val state = viewModel.collectAsState().value
    var snackBarState by rememberSnackBarState()

    viewModel.collectSideEffect {
        when (it) {
            ProfileEvent.ProfileUpdated -> {
                snackBarState = snackBarState.copy(isVisible =  false)
                snackBarState = snackBarState.copy(
                    message = "수정이 완료되었습니다!",
                    iconRes = R.drawable.icon_circle_check,
                    isVisible = true
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            onClickBack = onClickBack,
            onClickSetting = onClickSetting
        )
        ProfileContent(
            name = state.name,
            onClick = onClickProfile
        )
        Spacer(modifier = Modifier.height(8.dp))
        LinkItem(
            iconRes = R.drawable.icon_mail_faq,
            title = "의견 보내기",
            onClick = onClickFAQ
        )
        LinkItem(
            iconRes = R.drawable.icon_policy_document,
            title = "약관 및 정책",
            onClick = onClickPolicy
        )
    }

    if (snackBarState.isVisible) {
        TopSnackBar(
            message = snackBarState.message,
            iconRes = snackBarState.iconRes,
            iconTint = snackBarState.iconTint,
            onDismiss = { snackBarState = snackBarState.copy(isVisible = false) }
        )
    }
}

@Composable
private fun TopAppBar(
    onClickBack: () -> Unit = {},
    onClickSetting: () -> Unit = {}
) {
    SsukssukTopAppBar(
        navigationType = NavigationType.BACK,
        containerColor = Color.White,
        onNavigationClick = onClickBack
    ) {
        Text(
            text = "쑥쑥집사 프로필",
            color = DiaryColorsPalette.current.gray900,
            style = DiaryTypography.bodySmallBold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.icon_settings),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 20.dp)
                .size(24.dp)
                .clip(CircleShape)
                .clickable { onClickSetting() }
        )
    }
}

@Composable
private fun ProfileContent(
    name: String,
    onClick: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(name) }
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_logo_cat),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .background(color = Color.White, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = name,
            color = DiaryColorsPalette.current.gray900,
            style = DiaryTypography.subtitleMediumBold,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.icon_arrow_right_24),
            contentDescription = null,
            tint = DiaryColorsPalette.current.gray500,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun LinkItem(
    iconRes: Int,
    title: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = iconRes),
            contentDescription = null,
            tint = DiaryColorsPalette.current.gray700,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = DiaryTypography.bodyLargeMedium,
            color = DiaryColorsPalette.current.gray700,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.icon_arrow_right_24),
            contentDescription = null,
            tint = DiaryColorsPalette.current.gray500,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview
@Composable
private fun TopAppBarPreview() {
    SsukssukDiaryTheme {
        TopAppBar { }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileContentPreview() {
    SsukssukDiaryTheme {
        ProfileContent(name = "쑥쑥집사")
    }
}

@Preview
@Composable
private fun LinkItemPreview() {
    SsukssukDiaryTheme {
        LinkItem(
            iconRes = R.drawable.icon_mail_faq,
            title = "의견 보내기",
            onClick = {}
        )
    }
}