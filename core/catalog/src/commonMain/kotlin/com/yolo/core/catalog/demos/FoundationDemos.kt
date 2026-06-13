package com.yolo.core.catalog.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yolo.core.catalog.CatalogEntry
import com.yolo.core.designsystem.theme.YoloSection
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.YoloTypeExtras
import com.yolo.core.designsystem.theme.colors
import com.yolo.core.designsystem.theme.extended

@Composable
private fun Swatch(name: String, color: Color, onColor: Color? = null) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(MaterialTheme.shapes.small)
                .background(color)
                .border(
                    YoloTokens.sizing.hairline,
                    MaterialTheme.colorScheme.outlineVariant,
                    MaterialTheme.shapes.small,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (onColor != null) Text("Aa", style = MaterialTheme.typography.labelMedium, color = onColor)
        }
        Text(name, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SwatchRow(swatches: List<Triple<String, Color, Color?>>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(YoloTokens.spacing.elementGap),
        verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.elementGap),
    ) {
        swatches.forEach { (name, color, on) -> Swatch(name, color, on) }
    }
}

@Composable
private fun ColorSchemeDemo() {
    val c = MaterialTheme.colorScheme
    Column(verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGap)) {
        Text("M3 roles (re-skinned per tab by YoloSectionTheme)", style = MaterialTheme.typography.titleSmall)
        SwatchRow(
            listOf(
                Triple("primary", c.primary, c.onPrimary),
                Triple("primaryCont", c.primaryContainer, c.onPrimaryContainer),
                Triple("secondary", c.secondary, c.onSecondary),
                Triple("secondaryCont", c.secondaryContainer, c.onSecondaryContainer),
                Triple("tertiary", c.tertiary, c.onTertiary),
                Triple("error", c.error, c.onError),
                Triple("errorCont", c.errorContainer, c.onErrorContainer),
            )
        )
        Text("Surface ladder", style = MaterialTheme.typography.titleSmall)
        SwatchRow(
            listOf(
                Triple("surface", c.surface, c.onSurface),
                Triple("contLowest", c.surfaceContainerLowest, c.onSurface),
                Triple("contLow", c.surfaceContainerLow, c.onSurface),
                Triple("container", c.surfaceContainer, c.onSurface),
                Triple("contHigh", c.surfaceContainerHigh, c.onSurface),
                Triple("contHighest", c.surfaceContainerHighest, c.onSurface),
                Triple("outline", c.outline, null),
                Triple("outlineVar", c.outlineVariant, null),
            )
        )
    }
}

@Composable
private fun ExtendedColorsDemo() {
    val x = MaterialTheme.colorScheme.extended
    val surface = MaterialTheme.colorScheme.surface
    Column(verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGap)) {
        Text("Status", style = MaterialTheme.typography.titleSmall)
        SwatchRow(
            listOf(
                Triple("success", x.success, surface),
                Triple("info", x.infoContainer, x.info),
            )
        )
        Text("Text", style = MaterialTheme.typography.titleSmall)
        SwatchRow(
            listOf(
                Triple("primary", x.textPrimary, surface),
                Triple("secondary", x.textSecondary, surface),
                Triple("tertiary", x.textTertiary, surface),
                Triple("placeholder", x.textPlaceholder, surface),
            )
        )
        Text("Habit accent (neutral ×8)", style = MaterialTheme.typography.titleSmall)
        SwatchRow(
            x.habitAccents.take(4).mapIndexed { i, q -> Triple("slot$i", q.container, q.onContainer) }
        )
    }
}

@Composable
private fun SectionWorldsDemo() {
    val isDark = MaterialTheme.colorScheme.extended.isDark
    Column(verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGap)) {
        YoloSection.entries.forEach { section ->
            val s = section.colors(isDark)
            Text(section.name, style = MaterialTheme.typography.titleSmall)
            SwatchRow(
                listOf(
                    Triple("accent", s.accent, s.onAccent),
                    Triple("container", s.accentContainer, s.onAccentContainer),
                    Triple("wash", s.surfaceTintWash, null),
                    Triple("sheet", s.sheetSurface, s.onSheetSurface),
                    Triple("iconChip", s.sheetIconChip, s.onSheetIconChip),
                    Triple("navPill", s.navIndicator, s.onNavIndicator),
                )
            )
        }
    }
}

@Composable
private fun TypographyDemo() {
    val t = MaterialTheme.typography
    Column(verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGapTight)) {
        listOf(
            "displayLarge 57" to t.displayLarge,
            "displayMedium 45" to t.displayMedium,
            "displaySmall 36" to t.displaySmall,
            "headlineLarge 32" to t.headlineLarge,
            "headlineMedium 28" to t.headlineMedium,
            "headlineSmall 24" to t.headlineSmall,
            "titleLarge 22" to t.titleLarge,
            "titleMedium 16" to t.titleMedium,
            "titleSmall 14" to t.titleSmall,
            "bodyLarge 16" to t.bodyLarge,
            "bodyMedium 14" to t.bodyMedium,
            "bodySmall 12" to t.bodySmall,
            "labelLarge 14" to t.labelLarge,
            "labelMedium 12" to t.labelMedium,
            "labelSmall 11" to t.labelSmall,
        ).forEach { (name, style) -> Text(name, style = style) }
        Text("statHero 57 (tnum) — 1234567890", style = YoloTypeExtras.statHero)
        Text("KICKER 12 +0.5".uppercase(), style = YoloTypeExtras.kicker, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("statUnit 14 — days / %", style = YoloTypeExtras.statUnit)
    }
}

@Composable
private fun ShapesDemo() {
    val shapes = listOf(
        "extraSmall 4" to MaterialTheme.shapes.extraSmall,
        "small 8" to MaterialTheme.shapes.small,
        "medium 12" to MaterialTheme.shapes.medium,
        "large 16" to MaterialTheme.shapes.large,
        "extraLarge 28" to MaterialTheme.shapes.extraLarge,
    )
    Row(horizontalArrangement = Arrangement.spacedBy(YoloTokens.spacing.itemGap)) {
        shapes.forEach { (name, shape) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .size(56.dp)
                        .clip(shape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                )
                Text(name, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun SpacingBar(name: String, value: Dp) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(YoloTokens.spacing.iconTextGap)) {
        Box(
            Modifier
                .width(value)
                .height(12.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(MaterialTheme.colorScheme.primary)
        )
        Text("$name = $value", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun SpacingDemo() {
    val s = YoloTokens.spacing
    Column(verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGapTight)) {
        SpacingBar("stackGapTight", s.stackGapTight)
        SpacingBar("elementGap", s.elementGap)
        SpacingBar("stackGap / iconTextGap", s.stackGap)
        SpacingBar("itemGap / cardPadding", s.itemGap)
        SpacingBar("listRowGap / sectionHeaderGap", s.listRowGap)
        SpacingBar("sectionGap / screenEdge", s.sectionGap)
        SpacingBar("dialogPadding", s.dialogPadding)
        SpacingBar("heroGap", s.heroGap)
        Text(
            "Adaptive: screenEdge ${s.screenEdge} on this window (24/32/40 by width class)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun MotionDemo() {
    val m = YoloTokens.motion
    Column(verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGapTight)) {
        Text(
            "instant ${m.instant} · quick ${m.quick} · standard ${m.standard} · gentle ${m.gentle} · emphasized ${m.emphasized} · celebration ${m.celebration} ms",
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            "Springs (reward register only): pressSpring, settleSpring, celebrateSpring. reducedMotion = ${m.reducedMotion}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

val foundationEntries: List<CatalogEntry> = listOf(
    CatalogEntry("Color scheme", "M3 roles + surface ladder — tab-aware via YoloSectionTheme") { ColorSchemeDemo() },
    CatalogEntry("Extended colors", "success / info + text aliases + neutral habit accents") { ExtendedColorsDemo() },
    CatalogEntry("Section worlds", "Unified neutral section palette, both modes") { SectionWorldsDemo() },
    CatalogEntry("Typography", "All 15 M3 slots + YoloTypeExtras (Manrope)") { TypographyDemo() },
    CatalogEntry("Shapes", "M3E corner scale 4/8/12/16/28") { ShapesDemo() },
    CatalogEntry("Spacing", "Semantic spacing tokens (adaptive)") { SpacingDemo() },
    CatalogEntry("Motion", "Durations, easings, springs, reducedMotion") { MotionDemo() },
)
