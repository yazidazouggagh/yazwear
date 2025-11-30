package com.yazwear.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.yazwear.ui.theme.Black
import com.example.yazwear.ui.theme.White

@Composable
fun YazWearTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = androidx.compose.material3.lightColorScheme(
            primary = Black,
            onPrimary = White,
            background = White,
            onBackground = Black
        ),
        typography = Typography,
        content = content
    )
}