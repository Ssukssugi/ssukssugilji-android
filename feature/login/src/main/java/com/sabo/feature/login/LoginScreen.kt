package com.sabo.feature.login

import android.view.Gravity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.core.model.LoginType
import kotlinx.coroutines.launch
import com.sabo.core.designsystem.R as dsR

@Composable
internal fun LoginRoute(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToSignUp: () -> Unit,
    navigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val kakaoLoginManager = rememberKakaoLoginManager {
        viewModel.onSuccessKakaoLogin(it)
    }
    val googleLoginManager = rememberGoogleLoginManager {
        viewModel.onSuccessGoogleLogin(it)
    }

    LaunchedEffect(Unit) {
        viewModel.loginEvent.collect {
            when (it) {
                LoginEvent.GoToMain -> navigateToHome()
                LoginEvent.GoToSignUp -> navigateToSignUp()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LoginContent(
            modifier = modifier,
            uiState = uiState,
            onClickKakaoLogin = kakaoLoginManager::requestToken,
            onClickGoogleLogin = googleLoginManager::requestToken,
            onSuccessNaverLogin = viewModel::onSuccessNaverLogin,
            onClickTermAgreeItem = { viewModel.changeAgreeTermState(it) },
            onClickNextButton = viewModel::applyTermsAgreement
        )
    }
}

@Composable
private fun LoginContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onClickKakaoLogin: () -> Unit,
    onClickGoogleLogin: suspend () -> Unit,
    onSuccessNaverLogin: (String) -> Unit,
    onClickTermAgreeItem: (TermsAgreeState) -> Unit,
    onClickNextButton: () -> Unit
) {
    when (uiState) {
        is LoginUiState.BeforeLogin -> LoginScreen(
            modifier = modifier,
            onClickKakaoLogin = onClickKakaoLogin,
            onClickGoogleLogin = onClickGoogleLogin,
            onSuccessNaverLogin = onSuccessNaverLogin,
            isShownTermsAgreeDialog = uiState.isShownTermsAgree,
            termsState = uiState.termsState,
            onClickTermAgreeItem = onClickTermAgreeItem,
            onClickNextButton = onClickNextButton
        )

        LoginUiState.SignUpLoading -> RedirectLoadingScreen()
        is LoginUiState.SuccessLogin -> LoginSuccessScreen(state = uiState)
    }
}

@Composable
private fun LoginScreen(
    modifier: Modifier = Modifier,
    onClickKakaoLogin: () -> Unit,
    onClickGoogleLogin: suspend () -> Unit,
    onSuccessNaverLogin: (String) -> Unit,
    isShownTermsAgreeDialog: Boolean = false,
    termsState: TermsAgreeState,
    onClickTermAgreeItem: (TermsAgreeState) -> Unit,
    onClickNextButton: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 80.dp, bottom = 188.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = dsR.drawable.img_home_logo),
            contentDescription = null,
            modifier = Modifier
                .width(156.dp)
                .height(40.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.login_title),
            style = DiaryTypography.subtitleLargeBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = dsR.drawable.img_logo_cat),
            contentDescription = null,
            modifier = Modifier.size(224.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.login_sns_help),
            style = DiaryTypography.bodyLargeBold,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            LoginIcon(
                resId = R.drawable.img_login_google,
                onClick = {
                    scope.launch {
                        onClickGoogleLogin()
                    }
                }
            )
            Spacer(modifier = modifier.width(16.dp))
            LoginIcon(
                resId = R.drawable.img_login_kakao,
                onClick = {
                    onClickKakaoLogin()
                }
            )
            Spacer(modifier = modifier.width(16.dp))
            LoginIcon(
                resId = R.drawable.img_login_naver,
                onClick = {
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
            )
        }
    }

    if (isShownTermsAgreeDialog) {
        TermAgreeDialog(
            termsState = termsState,
            onClickTermAgreeItem = onClickTermAgreeItem,
            onClickNextButton = onClickNextButton
        )
    }
}

@Composable
private fun TermAgreeDialog(
    modifier: Modifier = Modifier,
    termsState: TermsAgreeState,
    onDismissRequest: () -> Unit = {},
    onClickTermAgreeItem: (TermsAgreeState) -> Unit = {},
    onClickNextButton: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        (LocalView.current.parent as DialogWindowProvider).apply {
            window.setGravity(Gravity.BOTTOM)
        }
        Surface(
            shape = RoundedCornerShape(24.dp),
            modifier = modifier
                .padding(horizontal = 10.dp, vertical = 25.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(64.dp)
                        .height(4.dp)
                        .background(color = Color(0xFFDDDDDD))
                        .align(Alignment.CenterHorizontally),
                )
                Text(
                    text = "필수 약관에 동의해주세요",
                    style = DiaryTypography.subtitleLargeBold,
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(20.dp)
                )
                TermAgreeContent(
                    modifier = modifier,
                    state = termsState,
                    onClickTermAgreeItem = onClickTermAgreeItem,
                    onClickNextButton = onClickNextButton
                )
            }
        }
    }
}

@Composable
private fun TermAgreeContent(
    modifier: Modifier = Modifier,
    state: TermsAgreeState,
    onClickTermAgreeItem: (TermsAgreeState) -> Unit = {},
    onClickNextButton: () -> Unit = {}
) {
    TermAgreeContentItem(
        isChecked = state.isAllAgree(),
        item = {
            Text(
                text = "전체동의",
                style = DiaryTypography.bodyLargeMedium,
                color = DiaryColorsPalette.current.gray700
            )
        },
        onClick = {
            onClickTermAgreeItem(
                state.copy(
                    serviceTerms = state.isAllAgree().not(),
                    privateTerms = state.isAllAgree().not(),
                    ageTerms = state.isAllAgree().not(),
                    marketingTerms = state.isAllAgree().not()
                )
            )
        }
    )

    TermAgreeContentItem(
        isChecked = state.serviceTerms,
        item = {
            Text(
                text = "[필수] 서비스 이용약관",
                style = DiaryTypography.bodyMediumRegular,
                color = DiaryColorsPalette.current.gray700
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = ImageVector.vectorResource(com.sabo.core.designsystem.R.drawable.icon_arrow_right_24),
                contentDescription = null,
                tint = Color(0xFFCCCCCC)
            )
        },
        onClick = { onClickTermAgreeItem(state.copy(serviceTerms = state.serviceTerms.not())) }
    )

    TermAgreeContentItem(
        isChecked = state.privateTerms,
        item = {
            Text(
                text = "[필수] 개인정보 수집 / 이용동의서",
                style = DiaryTypography.bodyMediumRegular,
                color = DiaryColorsPalette.current.gray700
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = ImageVector.vectorResource(com.sabo.core.designsystem.R.drawable.icon_arrow_right_24),
                contentDescription = null,
                tint = Color(0xFFCCCCCC)
            )
        },
        onClick = { onClickTermAgreeItem(state.copy(privateTerms = state.privateTerms.not())) }
    )

    TermAgreeContentItem(
        isChecked = state.ageTerms,
        item = {
            Text(
                text = "[필수] 만 14세 이상 확인",
                style = DiaryTypography.bodyMediumRegular,
                color = DiaryColorsPalette.current.gray700
            )
        },
        onClick = { onClickTermAgreeItem(state.copy(ageTerms = state.ageTerms.not())) }
    )

    TermAgreeContentItem(
        isChecked = state.marketingTerms,
        item = {
            Text(
                text = "마케팅 정보 수신 동의",
                style = DiaryTypography.bodyMediumRegular,
                color = DiaryColorsPalette.current.gray700
            )
        },
        onClick = { onClickTermAgreeItem(state.copy(marketingTerms = state.marketingTerms.not())) }
    )

    Box(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = if (state.isRequiredAgree()) DiaryColorsPalette.current.green400
                else DiaryColorsPalette.current.green100
            )
            .clickable(
                enabled = state.isRequiredAgree(),
                onClick = { onClickNextButton() }
            )
    ) {
        Text(
            text = "시작하기",
            style = DiaryTypography.subtitleMediumBold,
            color = DiaryColorsPalette.current.gray50,
            modifier = modifier
                .padding(vertical = 16.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun TermAgreeContentItem(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    item: @Composable RowScope.() -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(com.sabo.core.designsystem.R.drawable.icon_check_24),
            contentDescription = null,
            tint = if (isChecked) DiaryColorsPalette.current.green400 else DiaryColorsPalette.current.gray400
        )
        Spacer(modifier = Modifier.width(8.dp))
        item()
    }
}

@Composable
private fun RedirectLoadingScreen(
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
private fun LoginMainInfo(
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
            style = DiaryTypography.headlineSmallBold,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally),
            text = subTitle,
            color = Color(0xFF777777),
            style = DiaryTypography.subtitleMediumMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoginSuccessScreen(
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
                title = "${
                    when (state.type) {
                        LoginType.KAKAO -> "카카오"
                        LoginType.GOOGLE -> "구글"
                        LoginType.NAVER -> "네이버"
                    }
                }로 로그인했어요",
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

@Composable
private fun RowScope.LoginIcon(
    modifier: Modifier = Modifier,
    @DrawableRes resId: Int,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .size(56.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .align(Alignment.CenterVertically),
        shape = RoundedCornerShape(100.dp),
    ) {
        Image(
            modifier = modifier.fillMaxSize(),
            painter = painterResource(resId),
            contentDescription = null
        )
    }
}

@Composable
fun rememberKakaoLoginManager(
    onSuccess: (String) -> Unit
): KakaoLoginManager {
    val context = LocalContext.current
    return KakaoLoginManager(context, object : LoginManager.CallbackListener {
        override fun onSuccess(token: String) {
            onSuccess(token)
        }
    })
}

@Composable
fun rememberGoogleLoginManager(
    onSuccess: (String) -> Unit
): GoogleLoginManager {
    val context = LocalContext.current
    return GoogleLoginManager(context, object : LoginManager.CallbackListener {
        override fun onSuccess(token: String) {
            onSuccess(token)
        }
    })
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onClickKakaoLogin = {},
        onClickGoogleLogin = {},
        onSuccessNaverLogin = {},
        termsState = TermsAgreeState(),
        onClickTermAgreeItem = {},
        onClickNextButton = {}
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

@Preview
@Composable
fun LoginIconPreview() {
    Row {
        LoginIcon(
            resId = R.drawable.img_login_kakao
        )
    }
}

@Preview
@Composable
private fun TermsAgreeDialogPreview() {
    SsukssukDiaryTheme {
        TermAgreeDialog(
            termsState = TermsAgreeState()
        )
    }
}