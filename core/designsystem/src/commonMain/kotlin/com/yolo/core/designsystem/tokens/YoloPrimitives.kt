package com.yolo.core.designsystem.tokens

import androidx.compose.ui.unit.dp

/**
 * Layer-1 primitives (Design System v3 — Yolo Design System v3,
 * docs/design/design-system-v3-spec.md).
 *
 * Values are pixel-measured from the 2025-2026 reference redesign press imagery and
 * normalized to the published GM3/M3 tokens they approximate (provenance per value in the spec).
 *
 * Watch-carry layer: raw ARGB values and dp steps with no material3 dependency. Feature modules
 * must NOT import this file — they consume semantic tokens only.
 */
object YoloColorPrimitives {
    // Ink ramp — GM3 Google-app gray ladder (light containers + dark content)
    const val Ink0 = 0xFFFFFFFF
    const val Ink50 = 0xFFFFFFFF // GM N98 — light surface/background
    const val Ink100 = 0xFFF8FAFD // surfaceContainerLow (derived ladder step)
    const val Ink150 = 0xFFF0F4F9 // surfaceContainer, measured 4x
    const val Ink200 = 0xFFE9EEF6 // surfaceContainerHigh, measured chart track
    const val Ink250 = 0xFFDDE3EA // surfaceContainerHighest (derived ladder step)
    const val Ink300 = 0xFFDDE3EA // surfaceDim, measured inactive-day circle
    const val Ink350 = 0xFFC4C7C5 // GM NV80 — dark onSurfaceVariant / light outlineVariant
    const val Ink400 = 0xFFA9ACAA // GM NV70
    const val Ink450 = 0xFF8E918F // GM NV60 — dark outline
    const val Ink500 = 0xFF747775 // GM NV50 — light outline
    const val Ink550 = 0xFF5C5F5E // GM NV40
    const val Ink600 = 0xFF444746 // GM NV30 — light onSurfaceVariant, measured
    const val Ink700 = 0xFF303030 // GM N20
    const val Ink800 = 0xFF1F1F1F // light onSurface, measured x3
    const val InkText = 0xFFE3E3E3 // GM N90 — dark onSurface, measured exact

    // Deep ramp — GM3 Google-app dark grays (NOT the purple-tinted public M3 baseline)
    const val Deep50 = 0xFF333537 // GM N22 — surfaceBright/Highest dark
    const val Deep100 = 0xFF282A2C // surfaceContainerHigh dark, measured ring track
    const val Deep200 = 0xFF1E1F20 // surfaceContainer dark, measured canvas/nav
    const val Deep300 = 0xFF1B1B1B // surfaceContainerLow dark (derived ladder step)
    const val Deep400 = 0xFF131314 // GM N6 — dark surface/background, measured hero wash
    const val Deep500 = 0xFF0E0E0F // GM N4 — surfaceContainerLowest dark

    // Brand ramp — reference Fitness teal family (the app's green-teal channel), all measured
    const val Brand1000 = 0xFF003733 // steps icon circle (dark)
    const val Brand900 = 0xFF00504B // steps chip main (dark)
    const val Brand800 = 0xFF006A64 // map route stroke — Progress light accent
    const val Brand700 = 0xFF00716A // activity icon glyph (light)
    const val Brand600 = 0xFF00948A // step bars (Material teal 500)
    const val Brand500 = 0xFF41D5CA // goal-met bars
    const val Brand300 = 0xFF90F4EA // steps chip text (dark)
    const val Brand200 = 0xFF5DF0E3 // Fitness tag chip fill (light)
    const val Brand100 = 0xFFCBE7EA // activity icon chip fill (light)
    const val BrandDeep = 0xFF05514A // Fitness tag chip text (light)

    // Amber — the reference app's single warm status channel (warning == streak), measured
    const val Amber800 = 0xFF5F4200
    const val Amber700 = 0xFF614206
    const val Amber300 = 0xFFFFBA2B
    const val Amber100 = 0xFFFFDEA9
    const val AmberDeepContainer = 0xFF5F4200
    const val AmberOnDark = 0xFF5F4200
    const val AmberContainerOn = 0xFFFFBA2B

    // Red — M3 baseline error ramp
    const val Red700 = 0xFFB3261E // E40
    const val Red600 = 0xFF8C1D18 // E30
    const val Red300 = 0xFFF2B8B5 // E80
    const val Red200 = 0xFFEC928E // E70
    const val Red100 = 0xFFF9DEDC // E90
    const val RedDeep900 = 0xFF601410 // E20
    const val RedDeep800 = 0xFF8C1D18 // E30

    // Blue — GM blue (primary/info), measured links/FAB/pill
    const val Blue700 = 0xFF0B57D0 // GM P40
    const val Blue300 = 0xFFA8C7FA // GM P80
    const val Blue100 = 0xFFD3E3FD // GM P90
    const val BlueDeep = 0xFF0842A0 // GM P30
    const val BlueOnDark = 0xFF062E6F // GM P20
    const val BlueDarkContainer = 0xFF0842A0
    const val BlueDarkContainerOn = 0xFFD3E3FD
    const val BlueHoverDark = 0xFF7CACF8 // GM P70
    const val BlueData500 = 0xFF4285F4 // light progressIndicator (cardio line)
    const val BlueData300 = 0xFF8AB4F8 // dark progressIndicator (ring arc)

    // Cyan — GM secondary family (the reference app nav pill, measured EXACT)
    const val Cyan700 = 0xFF00639B // GM S40
    const val Cyan300 = 0xFF7FCFFF // GM S80
    const val Cyan100 = 0xFFC2E7FF // GM S90, measured
    const val Cyan800 = 0xFF004A77 // GM S30, measured exact (nav pill)
    const val Cyan900 = 0xFF003355 // GM S20

    // Green — GM tertiary family (success channel; the reference app health chips measured)
    const val Green700 = 0xFF146C2E // GM T40
    const val Green300 = 0xFF6DD58C // GM T80
    const val Green100 = 0xFFC4EED0 // GM T90
    const val Green800 = 0xFF0F5223 // GM T30
    const val Green900 = 0xFF0A3818 // GM T20
    const val GreenContainerLight = 0xFFBEEFBB // measured status chips
    const val GreenOnContainerLight = 0xFF00522C // measured
    const val GreenContainerDark = 0xFF00522C // measured readiness chip
    const val GreenOnContainerDark = 0xFFBCEEBB // measured
    const val TealHeatmapDark3 = 0xFF007B73 // measured steps chip leading

    // Cobalt (Tracking world — reference Today blue)
    const val Cobalt700 = 0xFF0B57D0
    const val Cobalt300 = 0xFFA8C7FA
    const val Cobalt100 = 0xFFD3E3FD
    const val Cobalt150 = 0xFFC2E7FF // header icon chip, measured
    const val CobaltDeep = 0xFF0842A0
    const val CobaltDarkContainer = 0xFF004A77 // nav pill, measured exact
    const val CobaltContainerOn = 0xFFC2E7FF
    const val CobaltWashLight = 0xFFF0F4F9 // measured Health canvas (borrowed, spec §10.3)
    const val CobaltSheetLight = 0xFFFFFFFF // derived
    const val CobaltWashDark = 0xFF1E1F20 // measured hero wash — dark worlds are neutral
    const val CobaltSheetDark = 0xFF131314 // measured nested card
    const val CobaltChipDark = 0xFF004A77
    const val CobaltVariantLight = 0xFF444746 // derived blue-cast NV30
    const val CobaltVariantDark = 0xFFC4C7C5

    // Violet (Settings world — the reference app Sleep purple)
    const val Violet700 = 0xFF5A2F90 // measured Sleep tag text
    const val Violet300 = 0xFFCEA8FF // M3 baseline P80 (no dark purple accent in imagery)
    const val Violet100 = 0xFFEDDCFF // M3 baseline P90, measured within noise
    const val Violet150 = 0xFFF8EDFF
    const val VioletDeep = 0xFF421378 // M3 baseline P20
    const val VioletDarkContainer = 0xFF5A2F90 // measured dark Sleep tag pill
    const val VioletContainerOn = 0xFFEDDCFF // measured
    const val VioletWashLight = 0xFFFFFFFF // measured Sleep canvas
    const val VioletSheetLight = 0xFFF0F4F9 // derived
    const val VioletWashDark = 0xFF131314
    const val VioletSheetDark = 0xFF1E1F20
    const val VioletChipDark = 0xFF5B2F90
    const val VioletVariantLight = 0xFF444746 // M3 baseline NV30
    const val VioletVariantDark = 0xFFC4C7C5

    // Teal-world (Progress section) washes/sheets — measured Fitness canvas family
    const val BrandWashLight = 0xFFFFFFFF // measured Fitness canvas
    const val BrandSheetLight = 0xFFF0F4F9 // derived
    const val BrandChipLight = 0xFFCBE7EA // measured Fitness tag chip
    const val BrandWashDark = 0xFF131314 // dark worlds are neutral (spec §4.4)
    const val BrandVariantLight = 0xFF444746 // derived teal-cast NV30
    const val BrandVariantDark = 0xFFC4C7C5

    // v4 additions (measured from reference screenshots)
    const val Periwinkle500 = 0xFF437EF8
    const val Periwinkle300 = 0xFF90BAFF
    const val PeriwinkleInk = 0xFF001944
    const val Peach900 = 0xFF812800
    const val Peach100 = 0xFFFFDBCD
    const val Peach400 = 0xFFFF9B72
    const val Peach50 = 0xFFFFEDE6
    const val Green400 = 0xFF54D071
    const val GreenBandDark = 0xFF25352A
    const val GreenChipLight = 0xFFBEEFBB // measured 'In range' chip — green habitAccent container (replaces #BFEFBB)
    const val Cyan150 = 0xFFBDE9FF
    const val Cyan850 = 0xFF004D68
    const val Violet200 = 0xFFD4B2FF
    const val Amber500 = 0xFFE09F00
}

object YoloSpacePrimitives {
    val s2 = 2.dp
    val s4 = 4.dp
    val s6 = 6.dp
    val s8 = 8.dp
    val s12 = 12.dp
    val s16 = 16.dp
    val s20 = 20.dp
    val s24 = 24.dp
    val s32 = 32.dp
    val s36 = 36.dp
    val s40 = 40.dp
    val s48 = 48.dp
}
