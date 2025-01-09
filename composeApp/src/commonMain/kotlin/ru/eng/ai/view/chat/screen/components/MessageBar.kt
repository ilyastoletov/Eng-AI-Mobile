package ru.eng.ai.view.chat.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import engai.composeapp.generated.resources.Res
import engai.composeapp.generated.resources.ic_send
import org.jetbrains.compose.resources.painterResource
import ru.eng.ai.extension.isImeVisible
import ru.eng.ai.tool.Logger
import ru.eng.ai.view.theme.EngTheme

@Composable
fun MessageBar(
    fastReplyOptions: List<String>,
    onSendMessage: (String) -> Unit,
) {
    var messageText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(EngTheme.colors.primary)
            .padding(
                vertical = 12.dp,
                horizontal = 20.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MessageTextField(
            modifier = Modifier.fillMaxWidth(),
            text = messageText,
            onChangeText = { messageText = it },
            onClickSend = {
                if (messageText.isNotEmpty()) {
                    onSendMessage(messageText)
                    messageText = ""
                }
            }
        )
        if (fastReplyOptions.isNotEmpty()) {
            Spacer(
                modifier = Modifier.height(28.dp)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                fastReplyOptions.forEach { text ->
                    FastReplyButton(
                        modifier = Modifier.fillMaxWidth(),
                        replyText = text,
                        onClick = { onSendMessage(text) }
                    )
                }
            }
        }
    }

}

@Composable
private fun MessageTextField(
    modifier: Modifier = Modifier,
    text: String,
    onChangeText: (String) -> Unit,
    onClickSend: () -> Unit
) {
    BasicTextField(
        modifier = modifier,
        value = text,
        onValueChange = onChangeText,
        textStyle = EngTheme.typography.medium14,
    ) { innerTextField ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = EngTheme.colors.primaryVariant,
                    shape = RoundedCornerShape(30.dp)
                ),
            verticalAlignment = Alignment.Bottom
        ) {
            Box(
                modifier = Modifier
                    .weight(0.85f)
                    .padding(
                        top = 14.dp,
                        bottom = 14.dp,
                        start = 22.dp,
                        end = 12.dp
                    )
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = "Сообщение...",
                        style = EngTheme.typography.medium14,
                        color = EngTheme.colors.dimTertiary
                    )
                }
                innerTextField.invoke()
            }
            Box(
                modifier = Modifier
                    .weight(0.15f)
                    .padding(6.dp)
                    .size(36.dp)
                    .background(
                        color = EngTheme.colors.primary,
                        shape = CircleShape
                    )
                    .padding(
                        start = 10.dp,
                        end = 6.dp,
                        top = 6.dp,
                        bottom = 6.dp
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClickSend
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_send),
                    tint = EngTheme.colors.dimSecondary,
                    contentDescription = null,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
private fun FastReplyButton(
    modifier: Modifier = Modifier,
    replyText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = EngTheme.colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = replyText,
            textAlign = TextAlign.Center,
            style = EngTheme.typography.semiBold16,
            color = EngTheme.colors.dimSecondary
        )
    }
}