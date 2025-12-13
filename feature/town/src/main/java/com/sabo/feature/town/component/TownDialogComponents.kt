package com.sabo.feature.town.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserReportOptionsBottomSheet(
    onDismissRequest: () -> Unit,
    onReportClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(width = 56.dp, height = 4.dp)
                    .background(
                        color = Color(0xFFDDDDDD),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        },
        containerColor = Color(0xFFFFFFFF)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFFFFFFF))
                .padding(vertical = 24.dp)
        ) {
            ReportBottomSheetItem(
                iconRes = R.drawable.icon_trash,
                text = "신고하기",
                tint = DiaryColorsPalette.current.red400,
                onClick = { onReportClick() }
            )
        }
    }
}

@Composable
private fun ReportBottomSheetItem(
    iconRes: Int,
    text: String,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = text,
            style = DiaryTypography.bodyLargeSemiBold,
            color = tint
        )
    }
}

@Composable
internal fun UserReportDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icon_notice_triangle),
                contentDescription = null,
                tint = DiaryColorsPalette.current.red400,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "이 게시글을 정말 신고할까요?",
                style = DiaryTypography.subtitleLargeBold,
                color = DiaryColorsPalette.current.gray900,
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 8.dp)
            )
            Text(
                text = "신고 접수 시, 운영자의 검토 후\n게시글이 처리될 예정입니다.",
                style = DiaryTypography.bodyLargeMedium,
                color = DiaryColorsPalette.current.gray600
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DiaryColorsPalette.current.green50)
                        .clickable {
                            onConfirm()
                            onDismiss()
                        }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "신고하기",
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
                        text = "돌아가기",
                        color = DiaryColorsPalette.current.gray50,
                        style = DiaryTypography.subtitleMediumBold
                    )
                }
            }
        }
    }
}
