package com.yolo.myhabitshub.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yolo.core.designsystem.components.legacy.AppButtonPreview
import com.yolo.core.designsystem.components.legacy.ChipPreview
import com.yolo.core.designsystem.components.legacy.Divider
import com.yolo.core.designsystem.components.legacy.DividerPreview
import com.yolo.core.designsystem.components.legacy.HorizontalScrollableListPreview
import com.yolo.core.designsystem.components.legacy.LoadingProgressPreview
import com.yolo.core.designsystem.components.legacy.RadioButtonPreview
import com.yolo.core.designsystem.components.legacy.ScreenTitle
import com.yolo.core.designsystem.components.legacy.SectionContainer
import com.yolo.core.designsystem.components.legacy.TitlesPreview
import com.yolo.core.designsystem.components.legacy.UserInputPreview
import com.yolo.core.designsystem.components.legacy.modals.AppDialogPreview
import com.yolo.core.designsystem.components.legacy.modals.AppModalBottomSheetPreview
import com.yolo.account.presentation.components.AuthUiHelperButtonsPreview
import com.yolo.account.presentation.components.DeleteUserConfirmationPreview
import com.yolo.core.designsystem.components.legacy.modals.NativeDialogPreview
import com.yolo.core.designsystem.components.legacy.modals.DialogPreview
import com.yolo.core.designsystem.theme.legacy.AppTheme


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
