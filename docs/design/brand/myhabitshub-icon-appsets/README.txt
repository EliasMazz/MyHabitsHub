MyHabitsHub — app icon assets (generated from your exact SVG)

iOS  (Xcode):
  Drag  ios/AppIcon.appiconset  into Assets.xcassets (replace the existing AppIcon).
  Modern Xcode only needs icon-1024.png; classic per-size files included for older targets.
  Icons are opaque, square, no pre-rounded corners (Apple applies the squircle).

Android (Android Studio):
  Copy the contents of  android/res/  into your app module's  src/main/res/.
  Adaptive icon: mipmap-anydpi-v26/ic_launcher.xml uses
    foreground = mipmap/ic_launcher_foreground (your check+keyline, transparent)
    background = @color/ic_launcher_background (#FFFFFF)
    monochrome = same foreground (for Material You themed icons)
  Legacy launchers fall back to ic_launcher.png / ic_launcher_round.png per density.
  Foreground art sits within the inner ~66% safe zone so every launcher mask is safe.
  Play Store listing icon: android/play/play_store_512.png (512x512, upload in Play Console).

Fill: artwork is centered at ~62% of each canvas (Apple squircle + Android 72/108dp safe zone).
