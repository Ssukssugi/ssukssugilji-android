package com.sabo.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback

@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        LoginContent(
            uiState = uiState,
            onClickKakaoLogin = viewModel::loginWithKakao,
            onClickNaverLogin = viewModel::onRedirectLogin,
            onSuccessNaverLogin = viewModel::onSuccessNaverLogin
        )
    }
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onClickKakaoLogin: () -> Unit,
    onClickNaverLogin: () -> Unit,
    onSuccessNaverLogin: (String) -> Unit
) {
    when (uiState) {
        LoginUiState.BeforeLogin -> LoginScreen(
            onClickNaverLogin = onClickNaverLogin,
            onClickKakaoLogin = onClickKakaoLogin,
            onSuccessNaverLogin = onSuccessNaverLogin
        )
        LoginUiState.RedirectLoading -> RedirectLoadingScreen()
        is LoginUiState.SuccessLogin -> LoginSuccessScreen(state = uiState)
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onClickKakaoLogin: () -> Unit,
    onClickNaverLogin: () -> Unit,
    onSuccessNaverLogin: (String) -> Unit
) {

    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = modifier
                .wrapContentSize()
                .align(Alignment.Center)
        ) {
            Box(
                modifier = modifier
                    .wrapContentSize()
                    .background(Color.Yellow)
                    .clickable { onClickKakaoLogin() }
                    .padding(horizontal = 24.dp, vertical = 18.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "카카오톡으로 로그인"
                )
            }

            Spacer(
                modifier = modifier
                    .height(20.dp)
            )

            Box(
                modifier = modifier
                    .wrapContentSize()
                    .background(Color.Green)
                    .clickable {
                        onClickNaverLogin()
                        NaverIdLoginSDK.authenticate(context, object : OAuthLoginCallback {
                            override fun onSuccess() {
                                NaverIdLoginSDK.getAccessToken()?.let { token ->
                                    onSuccessNaverLogin(token)
                                }
                            }
                            override fun onError(errorCode: Int, message: String) {
                                //TODO("Not yet implemented")
                            }
                            override fun onFailure(httpStatus: Int, message: String) {
                                //TODO("Not yet implemented")
                            }
                        })
                    }
                    .padding(horizontal = 24.dp, vertical = 18.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "네이버로 로그인"
                )
            }
        }
    }
}

@Composable
fun RedirectLoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = modifier
                .wrapContentSize()
                .align(Alignment.TopCenter)
        ) {
            LoginMainInfo(
                title = "잠시만 기다려주세요",
                subTitle = "로그인하고 있어요"
            )
            Box(
                modifier = modifier
                    .padding(top = 68.dp)
                    .size(320.dp)
                    .background(color = Color.Gray)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun LoginMainInfo(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String
) {
    Column(
        modifier = modifier
            .padding(top = 72.dp)
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            text = title,
            color = Color(0xFF333333),
            style = TextStyle(
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            textAlign = TextAlign.Center
        )

        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally),
            text = subTitle,
            color = Color(0xFF777777),
            style = TextStyle(
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoginSuccessScreen(
    modifier: Modifier = Modifier,
    state: LoginUiState.SuccessLogin
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = modifier
                .wrapContentSize()
                .align(Alignment.TopCenter)
        ) {
            LoginMainInfo(
                title = "${state.type.text}로 로그인했어요",
                subTitle = "필수약관에 동의하고\n쑥쑥일지를 시작해보세요"
            )
            Box(
                modifier = modifier
                    .padding(top = 68.dp)
                    .size(320.dp)
                    .background(color = Color.Gray)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview(){
    LoginScreen(
        onClickKakaoLogin = {},
        onSuccessNaverLogin = {},
        onClickNaverLogin = {}
    )
}

@Preview
@Composable
fun RedirectLoadingScreenPreview() {
    RedirectLoadingScreen()
}

@Preview
@Composable
fun LoginMainInfoPreview() {
    LoginMainInfo(
        title = "잠시만 기다려주세요",
        subTitle = "로그인하고 있어요"
    )
}