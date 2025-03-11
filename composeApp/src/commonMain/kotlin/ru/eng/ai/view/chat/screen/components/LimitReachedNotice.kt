package ru.eng.ai.view.chat.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.eng.ai.view.theme.EngTheme

@Composable
fun LimitReachedNotice() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(EngTheme.colors.primary)
            .padding(
                vertical = 18.dp,
                horizontal = 20.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Вы превысили лимит общения на сегодня",
            style = EngTheme.typography.medium14,
            color = EngTheme.colors.dimSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}