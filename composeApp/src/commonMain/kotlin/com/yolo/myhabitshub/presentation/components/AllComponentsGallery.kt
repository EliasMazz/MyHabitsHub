package com.yolo.myhabitshub.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yolo.myhabitshub.presentation.components.modals.AppDialogPreview
import com.yolo.myhabitshub.presentation.components.modals.AppModalBottomSheetPreview
import com.yolo.myhabitshub.presentation.components.modals.DeleteUserConfirmationPreview
import com.yolo.myhabitshub.presentation.components.modals.NativeDialogPreview
import com.yolo.myhabitshub.presentation.components.modals.DialogPreview
import com.yolo.myhabitshub.core.presentation.theme.AppTheme


@Composable
fun AllComponentsGallery() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.largeSpacing)
    ) {
        ComponentsGalleryContainer(isDarkMode = false)
        Divider()
        ComponentsGalleryContainer(isDarkMode = true)
    }
}

@Composable
private fun ComponentsGallery() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.largeSpacing)
    ) {

        SectionContainer(title = "Radio Button") {
            RadioButtonPreview()
        }

        SectionContainer(title = "User Inputs") {
            UserInputPreview()
        }
        SectionContainer(title = "Chips") {
            ChipPreview()
        }
        SectionContainer(title = "Buttons") {
            AppButtonPreview()
        }
        SectionContainer(title = "Social Auth Buttons") {
            AuthUiHelperButtonsPreview()
        }
        SectionContainer(title = "Divider") {
            DividerPreview()
        }
        SectionContainer(title = "Loading Bar") {
            LoadingProgressPreview()
        }
        SectionContainer(title = "App Toolbar") {

        }

        SectionContainer(title = "Bottom Navigation") {
            //BottomNavigationBarPreview()
        }

        SectionContainer(title = "Horizontal Scrollable List") {
            HorizontalScrollableListPreview()
        }

        SectionContainer(title = "Dialogs") {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.verticalListItemSpacing)) {
                NativeDialogPreview()
                AppDialogPreview()
                DialogPreview()
                DeleteUserConfirmationPreview()
            }
        }

        SectionContainer("Modal Bottom Sheet") {
            AppModalBottomSheetPreview()
        }

        SectionContainer(title = "Titles") {
            Box(
                modifier = Modifier
                    .border(2.dp, AppTheme.colors.primary, RoundedCornerShape(16.dp))
                    .padding(AppTheme.spacing.defaultSpacing)
            ) {
                TitlesPreview()
            }

        }
    }
}

@Composable
private fun ComponentsGalleryContainer(isDarkMode: Boolean) {
    AppTheme(isDarkMode = isDarkMode) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sectionSpacing),
            modifier = Modifier.padding(AppTheme.spacing.outerSpacing)
        ) {
            ScreenTitle(if (isDarkMode) "Dark Mode components" else "Light Mode components")
            ComponentsGallery()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreviewHelper(content: @Composable () -> Unit) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.horizontalItemSpacing),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.verticalListItemSpacing),
        content = { content() },
    )
}