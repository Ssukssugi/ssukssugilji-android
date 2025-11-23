package com.sabo.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.TopSnackBar
import com.sabo.core.designsystem.component.rememberSnackBarState
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.navigator.model.PlantAddEdit
import com.sabo.feature.home.component.PlantDeleteDialog
import com.sabo.feature.home.component.PlantOptionsModalBottomSheet
import com.sabo.feature.home.component.PlantStory
import com.sabo.feature.home.component.SelectedPlantContent
import com.sabo.feature.home.component.TownListContent
import com.sabo.feature.home.component.UserReportDialog
import com.sabo.feature.home.component.UserReportOptionsBottomSheet
import com.sabo.feature.home.component.WriteDiaryFAB
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToGallery: (Long) -> Unit,
    navigateToPlantAdd: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToDiaryDetail: (Long, Long) -> Unit,
    navigateToPlantEdit: (PlantAddEdit.PlantEdit) -> Unit,
    navigateToMyGrowths: () -> Unit
) {

    val state = viewModel.collectAsState().value

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedPlant by remember { mutableStateOf<PlantListItem.Plant?>(null) }
    var showPlantDeleteDialog by remember { mutableStateOf(false) }

    var selectedGrowthId by remember { mutableStateOf<Long?>(null) }
    var showPostOptionBottomSheet by remember { mutableStateOf(false) }
    var showUserReportDialog by remember { mutableStateOf(false) }

    var snackBarState by rememberSnackBarState()

    viewModel.collectSideEffect {
        when (it) {
            is HomeEvent.NavigateToDiaryDetail -> navigateToDiaryDetail(it.plantId, it.diaryId)
            is HomeEvent.ShowPlantOptions -> {
                selectedPlant = it.plant
                showBottomSheet = true
            }

            is HomeEvent.NavigateToPlantEdit -> {
                showBottomSheet = false
                navigateToPlantEdit(it.route)
            }

            HomeEvent.ShowSnackBarDeletePlant -> {
                snackBarState = snackBarState.copy(isVisible = false)
                snackBarState = snackBarState.copy(
                    message = "삭제가 완료되었습니다!",
                    iconRes = R.drawable.icon_circle_check,
                    isVisible = true
                )
            }

            HomeEvent.ShowSnackBarReportGrowth -> {
                snackBarState = snackBarState.copy(isVisible = false)
                snackBarState = snackBarState.copy(
                    message = "게시글 신고가 완료되었어요!",
                    iconRes = R.drawable.icon_circle_check,
                    isVisible = true
                )
            }

            is HomeEvent.ShowPostOptions -> {
                selectedGrowthId = it.growthId
                showPostOptionBottomSheet = true
            }
        }
    }

    HomeContent(
        modifier = modifier,
        plantList = state.plantList,
        content = state.homeContent,
        navigateToGallery = navigateToGallery,
        navigateToPlantAdd = navigateToPlantAdd,
        navigateToProfile = navigateToProfile,
        onClickDiaryDetail = viewModel::onClickDiaryDetail,
        onClickMore = viewModel::onClickMore,
        onClickOtherPlant = viewModel::onSelectPlant,
        onClickTown = viewModel::onSelectTown,
        onLoadMoreTown = viewModel::loadMoreTownGrowth,
        onClickPostMore = viewModel::onClickGrowthPostMore,
        navigateToMyGrowths = navigateToMyGrowths
    )

    if (showBottomSheet) {
        PlantOptionsModalBottomSheet(
            plant = selectedPlant,
            onEditClick = viewModel::onEditPlant,
            onDeleteClick = { showPlantDeleteDialog = true },
            onDismissRequest = {
                showBottomSheet = false
                selectedPlant = null
            }
        )
    }

    if (showPlantDeleteDialog) {
        PlantDeleteDialog(
            onDismiss = { showPlantDeleteDialog = false },
            onConfirm = viewModel::onDeletePlant
        )
    }

    if (showPostOptionBottomSheet) {
        UserReportOptionsBottomSheet(
            onDismissRequest = {
                showPostOptionBottomSheet = false
                selectedGrowthId = null
            },
            onReportClick = { showUserReportDialog = true }
        )
    }

    if (showUserReportDialog) {
        UserReportDialog(
            onDismiss = {
                showUserReportDialog = false
                selectedGrowthId = null
            },
            onConfirm = {
                selectedGrowthId?.let { viewModel.reportGrowthPost(it) }
                showPostOptionBottomSheet = false
            }
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
private fun HomeContent(
    modifier: Modifier = Modifier,
    plantList: List<PlantListItem>,
    content: HomeContent,
    navigateToGallery: (Long) -> Unit = {},
    navigateToPlantAdd: () -> Unit = {},
    navigateToProfile: () -> Unit = {},
    onClickDiaryDetail: (Long) -> Unit = {},
    onClickMore: (Long) -> Unit = {},
    onClickOtherPlant: (Long) -> Unit = {},
    onClickTown: () -> Unit = {},
    onLoadMoreTown: (Long) -> Unit = {},
    onClickPostMore: (Long) -> Unit = {},
    navigateToMyGrowths: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .background(Color.White)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.img_home_logo),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_account),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .clickable { navigateToProfile() },
                    tint = DiaryColorsPalette.current.gray900
                )
            }

            PlantStory(
                plantList = plantList,
                isTownSelected = content is HomeContent.Town,
                onClickPlant = onClickOtherPlant,
                onClickAddPlant = navigateToPlantAdd,
                onClickTown = onClickTown
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = DiaryColorsPalette.current.gray500,
                thickness = 1.dp
            )

            when (content) {
                is HomeContent.Diary -> {
                    SelectedPlantContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = true),
                        data = content.plantContent,
                        onClickDiaryDetail = onClickDiaryDetail,
                        onClickMore = onClickMore
                    )
                }

                is HomeContent.Town -> {
                    TownListContent(
                        state = content.townContent,
                        onLoadMore = onLoadMoreTown,
                        onClickPostMore = onClickPostMore,
                        onClickMyPost = navigateToMyGrowths
                    )
                }
            }
        }
        if (content is HomeContent.Diary) {
            WriteDiaryFAB(
                modifier = modifier,
                anyPlants = plantList.filterIsInstance<PlantListItem.Plant>().isNotEmpty(),
                navigateToGallery = {
                    plantList
                        .filterIsInstance<PlantListItem.Plant>()
                        .find { it.isSelected }
                        ?.let { navigateToGallery(it.id) }
                },
                navigateToAddPlant = navigateToPlantAdd
            )
        }
    }
}
