package com.sabo.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.sabo.core.designsystem.component.NavigationType
import com.sabo.core.designsystem.component.SsukssukTopAppBar
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.sabo.core.designsystem.R

@Composable
internal fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onClickBack: () -> Unit = {},
    navigateToLogin: () -> Unit = {},
    onClickDeleteAccount: () -> Unit = {},
    onShowError: (String) -> Unit = {}
) {
    val state = viewModel.collectAsState().value
    var showLogoutDialog by remember { mutableStateOf(false) }

    viewModel.collectSideEffect { event ->
        when (event) {
            SettingsEvent.NavigateBack -> onClickBack()
            SettingsEvent.ShowLogoutDialog -> showLogoutDialog = true
            SettingsEvent.NavigateToLogin -> navigateToLogin()
            SettingsEvent.ShowDeleteAccountDialog -> onClickDeleteAccount()
            is SettingsEvent.ShowError -> onShowError(event.message)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        SettingsContent(
            state = state,
            onClickBack = onClickBack,
            onClickLogout = viewModel::onLogoutClick,
            onClickDeleteAccount = viewModel::onDeleteAccountClick,
            onClickMarketingToggle = viewModel::updateMarketingNotification,
            onClickServiceToggle = viewModel::updateServiceNotification
        )

        if (state.isLoading) {
            CircularProgressIndicator(
                color = DiaryColorsPalette.current.green400,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    if (showLogoutDialog) {
        LogoutConfirmDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                viewModel.onConfirmLogout()
            }
        )
    }
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onClickBack: () -> Unit = {},
    onClickLogout: () -> Unit = {},
    onClickDeleteAccount: () -> Unit = {},
    onClickMarketingToggle: (Boolean) -> Unit = {},
    onClickServiceToggle: (Boolean) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(onClickBack = onClickBack)

        Spacer(modifier = Modifier.height(24.dp))

        NotificationSection()

        Spacer(modifier = Modifier.height(24.dp))

        NotificationItem(
            title = "서비스 알림 수신 동의",
            subtitle = "물 주기 알림 등 필수 기능 알림을 드려요",
            isEnabled = state.serviceNotificationEnabled,
            onToggle = onClickServiceToggle
        )

        Spacer(modifier = Modifier.height(16.dp))

        NotificationItem(
            title = "마케팅 알림 수신 동의",
            subtitle = "혜택, 이벤트 등 알림을 드려요",
            isEnabled = state.marketingNotificationEnabled,
            onToggle = onClickMarketingToggle
        )

        Spacer(modifier = Modifier.weight(1f))

        BottomButtons(
            onClickLogout = onClickLogout,
            onClickDeleteAccount = onClickDeleteAccount
        )

        Spacer(modifier = Modifier.height(34.dp))
    }
}

@Composable
private fun TopAppBar(
    onClickBack: () -> Unit = {}
) {
    SsukssukTopAppBar(
        navigationType = NavigationType.BACK,
        containerColor = Color.White,
        onNavigationClick = onClickBack
    ) {
        Text(
            text = "앱 설정",
            color = DiaryColorsPalette.current.gray900,
            style = DiaryTypography.bodySmallBold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(44.dp))
    }
}

@Composable
private fun NotificationSection() {
    Text(
        text = "알림 설정",
        color = DiaryColorsPalette.current.gray600,
        style = DiaryTypography.bodySmallMedium,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

@Composable
private fun NotificationItem(
    title: String,
    subtitle: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = DiaryColorsPalette.current.gray900,
                style = DiaryTypography.bodyMediumSemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = DiaryColorsPalette.current.gray500,
                style = DiaryTypography.bodySmallMedium
            )
        }

        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = DiaryColorsPalette.current.green400,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = DiaryColorsPalette.current.gray300,
                uncheckedBorderColor = DiaryColorsPalette.current.gray300
            )
        )
    }
}

@Composable
private fun BottomButtons(
    onClickLogout: () -> Unit,
    onClickDeleteAccount: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "로그아웃",
            color = DiaryColorsPalette.current.gray500,
            style = DiaryTypography.bodyMediumMedium,
            modifier = Modifier
                .background(color = Color.Transparent)
                .clickable { onClickLogout() }
                .padding(horizontal = 4.dp, vertical = 8.dp)
        )

        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .width(2.dp)
                .height(12.dp)
                .background(color = DiaryColorsPalette.current.gray200, shape = CircleShape)
        )

        Text(
            text = "탈퇴하기",
            color = DiaryColorsPalette.current.gray500,
            style = DiaryTypography.bodyMediumMedium,
            modifier = Modifier
                .background(color = Color.Transparent)
                .clickable { onClickDeleteAccount() }
                .padding(horizontal = 4.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun LogoutConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icon_notice_round),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = DiaryColorsPalette.current.green400
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "정말 로그아웃 할까요?",
                color = DiaryColorsPalette.current.gray900,
                style = DiaryTypography.subtitleLargeBold
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = DiaryColorsPalette.current.green50)
                        .clickable { onDismiss() }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "아니요",
                        color = DiaryColorsPalette.current.green600,
                        style = DiaryTypography.subtitleMediumBold
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DiaryColorsPalette.current.green400)
                        .clickable { onConfirm() }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "네, 할게요",
                        color = Color.White,
                        style = DiaryTypography.subtitleMediumBold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LogoutConfirmDialogPreview() {
    SsukssukDiaryTheme {
        LogoutConfirmDialog(
            onDismiss = {},
            onConfirm = {}
        )
    }
}

@Preview
@Composable
private fun NotificationItemPreview() {
    SsukssukDiaryTheme {
        NotificationItem(
            title = "서비스 알림 수신 동의",
            subtitle = "물 주기 알림 등 필수 기능 알림을 드려요",
            isEnabled = false,
            onToggle = {}
        )
    }
}