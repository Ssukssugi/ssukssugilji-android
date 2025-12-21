package com.sabo.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.TopSnackBar
import com.sabo.core.designsystem.component.rememberSnackBarState
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.navigator.model.PlantAddEdit
import com.sabo.feature.home.component.PlantDeleteDialog
import com.sabo.feature.home.component.PlantOptionsModalBottomSheet
import com.sabo.feature.home.component.PlantStory
import com.sabo.feature.home.component.SelectedPlantContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToPlantAdd: () -> Unit,
    navigateToDiaryDetail: (Long, Long) -> Unit,
    navigateToPlantEdit: (PlantAddEdit.PlantEdit) -> Unit,
    onSelectedPlantIdChange: (Long?) -> Unit = {},
) {

    val state = viewModel.collectAsState().value
    val selectedPlantId by viewModel.selectedPlantId.collectAsStateWithLifecycle()

    LaunchedEffect(selectedPlantId) {
        onSelectedPlantIdChange(selectedPlantId)
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedPlant by remember { mutableStateOf<PlantListItem.Plant?>(null) }
    var showPlantDeleteDialog by remember { mutableStateOf(false) }

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
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        HomeContent(
            modifier = Modifier.fillMaxSize(),
            plantList = state.plantList,
            plantContent = state.plantContent,
            navigateToPlantAdd = navigateToPlantAdd,
            onClickDiaryDetail = viewModel::onClickDiaryDetail,
            onClickMore = viewModel::onClickMore,
            onClickOtherPlant = viewModel::onSelectPlant
        )
    }

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
    plantContent: PlantContent,
    navigateToPlantAdd: () -> Unit = {},
    onClickDiaryDetail: (Long) -> Unit = {},
    onClickMore: (Long) -> Unit = {},
    onClickOtherPlant: (Long) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 9.dp),
            text = "쑥쑥일지",
            color = DiaryColorsPalette.current.gray900,
            style = DiaryTypography.subtitleLargeBold,
        )

        PlantStory(
            plantList = plantList,
            onClickPlant = onClickOtherPlant,
            onClickAddPlant = navigateToPlantAdd
        )

        SelectedPlantContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true),
            data = plantContent,
            onClickDiaryDetail = onClickDiaryDetail,
            onClickMore = onClickMore
        )
    }
}
