package com.sabo.feature.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun SignUpRoute(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignUpContent(
        modifier = modifier,
        uiState = uiState
    )
}

@Composable
private fun SignUpContent(
    modifier: Modifier = Modifier,
    uiState: SignUpUiState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        when(uiState) {
            is SignUpUiState.CreateNickname -> NicknameCreate(
                initialNickname = uiState.nickname,
                errorState = uiState.errorState
            )
        }
    }
}

@Composable
private fun NicknameCreate(
    modifier: Modifier = Modifier,
    initialNickname: String,
    errorState: SignUpUiState.CreateNickname.ErrorState
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Title(
            mainTitle = "원하는 닉네임이\n있으신가요?",
            subTitle = "서비스를 사용할 닉네임을 설정해보세요"
        )

        Spacer(modifier = modifier.height(8.dp))

        ContentTitle(text = "닉네임", colorCode = 0xFF3283F7)

        NickNameInput(
            initialNickname = initialNickname,
            errorState = errorState
        )
    }
}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    mainTitle: String,
    subTitle: String = ""
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth(),
            text = mainTitle,
            color = Color(0xFF333333),
            style = TextStyle(
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )

        Spacer(modifier = modifier.height(8.dp))

        Text(
            modifier = modifier
                .fillMaxWidth(),
            text = subTitle,
            color = Color(0xFF777777),
            style = TextStyle(
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        )
    }
}

@Composable
private fun ContentTitle(
    modifier: Modifier = Modifier,
    text: String,
    colorCode: Long
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        text = text,
        color = Color(colorCode),
        style = TextStyle(
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
    )
}

@Composable
private fun NickNameInput(
    modifier: Modifier = Modifier,
    initialNickname: String = "",
    errorState: SignUpUiState.CreateNickname.ErrorState
) {
    var inputted by remember { mutableStateOf(initialNickname) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp)
    ) {
        TextField(
            modifier = modifier
                .fillMaxWidth(),
            value = inputted,
            onValueChange = {
                inputted = it
            },
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateNicknamePreview() {
    NicknameCreate(
        initialNickname = "씩씩한몬스테라",
        errorState = SignUpUiState.CreateNickname.ErrorState.NONE
    )
}

@Preview(showBackground = true)
@Composable
private fun TitleTextPreview() {
    Title(
        mainTitle = "원하는 닉네임이\n있으신가요?"
    )
}