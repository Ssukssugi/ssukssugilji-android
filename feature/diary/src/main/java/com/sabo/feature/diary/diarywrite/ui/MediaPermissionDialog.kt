package com.sabo.feature.diary.diarywrite.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme

@Composable
internal fun MediaPermissionDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Box(
                    modifier = modifier
                        .padding(vertical = 8.dp)
                        .size(width = 56.dp, height = 4.dp)
                        .background(color = Color(0xFFDDDDDD), shape = RoundedCornerShape(2.dp))
                        .align(Alignment.CenterHorizontally),
                ) {}

                Text(
                    text = "서비스 사용에 필요한 권한을\n허용해주세요",
                    color = Color(0xFF333333),
                    fontWeight = FontWeight.Bold,
                    modifier = modifier
                        .padding(20.dp)
                )

                PermissionDescriptionItem(
                    modifier = modifier,
                    image = R.drawable.icon_photo_camera_32,
                    title = "카메라 접근 권한 허용",
                    description = "사진 촬영을 위해 앨범 접근 권한을 허용해주세요"
                )

                PermissionDescriptionItem(
                    modifier = modifier,
                    image = R.drawable.icon_insert_photo_32,
                    title = "앨범 접근 권한 허용",
                    description = "사진 업로드를 위해 앨범 접근 권한을 허용해주세요"
                )

                Box(
                    modifier = modifier
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .background(color = DiaryColorsPalette.current.green400, shape = RoundedCornerShape(16.dp))
                        .padding(16.dp)
                        .clickable(
                            onClick = onConfirm
                        )
                ) {
                    Text(
                        text = "허용하기",
                        modifier = modifier
                            .fillMaxWidth(),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionDescriptionItem(
    modifier: Modifier,
    @DrawableRes image: Int,
    title: String,
    description: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Image(
            imageVector = ImageVector.vectorResource(image),
            contentDescription = null,
            modifier = modifier.size(32.dp)
        )

        Column(
            modifier = modifier
                .padding(start = 16.dp)
                .wrapContentHeight()
        ) {
            Text(
                text = title,
                color = DiaryColorsPalette.current.gray600
            )

            Text(
                text = description,
                color = Color(0xFF777777),
                modifier = modifier.padding(top = 2.dp)
            )
        }
    }
}

@Preview
@Composable
private fun MediaPermissionDialogPreview() {
    SsukssukDiaryTheme {
        MediaPermissionDialog(
            onDismiss = {},
            onConfirm = {}
        )
    }
}