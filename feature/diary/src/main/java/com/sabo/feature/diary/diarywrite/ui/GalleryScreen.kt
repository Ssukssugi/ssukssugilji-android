package com.sabo.feature.diary.diarywrite.ui

import android.Manifest
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.core.designsystem.theme.component.NavigationType
import com.sabo.core.designsystem.theme.component.SsukssukTopAppBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun GalleryScreen(
    modifier: Modifier = Modifier
) {
    val permissionState = rememberPermissionState(Manifest.permission.READ_MEDIA_IMAGES)
    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(permissionState.status) {
        images = if (permissionState.status.isGranted) {
            loadGalleryImages(context = context)
        } else {
            emptyList()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
    ) {
        SsukssukTopAppBar(
            modifier = modifier,
            navigationType = NavigationType.BACK,
            onNavigationClick = {},
            containerColor = Color(0xFFFFFFFF)
        )

        GalleryHeader(
            onClickCategory = {}
        )

        GalleryGridList(
            modifier = modifier,
            images = images,
            onClickImage = {}
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
    onClickImage: (Uri) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(2.dp)
    ) {
        cameraOpenItem {  }

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

suspend fun loadGalleryImages(context: Context): List<Uri> = withContext(Dispatchers.IO) {
    val imageUriList = mutableListOf<Uri>()
    val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    context.contentResolver.query(
        collection,
        projection,
        null,
        null,
        sortOrder
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val contentUri = Uri.withAppendedPath(collection, id.toString())
            imageUriList.add(contentUri)
        }
    }
    imageUriList
}

@Preview
@Composable
private fun GalleryScreenPreview() {
    SsukssukDiaryTheme {
        GalleryScreen()
    }
}
