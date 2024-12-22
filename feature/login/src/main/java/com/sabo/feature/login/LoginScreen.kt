package com.sabo.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback

@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    LoginScreen(
        modifier = modifier,
        onClickKakaoLogin = viewModel::loginWithKakao,
        onSuccessNaverLogin = viewModel::onSuccessNaverLogin
    )
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onClickKakaoLogin: () -> Unit,
    onSuccessNaverLogin: () -> Unit
) {

    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
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
                        NaverIdLoginSDK.authenticate(context, object : OAuthLoginCallback {
                            override fun onSuccess() {
                                onSuccessNaverLogin()
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

@Preview
@Composable
fun LoginScreenPreview(){
    LoginScreen(
        onClickKakaoLogin = {},
        onSuccessNaverLogin = {}
    )
}