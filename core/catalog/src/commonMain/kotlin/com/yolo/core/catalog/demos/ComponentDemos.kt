package com.yolo.core.catalog.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.yolo.core.catalog.CatalogEntry
import com.yolo.core.designsystem.components.brand.YoloBrandLogo
import com.yolo.core.designsystem.components.brand.YoloFailureIcon
import com.yolo.core.designsystem.components.brand.YoloSuccessIcon
import com.yolo.core.designsystem.components.buttons.YoloButton
import com.yolo.core.designsystem.components.buttons.YoloButtonStyle
import com.yolo.core.designsystem.components.legacy.LoadingProgress
import com.yolo.core.designsystem.components.legacy.LoadingProgressMode
import com.yolo.core.designsystem.components.legacy.modals.AppModalBottomSheet
import com.yolo.core.designsystem.components.sheets.YoloSheetActionRow
import com.yolo.core.designsystem.components.sheets.YoloSheetActionRowStyle
import com.yolo.core.designsystem.components.textfields.YoloPasswordTextField
import com.yolo.core.designsystem.components.textfields.YoloTextField
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended
import com.yolo.core.designsystem.theme.section
import myhabitshub.core.designsystem.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.ic_back
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ButtonsDemo() {
    Column(verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGap)) {
        YoloButtonStyle.entries.forEach { style ->
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(YoloTokens.spacing.elementGap),
                verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.elementGap),
            ) {
                YoloButton(text = style.name, onClick = {}, style = style)
                YoloButton(text = "Disabled", onClick = {}, style = style, enabled = false)
                YoloButton(text = "Loading", onClick = {}, style = style, isLoading = true)
            }
        }
    }
}

@Composable
private fun TextFieldsDemo() {
    var value by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("secret") }
    var passwordVisible by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGap)) {
        YoloTextField(
            value = value,
            onValueChange = { value = it },
            title = "Habit name",
            placeholder = "e.g. Morning run",
        )
        YoloTextField(
            value = "wrong@",
            onValueChange = {},
            title = "With error",
            isError = true,
            supportingText = "Invalid value",
        )
        YoloTextField(
            value = "Read only-ish",
            onValueChange = {},
            title = "Disabled",
            enabled = false,
        )
        YoloPasswordTextField(
            value = password,
            onValueChange = { password = it },
            isPasswordVisible = passwordVisible,
            onToggleVisibilityClick = { passwordVisible = !passwordVisible },
            title = "Password",
        )
    }
}

@Composable
private fun SheetRowsDemo() {
    var showSheet by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGap)) {
        // Rows previewed on the current section's sheet fill
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.section.sheetSurface)
                .padding(vertical = YoloTokens.spacing.elementGap),
        ) {
            YoloSheetActionRow(
                icon = painterResource(Res.drawable.ic_back),
                label = "Default action",
                supportingText = "Icon-on-tinted-chip, section colored",
                onClick = {},
            )
            YoloSheetActionRow(
                icon = painterResource(Res.drawable.ic_back),
                label = "Destructive action",
                style = YoloSheetActionRowStyle.Destructive,
                onClick = {},
            )
            YoloSheetActionRow(
                icon = painterResource(Res.drawable.ic_back),
                label = "Disabled action",
                style = YoloSheetActionRowStyle.Disabled,
            )
        }
        YoloButton(text = "Open real bottom sheet", onClick = { showSheet = true })
    }
    if (showSheet) {
        AppModalBottomSheet(
            title = "Catalog sheet",
            hideButtons = true,
            onDismiss = { showSheet = false },
        ) {
            YoloSheetActionRow(
                icon = painterResource(Res.drawable.ic_back),
                label = "An action",
                supportingText = "Tinted by the selected section world",
                onClick = { showSheet = false },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HabitAccentsDemo() {
    val accents = MaterialTheme.colorScheme.extended.habitAccents
    val names = listOf("blue", "teal", "green", "purple", "pink", "amber", "cyan", "indigo")
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(YoloTokens.spacing.elementGap),
        verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.elementGap),
    ) {
        accents.forEachIndexed { index, quad ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(quad.container),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_back),
                        contentDescription = null,
                        tint = quad.onContainer,
                        modifier = Modifier.size(YoloTokens.sizing.icon),
                    )
                }
                Text(names.getOrElse(index) { "$index" }, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun LoadingDemo() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(YoloTokens.spacing.itemGap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LoadingProgress(mode = LoadingProgressMode.CIRCULAR)
        Text("LoadingProgress — CIRCULAR (OVERLAY/FULLSCREEN exist too)", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun BrandDemo() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(YoloTokens.spacing.itemGap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        YoloBrandLogo()
        YoloSuccessIcon()
        YoloFailureIcon()
    }
}

val componentEntries: List<CatalogEntry> = listOf(
    CatalogEntry("Buttons", "YoloButton — all styles x enabled/disabled/loading") { ButtonsDemo() },
    CatalogEntry("Text fields", "YoloTextField / YoloPasswordTextField states") { TextFieldsDemo() },
    CatalogEntry("Sheet action rows", "YoloSheetActionRow styles + a real AppModalBottomSheet") { SheetRowsDemo() },
    CatalogEntry("Habit accents", "The 8 per-habit identity chips (icon-on-tinted-chip)") { HabitAccentsDemo() },
    CatalogEntry("Loading", "LoadingProgress modes") { LoadingDemo() },
    CatalogEntry("Brand", "Logo + success/failure icons") { BrandDemo() },
)
