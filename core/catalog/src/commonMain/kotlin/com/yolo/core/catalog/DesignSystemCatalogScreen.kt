package com.yolo.core.catalog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.yolo.core.designsystem.theme.YoloSection
import com.yolo.core.designsystem.theme.YoloSectionTheme
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended
import com.yolo.core.designsystem.theme.section

/**
 * Design System Catalog — the living, visual reference for the Yolo design system.
 *
 * Debug-only by wiring: only reachable behind AppScreen's catalog flag (never a release
 * navigation destination; R8 strips it from release when the flag is a compile-time false).
 *
 * This screen intentionally owns its own YoloTheme/YoloSectionTheme wrappers (normally
 * forbidden in screens) so developers can flip dark mode and section worlds live — it is
 * a tool, not an app screen. Everything rendered inside follows the design system strictly;
 * the catalog doubles as the reference implementation.
 *
 * To add entries see CatalogEntry.kt — one demo composable + one list entry.
 */
@Composable
fun DesignSystemCatalogScreen() {
    var darkTheme by remember { mutableStateOf<Boolean?>(null) }
    var world by remember { mutableStateOf(YoloSection.Tracking) }
    val systemDark = isSystemInDarkTheme()
    val resolvedDark = darkTheme ?: systemDark

    YoloTheme(darkTheme = resolvedDark) {
        YoloSectionTheme(section = world) {
            val expanded = remember { mutableStateMapOf<String, Boolean>() }
            // Surface (not Modifier.background) so LocalContentColor is established —
            // onSurface is the CI-verified pair on every wash/card in both modes.
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.section.surfaceTintWash,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = YoloTokens.spacing.screenEdge,
                        vertical = YoloTokens.spacing.sectionGap,
                    ),
                verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGap),
            ) {
                Text("Design System Catalog", style = MaterialTheme.typography.headlineMedium)
                Text(
                    "Yolo Design System v3 — docs/design/design-system-catalog.md",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                // Controls: dark toggle + section world selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(YoloTokens.spacing.itemGap),
                ) {
                    Text("Dark", style = MaterialTheme.typography.labelLarge)
                    Switch(checked = resolvedDark, onCheckedChange = { darkTheme = it })
                    Spacer(Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(YoloTokens.spacing.elementGap)) {
                    YoloSection.entries.forEach { s ->
                        FilterChip(
                            selected = world == s,
                            onClick = { world = s },
                            label = { Text(s.name, style = MaterialTheme.typography.labelMedium) },
                        )
                    }
                }

                catalogSections.forEach { (category, entries) ->
                    Text(
                        category.label,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = YoloTokens.spacing.sectionHeaderGap),
                    )
                    entries.forEach { entry ->
                        val isOpen = expanded[entry.title] ?: false
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.large)
                                .background(MaterialTheme.colorScheme.extended.surfaceHigher)
                                .clickable { expanded[entry.title] = !isOpen }
                                .padding(YoloTokens.spacing.cardPadding),
                            verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGap),
                        ) {
                            Text(entry.title, style = MaterialTheme.typography.titleMedium)
                            Text(
                                entry.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            AnimatedVisibility(visible = isOpen) {
                                Column(modifier = Modifier.padding(top = YoloTokens.spacing.stackGap)) {
                                    entry.content()
                                }
                            }
                        }
                    }
                }
            }
            }
        }
    }
}
