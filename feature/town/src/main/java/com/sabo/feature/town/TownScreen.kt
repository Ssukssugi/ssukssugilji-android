package com.sabo.feature.town

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.TopSnackBar
import com.sabo.core.designsystem.component.rememberSnackBarState
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.feature.town.component.GrowthDeleteDialog
import com.sabo.feature.town.component.TownListContent
import com.sabo.feature.town.component.UserReportDialog
import com.sabo.feature.town.component.UserReportOptionsBottomSheet
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun TownScreen(
    modifier: Modifier = Modifier,
    viewModel: TownViewModel = hiltViewModel(),
) {
    val state = viewModel.collectAsState().value

    var selectedGrowth by remember { mutableStateOf<SelectedGrowth?>(null) }
    var showPostOptionBottomSheet by remember { mutableStateOf(false) }
    var showUserReportDialog by remember { mutableStateOf(false) }
    var showGrowthDeleteDialog by remember { mutableStateOf(false) }

    var snackBarState by rememberSnackBarState()

    viewModel.collectSideEffect {
        when (it) {
            is TownEvent.ShowPostOptions -> {
                selectedGrowth = it.data
                showPostOptionBottomSheet = true
            }

            TownEvent.ShowSnackBarReportGrowth -> {
                snackBarState = snackBarState.copy(isVisible = false)
                snackBarState = snackBarState.copy(
                    message = "게시글 신고가 완료되었어요!",
                    iconRes = R.drawable.icon_circle_check,
                    isVisible = true
                )
            }

            TownEvent.ShowSnackBarDeleteGrowth -> {
                snackBarState = snackBarState.copy(isVisible = false)
                snackBarState = snackBarState.copy(
                    message = "게시글 삭제가 완료되었어요!",
                    iconRes = R.drawable.icon_circle_check,
                    isVisible = true
                )
            }
        }
    }

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
            text = "쑥쑥자랑",
            color = DiaryColorsPalette.current.gray900,
            style = DiaryTypography.subtitleLargeBold,
        )
        TownListContent(
            state = state.townContent,
            selectedTab = state.selectedTab,
            onTabSelected = viewModel::onTabSelected,
            onLoadMore = viewModel::loadMoreTownGrowth,
            onClickPostMore = viewModel::onClickGrowthPostMore,
            onClickNetworkRetry = viewModel::onRetryClicked,
        )
    }

    if (showPostOptionBottomSheet) {
        selectedGrowth?.let { growth ->
            UserReportOptionsBottomSheet(
                isMine = growth.isMine,
                onDismissRequest = {
                    showPostOptionBottomSheet = false
                    selectedGrowth = null
                },
                onReportClick = { showUserReportDialog = true },
                onDeleteClick = { showGrowthDeleteDialog = true }
            )
        }
    }

    if (showUserReportDialog) {
        UserReportDialog(
            onDismiss = {
                showUserReportDialog = false
                selectedGrowth = null
            },
            onConfirm = {
                selectedGrowth?.let { viewModel.reportGrowthPost(it.growthId) }
                showPostOptionBottomSheet = false
            }
        )
    }

    if (showGrowthDeleteDialog) {
        GrowthDeleteDialog(
            onDismiss = {
                showGrowthDeleteDialog = false
                selectedGrowth = null
            },
            onConfirm = {
                selectedGrowth?.let { viewModel.deleteGrowthPost(it.growthId) }
                showGrowthDeleteDialog = false
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
