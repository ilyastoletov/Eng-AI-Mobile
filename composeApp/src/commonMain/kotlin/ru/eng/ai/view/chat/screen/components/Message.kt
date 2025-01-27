package ru.eng.ai.view.chat.screen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import engai.composeapp.generated.resources.Res
import engai.composeapp.generated.resources.ic_copy
import engai.composeapp.generated.resources.ic_pin
import engai.composeapp.generated.resources.ic_pin_filled
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ru.eng.ai.view.theme.EngTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageItem(
    text: String,
    isOwn: Boolean,
    sendingTime: String,
    isPinned: Boolean,
    onCopy: () -> Unit,
    onPin: () -> Unit
) {
    var optionsMenuVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isOwn) Alignment.End else Alignment.Start
    ) {
        Box {
            Row(
                modifier = Modifier
                    .background(
                        color = if (isOwn) EngTheme.colors.secondary else EngTheme.colors.primary,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(
                        vertical = 8.dp,
                        horizontal = 19.dp
                    )
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                        onLongClick = { optionsMenuVisible = true }
                    )
            ) {
                Text(
                    text = text,
                    style = EngTheme.typography.medium14,
                    color = EngTheme.colors.dimSecondary,
                    modifier = Modifier
                )
            }
            MessageOptionsMenu(
                expanded = optionsMenuVisible,
                isPinned = isPinned,
                onDismiss = { optionsMenuVisible = false },
                onClickCopy = onCopy,
                onClickPin = onPin
            )
        }
        Spacer(
            modifier = Modifier.height(4.dp)
        )
        Row(
            modifier = Modifier.padding(horizontal = 19.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = sendingTime,
                style = EngTheme.typography.medium12,
                color = EngTheme.colors.dimTertiary,
                textAlign = if (isOwn) TextAlign.End else TextAlign.Start,
            )
            if (isPinned) {
                Spacer(
                    modifier = Modifier.width(6.dp)
                )
                Icon(
                    painter = painterResource(Res.drawable.ic_pin_filled),
                    contentDescription = null,
                    tint = EngTheme.colors.secondary,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
private fun MessageOptionsMenu(
    expanded: Boolean,
    isPinned: Boolean,
    onDismiss: () -> Unit,
    onClickCopy: () -> Unit,
    onClickPin: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        MessageOption(
            text = "Копировать",
            iconRes = Res.drawable.ic_copy,
            onClick = { onClickCopy(); onDismiss() }
        )
        MessageOption(
            text = if (isPinned) "Открепить" else "Закрепить",
            iconRes = Res.drawable.ic_pin,
            onClick = { onClickPin(); onDismiss() }
        )
    }
}

@Composable
private fun MessageOption(
    text: String,
    iconRes: DrawableResource,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                style = EngTheme.typography.semiBold12,
                color = Color(0xFF31284D)
            )
        },
        onClick = onClick,
        trailingIcon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color(0xFF31284D)
            )
        },
        colors = MenuDefaults.itemColors()
    )
}