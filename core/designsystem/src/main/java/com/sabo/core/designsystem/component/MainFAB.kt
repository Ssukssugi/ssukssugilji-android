package com.sabo.core.designsystem.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.sabo.core.designsystem.R
import com.sabo.core.designsystem.theme.DiaryColorsPalette
import com.sabo.core.designsystem.theme.DiaryTypography

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainFAB(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onTownWriteClick: () -> Unit,
    onDiaryWriteClick: () -> Unit
) {
    BackHandler(isExpanded) { onExpandedChange(false) }

    FloatingActionButtonMenu(
        modifier = modifier,
        expanded = isExpanded,
        button = {
            ToggleFloatingActionButton(
                checked = isExpanded,
                onCheckedChange = { onExpandedChange(!isExpanded) },
                containerColor = ToggleFloatingActionButtonDefaults.containerColor(
                    initialColor = DiaryColorsPalette.current.green500,
                    finalColor = DiaryColorsPalette.current.green500
                ),
                containerCornerRadius = ToggleFloatingActionButtonDefaults.containerCornerRadius(
                    initialSize = 100.dp,
                    finalSize = 100.dp
                ),
                containerSize = ToggleFloatingActionButtonDefaults.containerSize()
            ) {
                val imageVector by remember {
                    derivedStateOf {
                        if (checkedProgress > 0.5f) Icons.Filled.Close else Icons.Filled.Add
                    }
                }

                Icon(
                    painter = rememberVectorPainter(imageVector),
                    contentDescription = null,
                    modifier = Modifier.animateIcon(
                        checkedProgress = { checkedProgress },
                        color = { Color.White }
                    )
                )
            }
        }
    ) {
        FloatingActionButtonMenuItem(
            onClick = {
                onExpandedChange(false)
                onTownWriteClick()
            },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_bottom_nav_town_24),
                    contentDescription = null,
                    tint = DiaryColorsPalette.current.green500,
                    modifier = Modifier.size(16.dp)
                )
            },
            text = {
                Text(
                    text = "쑥쑥자랑하기",
                    style = DiaryTypography.bodyMediumBold,
                    color = DiaryColorsPalette.current.gray900
                )
            },
            containerColor = Color.White,
        )
        FloatingActionButtonMenuItem(
            onClick = {
                onExpandedChange(false)
                onDiaryWriteClick()
            },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_bottom_nav_diary_24),
                    contentDescription = null,
                    tint = DiaryColorsPalette.current.green500,
                    modifier = Modifier.size(16.dp)
                )
            },
            text = {
                Text(
                    text = "일지작성하기",
                    style = DiaryTypography.bodyMediumBold,
                    color = DiaryColorsPalette.current.gray900
                )
            },
            containerColor = Color.White,
        )
    }
}
