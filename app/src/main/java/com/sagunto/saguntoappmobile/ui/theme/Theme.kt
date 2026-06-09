package com.sagunto.saguntoappmobile.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SaguntoNeonGreen,
    secondary = SaguntoTextGray,
    tertiary = SaguntoDarkGreen,
    background = SaguntoDarkGreen,
    surface = SaguntoDarkGreen,
    onPrimary = SaguntoDarkGreen,
    onSecondary = SaguntoWhite,
    onTertiary = SaguntoWhite,
    onBackground = SaguntoWhite,
    onSurface = SaguntoWhite,
    error = SaguntoErrorRed,
    surfaceVariant = SaguntoDarkGreen.copy(alpha = 0.2f)
)

private val LightColorScheme = lightColorScheme(
    primary = SaguntoDarkGreen,
    secondary = SaguntoTextGray,
    tertiary = SaguntoNeonGreen,
    background = SaguntoBackground,
    surface = SaguntoWhite,
    onPrimary = SaguntoWhite,
    onSecondary = SaguntoDarkGreen,
    onTertiary = SaguntoDarkGreen,
    onBackground = SaguntoDarkGreen,
    onSurface = SaguntoDarkGreen,
    outline = SaguntoDarkGreen,
    error = SaguntoErrorRed,
    surfaceVariant = SaguntoInputBg // Usamos el color de input definido
)

@Composable
fun SaguntoAppMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
