package com.sabo.feature.town

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.TopSnackBar
import com.sabo.core.designsystem.component.rememberSnackBarState
import com.sabo.feature.town.component.TownListContent
import com.sabo.feature.town.component.UserReportDialog
import com.sabo.feature.town.component.UserReportOptionsBottomSheet
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun TownScreen(
    modifier: Modifier = Modifier,
    viewModel: TownViewModel = hiltViewModel(),
    navigateToMyGrowths: () -> Unit,
    navigateToDiaryWrite: () -> Unit = {},
) {
    val state = viewModel.collectAsState().value

    var selectedGrowthId by remember { mutableStateOf<Long?>(null) }
    var showPostOptionBottomSheet by remember { mutableStateOf(false) }
    var showUserReportDialog by remember { mutableStateOf(false) }
    var fabExpanded by remember { mutableStateOf(false) }

    var snackBarState by rememberSnackBarState()

    viewModel.collectSideEffect {
        when (it) {
            is TownEvent.ShowPostOptions -> {
                selectedGrowthId = it.growthId
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
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            TownListContent(
                state = state.townContent,
                onLoadMore = viewModel::loadMoreTownGrowth,
                onClickPostMore = viewModel::onClickGrowthPostMore,
                onClickMyPost = navigateToMyGrowths
            )
        }
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
