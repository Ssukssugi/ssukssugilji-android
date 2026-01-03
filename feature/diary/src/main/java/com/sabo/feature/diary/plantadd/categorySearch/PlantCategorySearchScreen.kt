package com.sabo.feature.diary.plantadd.categorySearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.component.NavigationType
import com.sabo.core.designsystem.component.SsukssukTopAppBar
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme

@Composable
internal fun PlantCategorySearchScreen(
    viewModel: PlantCategorySearchViewModel = hiltViewModel(),
    onClickBack: () -> Unit = {},
    onClickCategory: (String) -> Unit = {}
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        SsukssukTopAppBar(
            navigationType = NavigationType.BACK,
            containerColor = Color.White,
            onNavigationClick = onClickBack
        )

        PlantCategorySearchContent(
            state = state,
            onClickCategory = onClickCategory
        )
    }
}

@Composable
private fun PlantCategorySearchContent(
    state: PlantSearchState,
    onClickCategory: (String) -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Text(
            text = "키우는 식물의 종류를 검색해보세요",
            color = DiaryColorsPalette.current.gray900,
            style = DiaryTypography.subtitleMediumBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = "식물 종류",
            style = DiaryTypography.bodyMediumRegular,
            color = DiaryColorsPalette.current.gray600,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        BasicTextField(
            state = state.textFieldState,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .onFocusChanged { isFocused = it.isFocused }
                .focusRequester(focusRequester),
            lineLimits = TextFieldLineLimits.SingleLine,
            onKeyboardAction = KeyboardActionHandler {
                onClickCategory(state.textFieldState.text.toString())
            },
            decorator = { innerTextField ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        innerTextField()

                        if (state.textFieldState.text.isEmpty()) {
                            Text(
                                text = "예) 몬스테라, 방울토마토, 스위트바질",
                                style = DiaryTypography.bodyMediumSemiBold,
                                color = DiaryColorsPalette.current.gray400
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
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

        if (state.isLoading.not() && state.hasSearched) {
            SearchResultListContent(
                keyword = state.textFieldState.text.toString(),
                results = state.searchResult,
                onClickCategory = onClickCategory
            )
        } else {
            Spacer(
                modifier = Modifier.weight(1f, true)
            )
        }

        CompleteButton(
            onClick = { onClickCategory(state.textFieldState.text.toString()) }
        )
    }
}

@Composable
private fun ColumnScope.SearchResultListContent(
    keyword: String = "",
    results: List<PlantResult>,
    onClickCategory: (String) -> Unit
) {
    val count = remember(results) { results.size }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f, fill = true)
            .padding(top = 8.dp)
    ) {
        Text(
            text = if (keyword.isEmpty()) {
                AnnotatedString("")
            } else {
                buildAnnotatedString {
                    append("총 ")
                    withStyle(
                        style = SpanStyle(color = DiaryColorsPalette.current.green400)
                    ) {
                        append("${count}개")
                    }
                    append("의 검색 결과가 있어요.")
                }
            },
            color = DiaryColorsPalette.current.gray600,
            style = DiaryTypography.bodyMediumSemiBold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        if (keyword.isNotEmpty() && count == 0) {
            EmptyScreen(
                category = keyword,
                onClickCompleteButton = onClickCategory
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(
                    items = results,
                    key = { it.category }
                ) { result ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClickCategory(result.category) }
                            .padding(horizontal = 8.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = result.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(color = DiaryColorsPalette.current.gray200)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = buildAnnotatedString {
                                val name = result.category

                                if (keyword.isNotEmpty()) {
                                    val startIndex = name.indexOf(keyword, ignoreCase = true)
                                    if (startIndex >= 0) {
                                        append(name.substring(0, startIndex))
                                        withStyle(style = SpanStyle(color = DiaryColorsPalette.current.green400)) {
                                            append(name.substring(startIndex, startIndex + keyword.length))
                                        }
                                        append(name.substring(startIndex + keyword.length))
                                    } else {
                                        append(name)
                                    }
                                } else {
                                    append(name)
                                }
                            },
                            style = DiaryTypography.bodySmallSemiBold,
                            color = DiaryColorsPalette.current.gray800,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyScreen(
    category: String,
    onClickCompleteButton: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.img_plant_category_empty),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 24.dp)
                .size(80.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "'${category}'\n검색결과가 없어요",
            style = DiaryTypography.headlineSmallBold,
            color = DiaryColorsPalette.current.gray900,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "새로운 식물로 입력할까요?",
            style = DiaryTypography.subtitleMediumRegular,
            color = DiaryColorsPalette.current.gray600,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(color = DiaryColorsPalette.current.green500)
                .clickable { onClickCompleteButton(category) }
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "'${category}'로 입력하기",
                style = DiaryTypography.subtitleMediumBold,
                color = DiaryColorsPalette.current.gray50,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun CompleteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = DiaryColorsPalette.current.green500)
            .clickable { onClick() }
            .padding(vertical = 14.dp)
    ) {
        Text(
            text = "확인",
            style = DiaryTypography.subtitleMediumBold,
            color = DiaryColorsPalette.current.gray50,
            modifier = modifier.align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun PlantCategorySearchContentPreview() {
    SsukssukDiaryTheme {
        PlantCategorySearchContent(
            state = PlantSearchState()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyScreenPreview() {
    SsukssukDiaryTheme {
        EmptyScreen(
            category = "방울토마토"
        )
    }
}