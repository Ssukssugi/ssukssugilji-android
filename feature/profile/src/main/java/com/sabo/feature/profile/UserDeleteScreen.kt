package com.sabo.feature.profile

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.NavigationType
import com.sabo.core.designsystem.component.SsukssukTopAppBar
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun UserDeleteScreen(
    viewModel: UserDeleteViewModel = hiltViewModel(),
    onClickBack: () -> Unit = {},
    navigateToLogin: () -> Unit = {}
) {

    var isShowDeleteDialog by remember { mutableStateOf(false) }

    viewModel.collectSideEffect {
        when (it) {
            UserDeleteUiEvent.ShowAlertDialog -> isShowDeleteDialog = true
            UserDeleteUiEvent.UserDeleted -> navigateToLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        SsukssukTopAppBar(
            navigationType = NavigationType.BACK,
            onNavigationClick = onClickBack,
            containerColor = Color(0xFFFFFFFF),
            content = {
                Text(
                    text = "탈퇴하기",
                    style = DiaryTypography.bodySmallBold,
                    color = DiaryColorsPalette.current.gray900,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f, fill = true)
                )
                Spacer(modifier = Modifier.width(52.dp))
            }
        )
        UserDeleteContent(
            onClickBack = onClickBack,
            onclickDelete = viewModel::onClickDelete
        )
    }

    if (isShowDeleteDialog) {
        DeleteConfirmDialog(
            onDismiss = { isShowDeleteDialog = false },
            onConfirm = viewModel::deleteUser
        )
    }
}

@Composable
private fun UserDeleteContent(
    onclickDelete: () -> Unit = {},
    onClickBack: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "탈퇴하기 전,\n아래 내용을 꼭 확인해주세요!",
                style = DiaryTypography.subtitleLargeBold,
                color = DiaryColorsPalette.current.gray900,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.img_delete_user),
                contentDescription = null,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(200.dp)
            )
            InfoItem(
                icon = R.drawable.icon_recycle,
                title = "등록한 쑥쑥일지는 전부 삭제돼요."
            )
            InfoItem(
                icon = R.drawable.icon_delete_image,
                title = "삭제된 쑥쑥일지는 더이상 확인할 수 없어요."
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = DiaryColorsPalette.current.green50)
                    .clickable { onclickDelete() }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "탈퇴하기",
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
                    .clickable { onClickBack() }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "돌아가기",
                    color = Color.White,
                    style = DiaryTypography.subtitleMediumBold
                )
            }
        }

    }
}

@Composable
private fun InfoItem(
    icon: Int,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp),
            tint = DiaryColorsPalette.current.red400
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = DiaryTypography.bodyMediumMedium,
            color = DiaryColorsPalette.current.gray700,
        )
    }
}

@Composable
private fun DeleteConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(top = 40.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icon_notice_triangle),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = DiaryColorsPalette.current.red400
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "정말 탈퇴할까요?",
                color = DiaryColorsPalette.current.gray900,
                style = DiaryTypography.subtitleLargeBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "쑥쑥일지를 떠난다니 너무 아쉬워요",
                color = DiaryColorsPalette.current.gray600,
                style = DiaryTypography.bodyLargeMedium
            )

            Spacer(modifier = Modifier.height(36.dp))

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
                        .clickable { onConfirm() }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "네, 할게요",
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
                        .clickable { onDismiss() }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "아니요",
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
private fun UserDeleteContentPreview() {
    SsukssukDiaryTheme {
        UserDeleteContent(

        )
    }
}
