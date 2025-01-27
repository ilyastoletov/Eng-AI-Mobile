package ru.eng.ai.view.chat.screen.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import ru.eng.ai.model.Message
import ru.eng.ai.view.theme.EngTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinnedMessagesBottomSheet(
    pinned: List<Message>,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = EngTheme.colors.primaryVariant,
        onDismissRequest = onDismiss
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                top = 0.dp,
                start = 20.dp,
                end = 20.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Text(
                    text = "Закреплённые сообщения",
                    style = EngTheme.typography.semiBold16,
                    color = EngTheme.colors.dimSecondary
                )
            }

            items(pinned) {
                PinnedMessage(
                    text = it.text,
                    sendingTime = it.sendingTime,
                    isOwn = it.isOwn
                )
            }
        }
    }
}

@Composable
private fun PinnedMessage(
    text: String,
    sendingTime: String,
    isOwn: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                color = EngTheme.colors.primary,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(
                horizontal = 20.dp,
                vertical = 13.dp
            )
    ) {
        Row {
            Text(
                text = sendingTime,
                style = EngTheme.typography.medium12,
                color = EngTheme.colors.dimSecondary
                    .copy(alpha = 0.4f)
            )
            Spacer(
                modifier = Modifier.width(12.dp)
            )
            Text(
                text = if (isOwn) "Вы" else "Персонаж",
                style = EngTheme.typography.medium12,
                color = EngTheme.colors.dimSecondary
                    .copy(alpha = 0.4f)
            )
        }
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Text(
            text = text,
            style = EngTheme.typography.medium16,
            color = EngTheme.colors.dimSecondary
        )
    }
}