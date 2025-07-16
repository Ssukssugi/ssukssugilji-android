package com.sabo.feature.diary.plantadd.categorySearch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.designsystem.theme.SsukssukDiaryTheme
import com.sabo.core.designsystem.theme.component.NavigationType
import com.sabo.core.designsystem.theme.component.SsukssukTopAppBar

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
                .onFocusChanged { isFocused = it.isFocused },
            lineLimits = TextFieldLineLimits.SingleLine,
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
                        if (state.textFieldState.text.isEmpty()) {
                            Text(
                                text = "예) 몬스테라, 방울토마토, 스위트바질",
                                style = DiaryTypography.bodyMediumSemiBold,
                                color = DiaryColorsPalette.current.gray400
                            )
                        } else {
                            innerTextField()
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

        SearchResultListContent(
            keyword = state.textFieldState.text.toString(),
            results = state.searchResult,
            onClickCategory = onClickCategory
        )
    }
}

@Composable
private fun ColumnScope.SearchResultListContent(
    keyword: String = "",
    results: List<String>,
    onClickCategory: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f, fill = true),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(
            items = results,
            key = { it }
        ) { result ->
            Text(
                text = buildAnnotatedString {
                    if (keyword.isNotEmpty()) {
                        val startIndex = result.indexOf(keyword, ignoreCase = true)
                        if (startIndex >= 0) {
                            append(result.substring(0, startIndex))
                            withStyle(style = SpanStyle(color = DiaryColorsPalette.current.gray400)) {
                                append(result.substring(startIndex, startIndex + keyword.length))
                            }
                            append(result.substring(startIndex + keyword.length))
                        } else {
                            append(result)
                        }
                    } else {
                        append(result)
                    }
                },
                style = DiaryTypography.bodySmallSemiBold,
                color = DiaryColorsPalette.current.gray800,
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 8.dp)
                    .clickable { onClickCategory(result) },
                maxLines = 1,
            )
        }
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