package com.sabo.feature.diary.plantadd

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.core.designsystem.component.NavigationType
import com.sabo.core.designsystem.component.SsukssukTopAppBar

@Composable
internal fun PlantAddRoute(
    modifier: Modifier = Modifier,
    viewModel: PlantAddViewModel = hiltViewModel(),
    onClickBack: () -> Unit = {},
    onClickCategory: (String) -> Unit = {},
    onClickHome: () -> Unit = {},
    onClickDiary: () -> Unit = {}
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val isAddButtonEnabled by viewModel.isAddable.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)),
        verticalArrangement = Arrangement.Top
    ) {
        when (val state = uiState) {
            is PlantAddState.Input -> {
                SsukssukTopAppBar(
                    modifier = modifier,
                    navigationType = NavigationType.BACK,
                    containerColor = Color(0xFFFFFFFF),
                    onNavigationClick = onClickBack
                )

                PlantInfoInputScreen(
                    state = state,
                    isAddButtonEnabled = isAddButtonEnabled,
                    onClickCategory = onClickCategory,
                    onClickLightStep = viewModel::onClickLightStep,
                    onClickPlace = viewModel::onClickPlace,
                    onClickAddButton = viewModel::savePlant
                )
            }

            PlantAddState.SaveSuccess -> {
                SaveSuccessContent(
                    onClickHome = onClickHome,
                    onClickDiary = onClickDiary
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.PlantInfoInputScreen(
    modifier: Modifier = Modifier,
    state: PlantAddState.Input = PlantAddState.Input(),
    isAddButtonEnabled: Boolean,
    onClickCategory: (String) -> Unit = {},
    onClickLightStep: (Int) -> Unit = {},
    onClickPlace: (PlantPlace) -> Unit = {},
    onClickAddButton: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .weight(1f, fill = true)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = modifier
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "키우는 식물을\n추가해주세요",
                        style = DiaryTypography.subtitleLargeSemiBold,
                        color = DiaryColorsPalette.current.gray900
                    )
                    Spacer(modifier = modifier.height(6.dp))
                    Text(
                        text = "일지를 작성하기 위해 필요해요",
                        style = DiaryTypography.bodyMediumRegular,
                        color = DiaryColorsPalette.current.gray600
                    )
                }

                AsyncImage(
                    model = R.drawable.icon_insert_photo_32,
                    contentDescription = null,
                    modifier = modifier.size(40.dp)
                )
            }

            Spacer(modifier = modifier.height(6.dp))

            NicknameSection(textFieldState = state.textFieldState)

            PlantCategorySection(
                plantCategoryName = state.plantCategory,
                onClickCategory = onClickCategory
            )

            LightAmountBar(
                currentStep = state.lightAmount,
                onClickStep = onClickLightStep
            )

            PlantPlaceSection(
                selectedItem = state.place,
                onClickPlace = onClickPlace
            )
        }

        if (isAddButtonEnabled) {
            AddButton(
                modifier = Modifier.align(Alignment.BottomCenter),
                onclick = onClickAddButton
            )
        }
    }
}

@Composable
private fun NicknameSection(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState = TextFieldState(),
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color(0xFFFFFFFF))
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = "식물 별명",
            style = DiaryTypography.bodyMediumRegular,
            color = if (isFocused) DiaryColorsPalette.current.green400 else DiaryColorsPalette.current.gray600
        )
        BasicTextField(
            state = textFieldState,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            lineLimits = TextFieldLineLimits.SingleLine,
            inputTransformation = InputTransformation.maxLength(12),
            decorator = { innerTextField ->
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (textFieldState.text.isEmpty()) {
                            Text(
                                text = "식물이에게 별명을 지어주세요. 예)쑥쑥몬스테라",
                                style = DiaryTypography.bodyMediumSemiBold,
                                color = DiaryColorsPalette.current.gray400
                            )
                        } else {
                            innerTextField()
                        }
                        Spacer(modifier = modifier.weight(1f))
                        Text(
                            text = textFieldState.text.length.toString(),
                            style = DiaryTypography.bodyLargeRegular,
                            color = DiaryColorsPalette.current.green400
                        )
                        Text(
                            text = "/12",
                            style = DiaryTypography.bodyLargeRegular,
                            color = Color(0xFFCCCCCC)
                        )
                    }
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                if (isFocused) DiaryColorsPalette.current.green400
                                else DiaryColorsPalette.current.gray400
                            )
                    )
                }
            }
        )
    }
}

@Composable
private fun PlantCategorySection(
    modifier: Modifier = Modifier,
    plantCategoryName: String? = null,
    onClickCategory: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color(0xFFFFFFFF))
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = "식물 종류",
            style = DiaryTypography.bodyMediumRegular,
            color = DiaryColorsPalette.current.gray600
        )
        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { onClickCategory(plantCategoryName ?: "") }
        ) {
            Text(
                text = plantCategoryName ?: "예) 몬스테라, 방울토마토, 스위트바질",
                style = DiaryTypography.bodyMediumSemiBold,
                color = if (plantCategoryName == null) DiaryColorsPalette.current.gray400 else DiaryColorsPalette.current.gray900,
                modifier = modifier
                    .align(Alignment.CenterStart)
                    .padding(vertical = 16.dp)
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icon_magnification_24),
                tint = DiaryColorsPalette.current.gray800,
                contentDescription = null,
                modifier = modifier.align(Alignment.CenterEnd)
            )
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(DiaryColorsPalette.current.gray400)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun LightAmountBar(
    modifier: Modifier = Modifier,
    currentStep: LightAmount = LightAmount.NOT_SET,
    stepCount: Int = 4,
    onClickStep: (Int) -> Unit = {},
) {

    val animatedProgress by animateFloatAsState(
        targetValue = currentStep.value.toFloat() / (stepCount - 1),
        animationSpec = tween(durationMillis = 300, easing = EaseInOutCubic)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = "햇빛",
            style = DiaryTypography.bodyMediumRegular,
            color = DiaryColorsPalette.current.gray600
        )
        Spacer(modifier = modifier.height(12.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "없음",
                style = DiaryTypography.captionLargeRegular,
                color = DiaryColorsPalette.current.gray500
            )
            Text(
                text = "많음",
                style = DiaryTypography.captionLargeRegular,
                color = DiaryColorsPalette.current.gray500
            )
        }
        Spacer(modifier = modifier.height(6.dp))

        BoxWithConstraints(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val circleSize = 20.dp
            val innerDotSize = 12.dp
            val availableWidth = maxWidth - circleSize

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(
                            color = DiaryColorsPalette.current.gray200,
                            shape = CircleShape
                        )
                        .align(Alignment.Center)
                )

                Box(
                    modifier = modifier
                        .fillMaxWidth(animatedProgress.coerceAtLeast(0f))
                        .height(4.dp)
                        .background(
                            color = DiaryColorsPalette.current.green300,
                            shape = CircleShape
                        )
                        .align(Alignment.CenterStart)

                )
                repeat(stepCount) { index ->
                    val isActive = index <= currentStep.value
                    val fraction = index.toFloat() / (stepCount - 1)
                    val x = availableWidth * fraction

                    if (isActive) {
                        Box(
                            modifier = modifier
                                .size(circleSize)
                                .offset(x = x)
                                .background(
                                    color = DiaryColorsPalette.current.green300,
                                    shape = CircleShape
                                )
                                .clickable { onClickStep(index) }
                        ) {
                            Box(
                                modifier = modifier
                                    .size(innerDotSize)
                                    .background(
                                        color = DiaryColorsPalette.current.green50,
                                        shape = CircleShape
                                    )
                                    .align(Alignment.Center)
                            )
                        }
                    } else {
                        Box(
                            modifier = modifier
                                .size(circleSize)
                                .offset(x = x)
                                .background(
                                    color = DiaryColorsPalette.current.gray200,
                                    shape = CircleShape
                                )
                                .clickable { onClickStep(index) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = modifier.height(8.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = currentStep.helperText,
                style = DiaryTypography.bodyMediumSemiBold,
                color = DiaryColorsPalette.current.gray600,
                modifier = modifier
                    .wrapContentSize()
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val textWidth = placeable.width
                        val containerWidth = constraints.maxWidth

                        val fraction = currentStep.value.toFloat() / (stepCount - 1)
                        val circleX = (fraction * (containerWidth - 40.dp.toPx())).toInt()
                        val targetX = circleX + 10.dp.toPx() - (textWidth / 2)

                        val finalX = when {
                            targetX < 0 -> 0
                            targetX + textWidth > containerWidth -> (containerWidth - textWidth).coerceAtLeast(
                                0
                            )

                            else -> targetX.toInt()
                        }

                        layout(containerWidth, placeable.height) {
                            placeable.placeRelative(finalX, 0)
                        }
                    }
            )
        }
    }
}

@Composable
private fun PlantPlaceSection(
    modifier: Modifier = Modifier,
    selectedItem: PlantPlace? = null,
    onClickPlace: (PlantPlace) -> Unit = {}
) {
    val placeList = remember { PlantPlace.entries }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = "장소",
            style = DiaryTypography.bodyMediumRegular,
            color = DiaryColorsPalette.current.gray600
        )

        FlowRow(
            modifier = modifier
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            placeList.forEach {
                SelectChip(
                    modifier = modifier,
                    item = it,
                    selectedItem = selectedItem,
                    onClick = { onClickPlace(it) }
                )
            }
        }
    }
}

@Composable
fun SelectChip(
    modifier: Modifier = Modifier,
    item: PlantPlace,
    selectedItem: PlantPlace? = null,
    onClick: (PlantPlace) -> Unit = {}
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(40.dp))
            .then(
                if (item == selectedItem) {
                    modifier.background(color = DiaryColorsPalette.current.green400)
                } else {
                    modifier
                        .border(width = 1.dp, color = DiaryColorsPalette.current.gray200)
                        .background(color = DiaryColorsPalette.current.gray100)
                }
            )
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick(item)
            }
    ) {
        Text(
            text = item.displayName,
            color = if (item == selectedItem) DiaryColorsPalette.current.gray50 else DiaryColorsPalette.current.gray600,
            style = DiaryTypography.bodyMediumMedium,
            modifier = modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun AddButton(
    modifier: Modifier = Modifier,
    onclick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .fillMaxWidth()
            .background(
                color = DiaryColorsPalette.current.green500,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onclick() }
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "추가하기",
            style = DiaryTypography.subtitleMediumBold,
            color = DiaryColorsPalette.current.gray50,
            modifier = modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun SaveSuccessContent(
    onClickHome: () -> Unit = {},
    onClickDiary: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(top = 72.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "키우는 식물을 등록했어요!",
            color = Color(0xFF333333),
            style = DiaryTypography.headlineSmallBold
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .background(
                    color = Color(0xFF03D379),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onClickHome() }
                .clip(RoundedCornerShape(16.dp))
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "홈으로 돌아가기",
                color = DiaryColorsPalette.current.gray50,
                style = DiaryTypography.subtitleMediumBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .background(
                    color = DiaryColorsPalette.current.green50,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onClickDiary() }
                .clip(RoundedCornerShape(16.dp))
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "일지 작성하러 가기",
                color = DiaryColorsPalette.current.green600,
                style = DiaryTypography.subtitleMediumBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
private fun PlantInfoInputScreenPreview() {
    SsukssukDiaryTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            PlantInfoInputScreen(
                state = PlantAddState.Input(
                    lightAmount = LightAmount.HIGH,
                    place = PlantPlace.LIVINGROOM
                ),
                isAddButtonEnabled = true,
            )
        }
    }
}

@Preview
@Composable
private fun SaveSuccessContentPreview() {
    SsukssukDiaryTheme {
        SaveSuccessContent()
    }
}