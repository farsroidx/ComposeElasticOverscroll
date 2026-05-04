@file:Suppress("DEPRECATION", "UnusedReceiverParameter", "unused")

package ir.farsroidx.app.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers

// @formatter:off // TODO: DON'T Remove This -------------------------------------------------------

@Preview(
    apiLevel        = 36,
    name            = "Light Mode",
    group           = "Light",
    locale          = "en",
    showSystemUi    = false,
    showBackground  = false,
    device          = Devices.DEFAULT,
    backgroundColor = 0xFFFFFFFF,
    uiMode          = Configuration.UI_MODE_NIGHT_NO,
    wallpaper       = Wallpapers.BLUE_DOMINATED_EXAMPLE
)
annotation class ComposablePreview

@Composable
fun MyTestProjectThemePreview(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues.Zero,
    content: @Composable ColumnScope.() -> Unit
) {
    MaterialTheme(
        colorScheme = makeColorScheme(),
        typography = VazirmatnTypography,
        content = {
            Column(
                modifier = modifier.fillMaxWidth().padding(paddingValues),
                content = content
            )
        }
    )
}

@Composable
fun MyTestProjectTheme(
    userRtlDirection: Boolean = true, content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = makeColorScheme(),
        typography  = VazirmatnTypography,
        content     = content
    )
}

private fun makeColorScheme(): ColorScheme {
    return lightColorScheme(
        background = Color(0xFFF6F6F6),
        primary = Color(0xFF00875F),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFF00875F),
        onPrimaryContainer = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFFFFFF),
        onSecondaryContainer = Color(0xFF212121),
        outlineVariant = Color(0xFFE7E7E7)
    )
}