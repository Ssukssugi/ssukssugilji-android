package com.sabo.feature.diary.gallery

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.core.designsystem.theme.component.NavigationType
import com.sabo.core.designsystem.theme.component.SsukssukTopAppBar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun GalleryScreen(
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel = hiltViewModel(),
    onClickBack: () -> Unit = {}
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val permissionState = rememberMultiplePermissionsState(
        listOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.CAMERA)
    )

    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            capturedImageUri?.let { uri ->
                viewModel.onImageCaptured(uri)
            }
        }
    }

    LaunchedEffect(permissionState.allPermissionsGranted) {
        viewModel.onPermissionResult(permissionState.allPermissionsGranted)
        if (permissionState.allPermissionsGranted) {
            viewModel.loadGalleryImages()
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

        GalleryHeader(
            onClickCategory = {}
        )

        GalleryGridList(
            modifier = modifier,
            images = uiState.images,
            onClickCamera = {
                if (permissionState.allPermissionsGranted) {
                    val uri = viewModel.createImageUri()
                    capturedImageUri = uri
                    takePictureLauncher.launch(uri)
                } else {
                    viewModel.showPermissionDialog()
                }
            },
            onClickImage = viewModel::onImageSelected
        )
    }

    if (uiState.showPermissionDialog) {
        MediaPermissionDialog(
            onDismiss = viewModel::dismissPermissionDialog,
            onConfirm = {
                permissionState.launchMultiplePermissionRequest()
                viewModel.dismissPermissionDialog()
            }
        )
    }
}

@Composable
private fun GalleryHeader(
    modifier: Modifier = Modifier,
    onClickCategory: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color(0xFFFFFFFF))
            .padding(horizontal = 20.dp, vertical = 14.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = modifier
                .wrapContentSize()
                .clickable { onClickCategory() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "모든 사진",
                style = DiaryTypography.bodySmallMedium
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icon_arrow_down_24),
                contentDescription = null,
                tint = DiaryColorsPalette.current.gray700
            )
        }
    }
}

@Composable
private fun GalleryGridList(
    modifier: Modifier = Modifier,
    images: List<Uri>,
    onClickCamera: () -> Unit,
    onClickImage: (Uri) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(2.dp)
    ) {
        cameraOpenItem(
            onClick = onClickCamera
        )

        items(images) { uri ->
            AsyncImage(
                model = uri,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .clickable { onClickImage(uri) }
            )
        }
    }
}

private fun LazyGridScope.cameraOpenItem(
    onClick: () -> Unit
) {
    item {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
                .background(color = Color(0x80FFFFFF))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icon_photo_camera_32),
                contentDescription = null,
                tint = DiaryColorsPalette.current.gray800
            )
        }
    }
}

@Preview
@Composable
private fun GalleryScreenPreview() {
    SsukssukDiaryTheme {
        GalleryScreen()
    }
}
