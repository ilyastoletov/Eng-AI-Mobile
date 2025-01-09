package ru.eng.ai.view.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Avatar(
    painter: Painter,
    size: Dp
) {
    Image(
        painter = painter,
        contentDescription = null,
        contentScale = Crop,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
    )
}