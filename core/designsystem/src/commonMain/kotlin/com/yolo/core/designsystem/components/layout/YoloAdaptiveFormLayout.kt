package com.yolo.core.designsystem.components.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yolo.core.designsystem.components.brand.YoloBrandLogo
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.designsystem.theme.extended
import com.yolo.core.presentation.util.DeviceConfiguration
import com.yolo.core.presentation.util.clearFocusOnTap
import com.yolo.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun YoloAdaptiveFormLayout(
    headerText: String? = null,
    errorText: String? = null,
    logo: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    errorAction: (@Composable () -> Unit)? = null,
    aura: Color? = null,
    auraCenterFraction: Offset = Offset(0.85f, 0.05f),
    formContent: @Composable ColumnScope.() -> Unit
) {
    val configuration = currentDeviceConfiguration()
    val headerColor = if (configuration == DeviceConfiguration.MOBILE_LANDSCAPE) {
        MaterialTheme.colorScheme.onBackground
    } else {
        MaterialTheme.colorScheme.extended.textPrimary
    }

    when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            YoloSurface(
                modifier = modifier
                    .clearFocusOnTap()
                    .consumeWindowInsets(WindowInsets.navigationBars),
                aura = aura,
                auraCenterFraction = auraCenterFraction,
                header = {
                    navigationIcon?.let {
                        // Status-bar inset so the back arrow never sits under the clock.
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                // safeDrawing.top = status bar OR display cutout, whichever
                                // is taller — notch phones included.
                                .windowInsetsPadding(
                                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                                )
                                .padding(top = 8.dp)
                        ) { it() }
                    }
                    if (logo != null) {
                        Spacer(modifier = Modifier.height(if (navigationIcon != null) 8.dp else 32.dp))
                        logo()
                        Spacer(modifier = Modifier.height(32.dp))
                    } else if (navigationIcon == null) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            ) {
                // Screens that render their own compact header (headerText == null, no error)
                // get zero extra offset, so the title sits right at the surface top.
                if (headerText != null || errorText != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    AuthHeaderSection(
                        headerText = headerText,
                        headerColor = headerColor,
                        errorText = errorText,
                        errorAction = errorAction
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                formContent()
            }
        }

        DeviceConfiguration.MOBILE_LANDSCAPE -> {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier
                    .fillMaxSize()
                    .consumeWindowInsets(WindowInsets.navigationBars)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    navigationIcon?.let {
                        Row(
                            modifier = Modifier.windowInsetsPadding(
                                WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                            )
                        ) { it() }
                    }
                    logo?.invoke()
                    AuthHeaderSection(
                        headerText = headerText,
                        headerColor = headerColor,
                        errorText = errorText,
                        errorAction = errorAction,
                        headerTextAlignment = TextAlign.Start
                    )
                }
                YoloSurface(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    formContent()
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                navigationIcon?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(
                                WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                            )
                            .padding(horizontal = 24.dp)
                    ) { it() }
                }
                logo?.invoke()
                Column(
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AuthHeaderSection(
                        headerText = headerText,
                        headerColor = headerColor,
                        errorText = errorText,
                        errorAction = errorAction
                    )
                    formContent()
                }
            }
        }
    }
}

@Composable
fun ColumnScope.AuthHeaderSection(
    headerText: String?,
    headerColor: Color,
    errorText: String? = null,
    errorAction: (@Composable () -> Unit)? = null,
    headerTextAlignment: TextAlign = TextAlign.Center
) {
    headerText?.let {
        Text(
            text = headerText,
            style = MaterialTheme.typography.titleLarge,
            color = headerColor,
            textAlign = headerTextAlignment,
            modifier = Modifier.fillMaxWidth()
        )
    }
    AnimatedVisibility(
        visible = errorText != null
    ) {
        if (errorText != null) {
            // Request-level (server) errors ONLY — never field errors, never success copy.
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = errorText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { liveRegion = LiveRegionMode.Polite },
                    textAlign = headerTextAlignment
                )
                // Recovery action (e.g. "Log in instead"), rendered under the message.
                errorAction?.invoke()
            }
        }
    }
}

@Composable
@Preview
fun YoloAdaptiveFormLayoutLightPreview() {
    YoloTheme {
        YoloAdaptiveFormLayout(
            headerText = "Welcome to Yolo!",
            errorText = "Login failed!",
            logo = { YoloBrandLogo() },
            formContent = {
                Text(
                    text = "Sample form title",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Sample form title 2",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
    }
}

@Composable
@Preview
fun YoloAdaptiveFormLayoutDarkPreview() {
    YoloTheme(darkTheme = true) {
        YoloAdaptiveFormLayout(
            headerText = "Welcome to Yolo!",
            errorText = "Login failed!",
            logo = { YoloBrandLogo() },
            formContent = {
                Text(
                    text = "Sample form title",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Sample form title 2",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
    }
}