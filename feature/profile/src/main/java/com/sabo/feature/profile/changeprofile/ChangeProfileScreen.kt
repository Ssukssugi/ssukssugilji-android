package com.sabo.feature.profile.changeprofile

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.NavigationType
import com.sabo.core.designsystem.component.SsukssukTopAppBar
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun ChangeProfileScreen(
    viewModel: ChangeProfileViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onSucceedSave: () -> Unit
) {
    val state = viewModel.collectAsState().value
    val isSaveEnabled by viewModel.isSaveEnabled.collectAsStateWithLifecycle()

    viewModel.collectSideEffect {
        when (it) {
            is ChangeProfileSideEffect.FinishWithResult -> onSucceedSave()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
    ) {
        SsukssukTopAppBar(
            navigationType = NavigationType.BACK,
            onNavigationClick = onClickBack,
            containerColor = Color(0xFFFFFFFF),
            content = {
                Text(
                    text = "${state.nickname}님의 정보",
                    style = DiaryTypography.bodySmallBold,
                    color = DiaryColorsPalette.current.gray900,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f, fill = true)
                )
                Spacer(modifier = Modifier.width(52.dp))
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
                .background(color = Color(0xFFFFFFFF))
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.Center)
                )
            } else {
                ChangeProfileScreenContent(
                    textFieldState = state.textFieldState,
                    errorState = state.errorState,
                    onClickSave = viewModel::changeNickname,
                    isSavable = isSaveEnabled
                )
            }
        }
    }
}

@Composable
private fun ChangeProfileScreenContent(
    textFieldState: TextFieldState = TextFieldState(),
    errorState: ChangeProfileUiState.ErrorType = ChangeProfileUiState.ErrorType.NONE,
    onClickSave: () -> Unit = {},
    isSavable: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_logo_cat),
                contentDescription = null,
                modifier = Modifier
                    .size(108.dp)
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "닉네임",
            color = DiaryColorsPalette.current.gray600,
            style = DiaryTypography.bodyMediumRegular,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        BasicTextField(
            state = textFieldState,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            lineLimits = TextFieldLineLimits.SingleLine,
            inputTransformation = InputTransformation.maxLength(12),
            textStyle = DiaryTypography.bodyMediumSemiBold,
            decorator = { innerTextField ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (textFieldState.text.isEmpty()) {
                            Text(
                                text = "한글, 숫자, 영문 12글자만 입력 가능해요",
                                color = DiaryColorsPalette.current.gray400,
                                style = DiaryTypography.bodyLargeRegular
                            )
                        } else {
                            innerTextField()
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = textFieldState.text.length.toString(),
                            color = DiaryColorsPalette.current.green400,
                            style = DiaryTypography.bodyLargeRegular
                        )
                        Text(
                            text = "/12",
                            color = DiaryColorsPalette.current.gray600,
                            style = DiaryTypography.bodyLargeRegular
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(color = DiaryColorsPalette.current.gray600)
                    )
                    Text(
                        text = errorState.helper,
                        color = DiaryColorsPalette.current.red500,
                        style = DiaryTypography.captionLargeMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxWidth()
                .background(
                    color = if (isSavable) DiaryColorsPalette.current.green500 else DiaryColorsPalette.current.green100,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(shape = RoundedCornerShape(16.dp))
                .clickable { if (isSavable) onClickSave() }
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "수정 완료하기",
                style = DiaryTypography.subtitleMediumBold,
                color = DiaryColorsPalette.current.gray50,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
private fun ChangeProfileScreenPreview() {
    SsukssukDiaryTheme {
        ChangeProfileScreenContent()
    }
}