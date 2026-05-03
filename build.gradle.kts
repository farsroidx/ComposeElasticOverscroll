plugins {
    // Android
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library)     apply false
    // Jetbrains
    alias(libs.plugins.jetbrains.kotlin.compose) apply false
}