package com.sabo.feature.diary.gallery

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.NavigationType
import com.sabo.core.designsystem.component.SsukssukTopAppBar
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun GalleryScreen(
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel = hiltViewModel(),
    plantId: Long?,
    onClickBack: () -> Unit = {},
    onClickNext: (Long?, Uri) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            viewModel.onImageSelected(it)
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            capturedImageUri?.let { uri ->
                viewModel.onImageSelected(uri)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
    ) {
        SsukssukTopAppBar(
            modifier = modifier,
            navigationType = NavigationType.CLOSE,
            onNavigationClick = onClickBack,
            containerColor = Color(0xFFFFFFFF)
        )

        ImagePreviewContainer(
            uri = uiState.selectedImageUri
        )

        Spacer(modifier = Modifier.weight(1f))

        SelectionButtons(
            modifier = modifier,
            onClickGallery = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onClickCamera = {
                if (cameraPermissionState.status.isGranted) {
                    val uri = viewModel.createImageUri()
                    capturedImageUri = uri
                    takePictureLauncher.launch(uri)
                } else {
                    cameraPermissionState.launchPermissionRequest()
                }
            }
        )

        if (uiState.selectedImageUri != null) {
            NextButton(modifier) {
                uiState.selectedImageUri?.let {
                    onClickNext(plantId, it)
                }
            }
        }
    }
}

@Composable
private fun SelectionButtons(
    modifier: Modifier = Modifier,
    onClickGallery: () -> Unit = {},
    onClickCamera: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SelectionButton(
            text = "갤러리에서 선택",
            icon = R.drawable.icon_photo_camera_32,
            onClick = onClickGallery
        )
        SelectionButton(
            text = "사진 촬영",
            icon = R.drawable.icon_photo_camera_32,
            onClick = onClickCamera
        )
    }
}

@Composable
private fun SelectionButton(
    text: String,
    icon: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = DiaryColorsPalette.current.green50,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null,
                tint = DiaryColorsPalette.current.green600,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = text,
                style = DiaryTypography.bodyMediumSemiBold,
                color = DiaryColorsPalette.current.green600
            )
        }
    }
}

@Composable
private fun ImagePreviewContainer(
    uri: Uri?
) {
    AnimatedVisibility(
        visible = uri != null
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(390.dp)
                .padding(24.dp)
        ) {
            AsyncImage(
                model = uri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(0.75f)
                    .clip(RoundedCornerShape(8.dp))
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun NextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color(0xFFFFFFFF))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = DiaryColorsPalette.current.green400, shape = RoundedCornerShape(16.dp))
                .clickable { onClick() }
                .padding(vertical = 14.dp)
        ) {
            Text(
                text = "다음",
                color = Color(0xFFFFFFFF),
                style = DiaryTypography.subtitleMediumBold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NextButtonPreview() {
    SsukssukDiaryTheme {
        NextButton { }
    }
}
