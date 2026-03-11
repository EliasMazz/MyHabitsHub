package com.yolo.myhabitshub.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.yolo.myhabitshub.core.presentation.theme.AppTheme
import myhabitshub.core.designsystem.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.ic_arrow_right
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

data class SettingsItemViewData(
    val textRes: StringResource,
    val startIcon: DrawableResource? = null,
    val helperTextRes: StringResource? = null,
    val textIconColor: Color? = null,
    val showEndIcon: Boolean = true
)

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    settingsItemViewData: SettingsItemViewData,
    textStyle: TextStyle = AppTheme.typography.h6,
) {
    val textIconColor = settingsItemViewData.textIconColor ?: AppTheme.colors.text.primary
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.horizontalItemSpacing)
    ) {
        settingsItemViewData.startIcon?.let {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = vectorResource(it),
                contentDescription = null,
                tint = textIconColor
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(settingsItemViewData.textRes),
            style = textStyle,
            color = textIconColor,
        )

        Row {
            settingsItemViewData.helperTextRes?.let {
                Text(
                    text = stringResource(it),
                    style = AppTheme.typography.bodyLarge,
                    color = AppTheme.colors.text.secondary
                )
                Spacer(modifier = Modifier.width(AppTheme.spacing.inputIconTextSpacing))
            }

            if (settingsItemViewData.showEndIcon) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = vectorResource(Res.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = textIconColor
                )
            }
        }


    }
}

@Composable
fun SettingItemListContainer(
    itemList: List<SettingsItemViewData>,
    itemTextStyle: TextStyle = AppTheme.typography.h6,
    onClick: (SettingsItemViewData) -> Unit = {}
) {
    AppCardContainer(contentPaddingValues = PaddingValues(0.dp)) {
        Column {
            itemList.forEach {
                SettingsItem(
                    settingsItemViewData = it,
                    textStyle = itemTextStyle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClick(it)
                        }.padding(AppTheme.spacing.cardContentSpacing)
                )
            }

        }
    }
}