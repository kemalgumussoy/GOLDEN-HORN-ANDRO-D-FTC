package com.kemal.goldenhorn

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ==== BIOBUZZ 2026-2027 renk paleti (iOS ile aynı) ====
object Biobuzz {
    val honey   = Color(0xFFF2A900)
    val amber   = Color(0xFFE08A00)
    val leaf    = Color(0xFF7FB069)
    val forest  = Color(0xFF2D4A22)
    val bark    = Color(0xFF1A1F16)
    val bgCard  = Color(0xFF252B1F)
    val mutedG  = Color(0xFF8A9580)
    val cream   = Color(0xFFF5F0E1)
    val bloodO  = Color(0xFFD9534F)
}

private val BiobuzzScheme = darkColorScheme(
    primary = Biobuzz.honey,
    onPrimary = Biobuzz.bark,
    secondary = Biobuzz.leaf,
    background = Biobuzz.bark,
    onBackground = Biobuzz.cream,
    surface = Biobuzz.bgCard,
    onSurface = Biobuzz.cream,
    error = Biobuzz.bloodO
)

@Composable
fun GoldenHornTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BiobuzzScheme,
        content = content
    )
}
