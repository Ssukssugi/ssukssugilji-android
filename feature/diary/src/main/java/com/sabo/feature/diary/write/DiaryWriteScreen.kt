package com.sabo.feature.diary.write

import android.net.Uri
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.DatePickerDialog
import com.sabo.core.designsystem.component.NavigationType
import com.sabo.core.designsystem.component.SsukssukTopAppBar
import com.sabo.core.designsystem.component.TopSnackBar
import com.sabo.core.designsystem.component.rememberSnackBarState
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.core.designsystem.toolkit.noRippleClickable
import com.sabo.core.model.CareType
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DiaryWriteScreen(
    viewModel: DiaryWriteViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    navigateToDiaryDetail: (Long) -> Unit,
    navigateToHome: () -> Unit
) {
    val uiState = viewModel.collectAsState().value
    val isSaveEnabled by viewModel.isSaveEnabled.collectAsStateWithLifecycle()

    var isShownDatePicker by remember { mutableStateOf(false) }
    var snackBarState by rememberSnackBarState()

    viewModel.collectSideEffect {
        when (it) {
            is DiaryWriteSideEffect.NavigateToDetail -> navigateToDiaryDetail(it.plantId)
            is DiaryWriteSideEffect.ShowSnackBar -> {
                when (it.type) {
                    DiaryWriteSideEffect.ShowSnackBar.SnackBarType.PLANT_REQUIRED -> {
                        snackBarState = snackBarState.copy(isVisible =  false)
                        snackBarState = snackBarState.copy(
                            message = "식물 목록을 선택해주세요.",
                            iconRes = R.drawable.icon_notice_triangle,
                            iconTint = Color(0xFFFFDC16),
                            isVisible = true
                        )
                    }
                }
            }
        }
    }

    val scrollState = rememberScrollState()

    when {
        uiState.isSaveLoading -> {
            DiarySaveLoadingContent()
        }
        uiState.isSaveSuccess -> {
            DiarySaveSuccessContent(
                onClickGoToDetail = viewModel::onClickGoToDiaryDetail,
                navigateToHome = navigateToHome
            )
        }
        else -> {
            DiaryWriteContent(
                uiState = uiState,
                scrollState = scrollState,
                isSaveEnabled = isSaveEnabled,
                onClickBack = onClickBack,
                onClickPlant = viewModel::onClickPlant,
                onClickCalendar = { isShownDatePicker = true },
                onSelectCareType = viewModel::onClickCareType,
                onClickSave = viewModel::onClickSave
            )
        }
    }

    if (isShownDatePicker) {
        DatePickerDialog(
            selectedDate = uiState.date,
            onDismiss = { isShownDatePicker = false },
            onSuccess = viewModel::onChangeDate
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
private fun DiaryWriteContent(
    uiState: DiaryWriteUiState,
    scrollState: ScrollState,
    isSaveEnabled: Boolean,
    onClickBack: () -> Unit = {},
    onClickPlant: (Long) -> Unit = {},
    onClickCalendar: () -> Unit = {},
    onSelectCareType: (CareType) -> Unit = {},
    onClickSave: () -> Unit = {}
) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy년 MM월 dd일") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
    ) {
        SsukssukTopAppBar(
            navigationType = NavigationType.BACK,
            onNavigationClick = onClickBack,
            containerColor = Color(0xFFFFFFFF)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 72.dp)
                    .verticalScroll(scrollState)
            ) {
                AsyncImage(
                    model = uiState.imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .height(290.dp)
                        .aspectRatio(0.75f)
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(8.dp))
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "식물 목록",
                        style = DiaryTypography.captionLargeRegular,
                        color = Color(0xFF777777),
                    )
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp)
                ) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(color = DiaryColorsPalette.current.gray400, shape = CircleShape)
                                    .noRippleClickable { /* TODO: 식물 추가 */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.icon_add_plant),
                                    contentDescription = null,
                                    tint = DiaryColorsPalette.current.gray50,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "식물 추가하기",
                                style = DiaryTypography.bodySmallSemiBold,
                                color = DiaryColorsPalette.current.gray800,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.width(90.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    items(
                        items = uiState.plants,
                        key = { it.id }
                    ) { plant ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(DiaryColorsPalette.current.gray100)
                                    .clickable { onClickPlant(plant.id) }
                            ) {
                                AsyncImage(
                                    model = plant.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                if (plant.isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(color = Color(0xFF000000).copy(alpha = 0.5f))
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.icon_check_24),
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .align(Alignment.Center)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = plant.name,
                                style = DiaryTypography.bodySmallSemiBold,
                                color = DiaryColorsPalette.current.gray800,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.width(90.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Text(
                    text = "일지 날짜",
                    style = DiaryTypography.captionLargeRegular,
                    color = Color(0xFF777777),
                    modifier = Modifier.padding(top = 12.dp, start = 20.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = uiState.date.format(dateFormatter),
                        style = DiaryTypography.bodyMediumRegular,
                        color = DiaryColorsPalette.current.gray900
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.icon_invitation),
                        contentDescription = null,
                        tint = DiaryColorsPalette.current.gray700,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(24.dp)
                            .clickable { onClickCalendar() }
                    )
                }

                Text(
                    text = "돌보기",
                    style = DiaryTypography.captionLargeRegular,
                    color = DiaryColorsPalette.current.gray700,
                    modifier = Modifier.padding(top = 20.dp, start = 20.dp, bottom = 12.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(uiState.careTypes) { care ->
                        val isSelected = care.isSelected
                        FilterChip(
                            onClick = { onSelectCareType(care.type) },
                            label = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = when (care.type) {
                                            CareType.WATER -> "물 주기"
                                            CareType.DIVIDING -> "분갈이"
                                            CareType.NUTRIENT -> "영양제"
                                            CareType.PRUNING -> "가지치기"
                                        },
                                        style = DiaryTypography.bodyMediumRegular,
                                        color = if (isSelected) DiaryColorsPalette.current.gray50 else DiaryColorsPalette.current.gray600
                                    )
                                }
                            },
                            selected = isSelected,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DiaryColorsPalette.current.green400,
                                selectedLabelColor = DiaryColorsPalette.current.gray50,
                                containerColor = DiaryColorsPalette.current.gray200,
                                labelColor = DiaryColorsPalette.current.gray700,
                                disabledContainerColor = DiaryColorsPalette.current.gray100,
                                disabledSelectedContainerColor = DiaryColorsPalette.current.green400
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) DiaryColorsPalette.current.green400 else DiaryColorsPalette.current.gray300,
                                selectedBorderColor = DiaryColorsPalette.current.green400,
                                disabledBorderColor = DiaryColorsPalette.current.gray200,
                                disabledSelectedBorderColor = DiaryColorsPalette.current.green400,
                                borderWidth = 1.dp,
                                selectedBorderWidth = 1.dp
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }

                Text(
                    text = "일기",
                    style = DiaryTypography.captionLargeRegular,
                    color = DiaryColorsPalette.current.gray700,
                    modifier = Modifier.padding(top = 20.dp, start = 20.dp)
                )

                DiaryInput(
                    textFieldState = uiState.content,
                )
            }

            SaveButton(
                modifier = Modifier.align(Alignment.BottomCenter),
                onClickSave = onClickSave,
                isEnabled = isSaveEnabled
            )

            if (uiState.isLoading) {
                LoadingContent()
            }
        }
    }
}

@Composable
private fun DiaryInput(
    textFieldState: TextFieldState
) {
    BasicTextField(
        state = textFieldState,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        inputTransformation = InputTransformation.maxLength(1000),
        decorator = { innerTextField ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                if (textFieldState.text.isEmpty()) {
                    Text(
                        text = "일지 내용을 1000자 이내로 작성해주세요",
                        color = DiaryColorsPalette.current.gray400,
                        style = DiaryTypography.bodyLargeRegular
                    )
                } else {
                    innerTextField()
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = DiaryColorsPalette.current.gray400)
                )
            }
        }
    )
}

@Composable
private fun SaveButton(
    modifier: Modifier = Modifier,
    onClickSave: () -> Unit,
    isEnabled: Boolean
) {
    Box(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .fillMaxWidth()
            .background(
                color = if (isEnabled) DiaryColorsPalette.current.green500 else DiaryColorsPalette.current.green100,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClickSave() }
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "작성 완료하기",
            style = DiaryTypography.subtitleMediumBold,
            color = DiaryColorsPalette.current.gray50,
            modifier = modifier.align(Alignment.Center)
        )
    }
}


@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(56.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun DiarySaveLoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
            .padding(vertical = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "일지 작성을 완료 중이에요",
            style = DiaryTypography.headlineSmallBold,
            color = Color(0xFF000000)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "잠시만 기다려주세요",
            style = DiaryTypography.subtitleMediumMedium,
            color = Color(0xFF000000)
        )
    }
}

@Composable
private fun DiarySaveSuccessContent(
    onClickGoToDetail: () -> Unit = {},
    navigateToHome: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
            .padding(top = 72.dp, bottom = 16.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "일지 작성을 완료했어요!",
            style = DiaryTypography.headlineSmallBold,
            color = Color(0xFF000000)
        )
        Spacer(
            modifier = Modifier
            .weight(1f, fill = true)
        )

        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 4.dp)
                .fillMaxWidth()
                .background(
                    color = DiaryColorsPalette.current.green500,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onClickGoToDetail() }
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "일지 보러가기",
                style = DiaryTypography.subtitleMediumBold,
                color = DiaryColorsPalette.current.gray50,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 4.dp)
                .fillMaxWidth()
                .background(
                    color = DiaryColorsPalette.current.green50,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { navigateToHome() }
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "홈으로 가기",
                style = DiaryTypography.subtitleMediumBold,
                color = DiaryColorsPalette.current.green600,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun DiaryWriteContentPreview() {
    SsukssukDiaryTheme {
        DiaryWriteContent(
            uiState = DiaryWriteUiState(
                isLoading = false,
                imageUri = Uri.EMPTY,
                plants = listOf(
                    PlantListItem(
                        id = 1L,
                        name = "몬스테라",
                        imageUrl = "",
                        isSelected = true
                    ),
                    PlantListItem(
                        id = 2L,
                        name = "스킨답서스",
                        imageUrl = ""
                    )
                )
            ),
            scrollState = ScrollState(0),
            onClickBack = {},
            isSaveEnabled = false
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun DiaryWriteContentLoadingPreview() {
    DiaryWriteContent(
        uiState = DiaryWriteUiState(
            isLoading = true
        ),
        scrollState = ScrollState(0),
        onClickBack = {},
        isSaveEnabled = true
    )
}

@Preview
@Composable
private fun DiarySaveSuccessContentPreview() {
    SsukssukDiaryTheme {
        DiarySaveSuccessContent()
    }
}
