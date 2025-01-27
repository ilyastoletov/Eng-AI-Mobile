package ru.eng.ai.view.chat.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import engai.composeapp.generated.resources.Res
import engai.composeapp.generated.resources.ic_pin
import org.jetbrains.compose.resources.painterResource
import ru.eng.ai.model.Message
import ru.eng.ai.view.chat.screen.bottomsheet.PinnedMessagesBottomSheet
import ru.eng.ai.view.theme.EngTheme

@Composable
fun PinnedMessagesBar(
    messages: List<Message>
) {
    val pinned = remember(messages) {
        messages.filter { it.isPinned }
    }
    var bottomSheetExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 1.dp)
            .background(color = EngTheme.colors.primary)
            .padding(
                vertical = 12.dp,
                horizontal = 20.dp
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { bottomSheetExpanded = true }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_pin),
            contentDescription = null,
            tint = EngTheme.colors.dimSecondary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(
            modifier = Modifier.width(13.dp)
        )
        Text(
            text = "Закреплённые сообщения",
            style = EngTheme.typography.semiBold16,
            color = EngTheme.colors.dimSecondary
        )
    }

    if (bottomSheetExpanded) {
        PinnedMessagesBottomSheet(
            pinned = pinned,
            onDismiss = { bottomSheetExpanded = false }
        )
    }
}