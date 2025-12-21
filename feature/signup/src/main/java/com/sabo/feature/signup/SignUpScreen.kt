package com.sabo.feature.signup

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.feature.signup.component.SignUpContentTitle
import com.sabo.feature.signup.component.SignUpTitle
import com.sabo.feature.signup.model.AgeChip
import com.sabo.feature.signup.model.HowKnownChip
import com.sabo.feature.signup.model.PlantReasonChip
import com.sabo.feature.signup.model.SignUpEvent
import com.sabo.feature.signup.model.SignUpStep
import com.sabo.feature.signup.model.SignUpUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun SignUpRoute(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
    onCompletedSignUp: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest {
            when (it) {
                SignUpEvent.OnCompletedSignUp -> onCompletedSignUp()
            }
        }
    }

    SignUpContent(
        modifier = modifier,
        uiState = uiState,
        onClickNicknameCheck = viewModel::checkIsNicknameDuplicated,
        onClickBackButton = viewModel::onClickBackScreen,
        onClickNextButton = viewModel::moveToNextStep,
        onClickAgeChip = { viewModel.selectAge(it) },
        onClickPlantReasonChip = { viewModel.selectPlantReason(it) },
        onClickHowKnownChip = { viewModel.selectHowKnown(it) },
        onClickSkipButton = viewModel::skipSignUp
    )
}

@Composable
private fun SignUpContent(
    modifier: Modifier = Modifier,
    uiState: SignUpUiState,
    onClickNicknameCheck: () -> Unit = {},
    onClickBackButton: () -> Unit = {},
    onClickNextButton: () -> Unit = {},
    onClickAgeChip: (AgeChip) -> Unit = {},
    onClickPlantReasonChip: (PlantReasonChip) -> Unit = {},
    onClickHowKnownChip: (HowKnownChip) -> Unit = {},
    onClickSkipButton: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (uiState.step == SignUpStep.NICKNAME) {
            NicknameCreate(
                nicknameState = uiState.nickname,
                errorState = uiState.nicknameErrorState,
                onClickNextButton = onClickNicknameCheck
            )
        } else {
            SignUpExtraInfoScreen(
                modifier = modifier,
                uiState = uiState,
                onClickBackButton = onClickBackButton,
                onClickSkipButton = onClickSkipButton,
                onClickNextButton = onClickNextButton,
                onClickAgeChip = onClickAgeChip,
                onClickPlantReasonChip = onClickPlantReasonChip,
                onClickHowKnownChip = onClickHowKnownChip
            )
        }
    }
}

@Composable
private fun NicknameCreate(
    modifier: Modifier = Modifier,
    nicknameState: TextFieldState,
    errorState: SignUpUiState.NicknameErrorState,
    onClickNextButton: () -> Unit = {}
) {
    val regex = remember { "^[가-힣A-Za-z0-9]{1,12}$".toRegex() }
    var isValid by remember { mutableStateOf(false) }

    LaunchedEffect(nicknameState) {
        snapshotFlow { nicknameState.text }
            .map { input -> regex.matches(input) }
            .distinctUntilChanged()
            .collect { isValid = it }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SignUpTopBar()
        SignUpTitle(
            mainTitle = "원하는 닉네임이\n있으신가요?",
            subTitle = "서비스를 사용할 닉네임을 설정해보세요"
        )

        Spacer(modifier = modifier.height(8.dp))

        SignUpContentTitle(text = "닉네임")

        NickNameInput(
            nicknameState = nicknameState,
            errorState = errorState
        )

        Spacer(modifier = modifier.weight(1f))

        NextButton(
            modifier = modifier
                .fillMaxWidth(),
            isActive = isValid,
            text = "확인",
            onClicked = onClickNextButton
        )
    }
}

@Composable
private fun NickNameInput(
    modifier: Modifier = Modifier,
    nicknameState: TextFieldState,
    errorState: SignUpUiState.NicknameErrorState
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }
    BasicTextField(
        state = nicknameState,
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .focusRequester(focusRequester = focusRequester),
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
                        .padding(vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (nicknameState.text.isEmpty()) {
                        Text(
                            text = "한글, 숫자, 영문 12글자만 입력 가능해요",
                            color = DiaryColorsPalette.current.gray400,
                            style = DiaryTypography.bodyLargeRegular
                        )
                    } else {
                        innerTextField()
                    }
                    Spacer(modifier = modifier.weight(1f))
                    Text(
                        modifier = modifier,
                        text = nicknameState.text.length.toString(),
                        color = DiaryColorsPalette.current.green400,
                        style = DiaryTypography.bodyLargeRegular
                    )
                    Text(
                        modifier = modifier,
                        text = "/12",
                        color = DiaryColorsPalette.current.gray600,
                        style = DiaryTypography.bodyLargeRegular
                    )
                }
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = DiaryColorsPalette.current.green400)
                )
            }
        }
    )
}

@Composable
private fun NextButton(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    text: String,
    onClicked: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .wrapContentHeight()
            .background(
                color = if (isActive) DiaryColorsPalette.current.green400 else DiaryColorsPalette.current.gray500
            )
            .clickable(
                enabled = isActive,
                onClick = { onClicked() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = modifier
                .padding(vertical = 14.dp)
                .wrapContentSize(),
            text = text,
            style = DiaryTypography.subtitleMediumBold,
            color = DiaryColorsPalette.current.gray50
        )
    }
}

@Composable
private fun SignUpExtraInfoScreen(
    modifier: Modifier = Modifier,
    uiState: SignUpUiState,
    onClickBackButton: () -> Unit = {},
    onClickSkipButton: () -> Unit = {},
    onClickNextButton: () -> Unit = {},
    onClickAgeChip: (AgeChip) -> Unit = {},
    onClickPlantReasonChip: (PlantReasonChip) -> Unit = {},
    onClickHowKnownChip: (HowKnownChip) -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { SignUpExtraStep.entries.size })

    LaunchedEffect(uiState.step) {
        val page = uiState.step.ordinal - 1
        if (page in 0..pagerState.pageCount) {
            pagerState.animateScrollToPage(page = page)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SignUpTopBar(
            modifier = modifier,
            leftContent = {
                TopBarLeftControl(
                    modifier = modifier,
                    onClickBackButton = onClickBackButton
                )
            },
            rightContent = {
                TopBarRightControl(
                    onClickSkipButton = onClickSkipButton
                )
            }
        )

        Column(
            modifier = modifier
                .weight(1f)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HorizontalPager(
                modifier = modifier
                    .fillMaxSize(),
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> SignUpAgeContent(
                        modifier = modifier,
                        nickname = uiState.nickname.text.toString(),
                        selectedAge = uiState.age,
                        onClickItem = { onClickAgeChip(it) }
                    )
                    1 -> SignUpPlantReasonContent(
                        modifier = modifier,
                        selectedItems = uiState.plantReason,
                        onClickItem = { onClickPlantReasonChip(it) }
                    )
                    2 -> SignUpHowKnowContent(
                        modifier = modifier,
                        selectedItems = uiState.howKnown,
                        onClickItem = { onClickHowKnownChip(it) }
                    )
                }
            }
        }

        Spacer(modifier.height(16.dp))

        NextButton(
            modifier = modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(size = 16.dp)),
            isActive = uiState.age != null,
            text = if (pagerState.currentPage == pagerState.pageCount) "시작하기" else "다음",
            onClicked = onClickNextButton
        )

        Spacer(modifier = modifier.height(16.dp))
    }
}

@Composable
private fun SignUpTopBar(
    modifier: Modifier = Modifier,
    leftContent: @Composable () -> Unit = {},
    rightContent: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leftContent()
        Spacer(
            modifier = modifier
                .fillMaxHeight()
                .weight(1f)
        )
        rightContent()
    }
}

@Composable
private fun TopBarLeftControl(
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit
) {
    IconButton(
        modifier = modifier
            .fillMaxHeight()
            .wrapContentWidth(),
        onClick = onClickBackButton,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.icon_arrow_left),
            contentDescription = null,
            tint = DiaryColorsPalette.current.gray900
        )
    }
}

@Composable
private fun TopBarRightControl(
    modifier: Modifier = Modifier,
    onClickSkipButton: () -> Unit
) {
    Text(
        text = "건너뛰기",
        style = DiaryTypography.bodyMediumRegular,
        color = DiaryColorsPalette.current.gray600,
        modifier = modifier
            .padding(end = 20.dp)
            .clickable {
                onClickSkipButton()
            }
    )
}

private enum class SignUpExtraStep {
    AGE, PLANT_REASON, HOW_KNOWN
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun CreateNicknamePreview() {
    SsukssukDiaryTheme {
        NicknameCreate(
            nicknameState = TextFieldState("씩씩한몬스테라"),
            errorState = SignUpUiState.NicknameErrorState.NONE,
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun SignUpTopBarPreview() {
    SsukssukDiaryTheme {
        SignUpExtraInfoScreen(
            uiState = SignUpUiState(),
            onClickSkipButton = {},
            onClickBackButton = {},
            onClickNextButton = {}
        )
    }
}
