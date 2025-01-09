package ru.eng.ai.view.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.eng.ai.view.theme.EngCustomTheme
import ru.eng.ai.view.theme.EngTheme

@Composable
fun TonalButton(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true,
    backgroundColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    EButton(
        modifier = modifier,
        backgroundColor = backgroundColor,
        enabled = enabled,
        enableBorder = true,
        borderColor = borderColor,
        onClick = onClick
    ) {
        Text(
            text = label,
            style = EngTheme.typography.bold24,
            color = EngTheme.colors.lightSecondary
        )
    }
}

@Composable
fun FilledButton(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true,
    backgroundColor: Color = EngTheme.colors.lightSecondary,
    onClick: () -> Unit
) {
    EButton(
        modifier = modifier,
        backgroundColor = backgroundColor,
        enabled = enabled,
        onClick = onClick
    ) {
        Text(
            text = label,
            style = EngTheme.typography.bold24,
            color = EngTheme.colors.primary
        )
    }
}

@Composable
private fun EButton(
    modifier: Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
    backgroundColor: Color,
    enableBorder: Boolean = false,
    borderColor: Color = Color.Transparent,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .alpha(alpha = if (enabled) 1.0f else 0.7f)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(15.dp)
            )
            .then(
                if (enableBorder) {
                    Modifier.border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(15.dp)
                    )
                } else Modifier
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        content.invoke()
    }
}
