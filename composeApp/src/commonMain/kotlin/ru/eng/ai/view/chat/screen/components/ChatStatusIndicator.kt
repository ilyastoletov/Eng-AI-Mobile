package ru.eng.ai.view.chat.screen.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import ru.eng.ai.view.chat.viewmodel.enumeration.ChatStatus
import ru.eng.ai.view.theme.EngTheme

@Composable
fun ChatStatusIndicator(currentStatus: ChatStatus) {
    when(currentStatus) {
        ChatStatus.NONE -> {}
        ChatStatus.WRITING -> StatusWriting()
        ChatStatus.ERROR -> {
            Text(
                text = "Ошибка",
                style = EngTheme.typography.medium14,
                color = EngTheme.colors.onBackground
            )
        }
        ChatStatus.RECONNECT -> {
            Text(
                text = "Подключение...",
                style = EngTheme.typography.bold14,
                color = EngTheme.colors.dimSecondary
            )
        }
    }
}

@Composable
private fun StatusWriting() {
    val circleAlpha = remember { Animatable(initialValue = 1F) }

    LaunchedEffect(Unit) {
        circleAlpha.animateTo(
            targetValue = 0F,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 500),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer { alpha = circleAlpha.value }
                .size(12.dp)
                .background(
                    color = EngTheme.colors.secondary,
                    shape = CircleShape
                )
        )
        Text(
            text = "Печатает...",
            style = EngTheme.typography.medium14,
            color = EngTheme.colors.secondary
        )
    }
}