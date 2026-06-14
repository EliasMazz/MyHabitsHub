MyHabitsHub — DARK THEME icon assets

iOS (Xcode, iOS 18+ appearance-aware):
  Use ios/AppIcon.appiconset (Contents.json has Any + Dark + Tinted).
    Any/light   -> icon-1024.png        (opaque white, original)
    Dark        -> icon-1024-dark.png   (transparent symbol; iOS adds its dark backing)
    Tinted      -> icon-1024-tinted.png (grayscale; system applies a tint)
  icon-1024-dark-opaque.png is a filled dark version if you prefer a solid dark background.

Android (Android Studio):
  Copy android/res/ into src/main/res/.  Dark adaptive uses
    background = @color/ic_launcher_background_dark (#16181B)
    foreground = white check + gray keyline (transparent)
    monochrome = same (Material You themed icons).
  Play listing (dark marketing): android/play/play_store_512_dark.png

Palette: bg #16181B, keyline #3A3E45, check #F5F6F8 (mirrors the light icon's contrast).
