package com.sabo.feature.main.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography
import com.sabo.core.navigator.MainTabRoute
import com.sabo.feature.main.MainTab

@Composable
internal fun MainNavigationSuite(
    currentTab: MainTab,
    onTabSelected: (MainTabRoute) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
                    clip = false
                ),
                containerColor = Color(0xFFFFFFFF),
                tonalElevation = 0.dp
            ) {
                MainTab.entries.forEach { tab ->
                    val isSelected = tab == currentTab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                onTabSelected(tab.route)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(tab.iconRes),
                                contentDescription = tab.label,
                                tint = if (isSelected) {
                                    DiaryColorsPalette.current.green500
                                } else {
                                    DiaryColorsPalette.current.gray500
                                }
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                color = if (isSelected) {
                                    DiaryColorsPalette.current.gray900
                                } else {
                                    DiaryColorsPalette.current.gray500
                                },
                                style = DiaryTypography.captionMediumSemiBold
                            )
                        },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                            selectedTextColor = DiaryColorsPalette.current.gray900
                        ),
                        interactionSource = remember { MutableInteractionSource() }
                    )
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            content(innerPadding)
        }
    }
}
