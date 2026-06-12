package com.yolo.core.designsystem.theme

import androidx.compose.ui.graphics.Color
import com.yolo.core.designsystem.tokens.YoloColorPrimitives

// Design System v3 (Yolo Design System v3) color primitives as Compose Colors.
// Mirrors tokens/YoloPrimitives.kt; provenance for every value in
// docs/design/design-system-v3-spec.md §1. ContrastTest locks all pairs.

// Ink ramp (GM3 Google-app grays — light containers + dark content)
val YoloInk0 = Color(YoloColorPrimitives.Ink0)
val YoloInk50 = Color(YoloColorPrimitives.Ink50)
val YoloInk100 = Color(YoloColorPrimitives.Ink100)
val YoloInk150 = Color(YoloColorPrimitives.Ink150)
val YoloInk200 = Color(YoloColorPrimitives.Ink200)
val YoloInk250 = Color(YoloColorPrimitives.Ink250)
val YoloInk300 = Color(YoloColorPrimitives.Ink300)
val YoloInk350 = Color(YoloColorPrimitives.Ink350)
val YoloInk400 = Color(YoloColorPrimitives.Ink400)
val YoloInk450 = Color(YoloColorPrimitives.Ink450)
val YoloInk500 = Color(YoloColorPrimitives.Ink500)
val YoloInk550 = Color(YoloColorPrimitives.Ink550)
val YoloInk600 = Color(YoloColorPrimitives.Ink600)
val YoloInk700 = Color(YoloColorPrimitives.Ink700)
val YoloInk800 = Color(YoloColorPrimitives.Ink800)
val YoloInkText = Color(YoloColorPrimitives.InkText)

// Deep ramp (GM3 dark grays)
val YoloDeep50 = Color(YoloColorPrimitives.Deep50)
val YoloDeep100 = Color(YoloColorPrimitives.Deep100)
val YoloDeep200 = Color(YoloColorPrimitives.Deep200)
val YoloDeep300 = Color(YoloColorPrimitives.Deep300)
val YoloDeep400 = Color(YoloColorPrimitives.Deep400)
val YoloDeep500 = Color(YoloColorPrimitives.Deep500)

// Brand ramp (reference Fitness teal — the app's green-teal channel)
val YoloBrand1000 = Color(YoloColorPrimitives.Brand1000)
val YoloBrand900 = Color(YoloColorPrimitives.Brand900)
val YoloBrand800 = Color(YoloColorPrimitives.Brand800)
val YoloBrand700 = Color(YoloColorPrimitives.Brand700)
val YoloBrand600 = Color(YoloColorPrimitives.Brand600)
val YoloBrand500 = Color(YoloColorPrimitives.Brand500)
val YoloBrand300 = Color(YoloColorPrimitives.Brand300)
val YoloBrand200 = Color(YoloColorPrimitives.Brand200)
val YoloBrand100 = Color(YoloColorPrimitives.Brand100)
val YoloBrandDeep = Color(YoloColorPrimitives.BrandDeep)

// Amber (single warm status channel: warning == streak)
val YoloAmber800 = Color(YoloColorPrimitives.Amber800)
val YoloAmber700 = Color(YoloColorPrimitives.Amber700)
val YoloAmber300 = Color(YoloColorPrimitives.Amber300)
val YoloAmber100 = Color(YoloColorPrimitives.Amber100)
val YoloAmberDeepContainer = Color(YoloColorPrimitives.AmberDeepContainer)
val YoloAmberOnDark = Color(YoloColorPrimitives.AmberOnDark)
val YoloAmberContainerOn = Color(YoloColorPrimitives.AmberContainerOn)

// Red (M3 baseline error ramp)
val YoloRed700 = Color(YoloColorPrimitives.Red700)
val YoloRed600 = Color(YoloColorPrimitives.Red600)
val YoloRed300 = Color(YoloColorPrimitives.Red300)
val YoloRed200 = Color(YoloColorPrimitives.Red200)
val YoloRed100 = Color(YoloColorPrimitives.Red100)
val YoloRedDeep900 = Color(YoloColorPrimitives.RedDeep900)
val YoloRedDeep800 = Color(YoloColorPrimitives.RedDeep800)

// Blue (GM blue — primary/info/data-viz)
val YoloBlue700 = Color(YoloColorPrimitives.Blue700)
val YoloBlue300 = Color(YoloColorPrimitives.Blue300)
val YoloBlue100 = Color(YoloColorPrimitives.Blue100)
val YoloBlueDeep = Color(YoloColorPrimitives.BlueDeep)
val YoloBlueOnDark = Color(YoloColorPrimitives.BlueOnDark)
val YoloBlueDarkContainer = Color(YoloColorPrimitives.BlueDarkContainer)
val YoloBlueDarkContainerOn = Color(YoloColorPrimitives.BlueDarkContainerOn)
val YoloBlueHoverDark = Color(YoloColorPrimitives.BlueHoverDark)
val YoloBlueData500 = Color(YoloColorPrimitives.BlueData500)
val YoloBlueData300 = Color(YoloColorPrimitives.BlueData300)

// Cyan (GM secondary — the measured the reference app nav pill family)
val YoloCyan700 = Color(YoloColorPrimitives.Cyan700)
val YoloCyan300 = Color(YoloColorPrimitives.Cyan300)
val YoloCyan100 = Color(YoloColorPrimitives.Cyan100)
val YoloCyan800 = Color(YoloColorPrimitives.Cyan800)
val YoloCyan900 = Color(YoloColorPrimitives.Cyan900)

// Green (GM tertiary — success channel)
val YoloGreen700 = Color(YoloColorPrimitives.Green700)
val YoloGreen300 = Color(YoloColorPrimitives.Green300)
val YoloGreen100 = Color(YoloColorPrimitives.Green100)
val YoloGreen800 = Color(YoloColorPrimitives.Green800)
val YoloGreen900 = Color(YoloColorPrimitives.Green900)
val YoloSuccessContainerLight = Color(YoloColorPrimitives.GreenContainerLight)
val YoloOnSuccessContainerLight = Color(YoloColorPrimitives.GreenOnContainerLight)
val YoloGreenContainerDark = Color(YoloColorPrimitives.GreenContainerDark)
val YoloGreenOnContainerDark = Color(YoloColorPrimitives.GreenOnContainerDark)
val YoloTealHeatmapDark3 = Color(YoloColorPrimitives.TealHeatmapDark3)

// Cobalt (Tracking section world)
val YoloCobalt700 = Color(YoloColorPrimitives.Cobalt700)
val YoloCobalt300 = Color(YoloColorPrimitives.Cobalt300)
val YoloCobalt100 = Color(YoloColorPrimitives.Cobalt100)
val YoloCobalt150 = Color(YoloColorPrimitives.Cobalt150)
val YoloCobaltDeep = Color(YoloColorPrimitives.CobaltDeep)
val YoloCobaltDarkContainer = Color(YoloColorPrimitives.CobaltDarkContainer)
val YoloCobaltContainerOn = Color(YoloColorPrimitives.CobaltContainerOn)
val YoloCobaltWashLight = Color(YoloColorPrimitives.CobaltWashLight)
val YoloCobaltSheetLight = Color(YoloColorPrimitives.CobaltSheetLight)
val YoloCobaltWashDark = Color(YoloColorPrimitives.CobaltWashDark)
val YoloCobaltSheetDark = Color(YoloColorPrimitives.CobaltSheetDark)
val YoloCobaltChipDark = Color(YoloColorPrimitives.CobaltChipDark)
val YoloCobaltVariantLight = Color(YoloColorPrimitives.CobaltVariantLight)
val YoloCobaltVariantDark = Color(YoloColorPrimitives.CobaltVariantDark)

// Violet (Settings section world)
val YoloViolet700 = Color(YoloColorPrimitives.Violet700)
val YoloViolet300 = Color(YoloColorPrimitives.Violet300)
val YoloViolet100 = Color(YoloColorPrimitives.Violet100)
val YoloViolet150 = Color(YoloColorPrimitives.Violet150)
val YoloVioletDeep = Color(YoloColorPrimitives.VioletDeep)
val YoloVioletDarkContainer = Color(YoloColorPrimitives.VioletDarkContainer)
val YoloVioletContainerOn = Color(YoloColorPrimitives.VioletContainerOn)
val YoloVioletWashLight = Color(YoloColorPrimitives.VioletWashLight)
val YoloVioletSheetLight = Color(YoloColorPrimitives.VioletSheetLight)
val YoloVioletWashDark = Color(YoloColorPrimitives.VioletWashDark)
val YoloVioletSheetDark = Color(YoloColorPrimitives.VioletSheetDark)
val YoloVioletChipDark = Color(YoloColorPrimitives.VioletChipDark)
val YoloVioletVariantLight = Color(YoloColorPrimitives.VioletVariantLight)
val YoloVioletVariantDark = Color(YoloColorPrimitives.VioletVariantDark)

// Teal-world (Progress section) washes/sheets
val YoloBrandWashLight = Color(YoloColorPrimitives.BrandWashLight)
val YoloBrandSheetLight = Color(YoloColorPrimitives.BrandSheetLight)
val YoloBrandChipLight = Color(YoloColorPrimitives.BrandChipLight)
val YoloBrandWashDark = Color(YoloColorPrimitives.BrandWashDark)
val YoloBrandVariantLight = Color(YoloColorPrimitives.BrandVariantLight)
val YoloBrandVariantDark = Color(YoloColorPrimitives.BrandVariantDark)

// Status / scheme extras (spec §1.7, §3)
val YoloWarningLight = YoloAmber700
val YoloWarningContainerLight = YoloAmber100
val YoloOnWarningContainerLight = YoloAmber700
val YoloWarningDark = YoloAmber300
val YoloOnWarningDark = YoloAmber800
val YoloWarningContainerDark = YoloAmber800
val YoloOnWarningContainerDark = YoloAmber300
val YoloOnErrorContainerDark = YoloRed100
val YoloOnSecondaryDark = YoloCyan900
val YoloOnSecondaryContainerDark = YoloCyan100
val YoloPrimaryHoverLight = YoloBlueDeep
val YoloSurfaceVariantLight = Color(0xFFE1E3E1) // GM NV90
val YoloNeutral95 = Color(0xFFF2F2F2) // GM N95 — light inverseOnSurface
