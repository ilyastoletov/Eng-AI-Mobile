package ru.eng.ai.view.chat.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import engai.composeapp.generated.resources.Res
import engai.composeapp.generated.resources.enter_message
import engai.composeapp.generated.resources.ic_chevron_left
import engai.composeapp.generated.resources.ic_send
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.eng.ai.tool.keyboardAsState
import ru.eng.ai.view.theme.EngTheme

@Composable
fun MessageBar(
    fastReplyOptions: List<String>,
    isSendingAllowed: Boolean,
    onSendMessage: (String) -> Unit,
) {
    var messageText by remember { mutableStateOf("") }

    val fastReplyOptionsEnabled = remember(fastReplyOptions) { fastReplyOptions.isNotEmpty() }
    var isReplyOptionsVisible by remember { mutableStateOf(true) }

    val isKeyboardVisible by keyboardAsState()
    LaunchedEffect(isKeyboardVisible) {
        if (isKeyboardVisible && isReplyOptionsVisible) {
            isReplyOptionsVisible = false
        }
    }

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
            enableFastReplyButton = fastReplyOptionsEnabled,
            isFastReplyOpen = isReplyOptionsVisible,
            isSendingAllowed = isSendingAllowed,
            onToggleFastReply = { isReplyOptionsVisible = !isReplyOptionsVisible },
            onClickSend = {
                if (messageText.isNotEmpty()) {
                    onSendMessage(messageText)
                    messageText = ""
                }
            }
        )
        if (fastReplyOptionsEnabled && isReplyOptionsVisible) {
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
                        onClick = {
                            onSendMessage(text)
                            isReplyOptionsVisible = false
                        }
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
    enableFastReplyButton: Boolean,
    isFastReplyOpen: Boolean,
    isSendingAllowed: Boolean,
    onToggleFastReply: () -> Unit,
    onChangeText: (String) -> Unit,
    onClickSend: () -> Unit
) {
    BasicTextField(
        modifier = modifier,
        value = text,
        onValueChange = onChangeText,
        cursorBrush = SolidColor(EngTheme.colors.dimSecondary),
        textStyle = EngTheme.typography.medium14
            .copy(color = EngTheme.colors.dimSecondary),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences
        )
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
            if (enableFastReplyButton) {
                ToggleFastReplyButton(
                    modifier = Modifier
                        .weight(0.15f)
                        .padding(
                            start = 10.dp,
                            bottom = 6.dp,
                            top = 6.dp
                        ),
                    open = isFastReplyOpen,
                    onClick = onToggleFastReply
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .padding(
                        top = 14.dp,
                        bottom = 14.dp,
                        start = 15.dp,
                        end = 12.dp
                    )
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.enter_message),
                        style = EngTheme.typography.medium14,
                        color = EngTheme.colors.dimTertiary
                    )
                }
                innerTextField.invoke()
            }
            SendMessageButton(
                isEnabled = isSendingAllowed,
                onClick = onClickSend
            )
        }
    }
}

@Composable
private fun ToggleFastReplyButton(
    modifier: Modifier,
    open: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .background(
                color = EngTheme.colors.primary,
                shape = CircleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_chevron_left),
            tint = EngTheme.colors.dimSecondary,
            contentDescription = null,
            modifier = Modifier
                .rotate(degrees = if (open) 90f else -90f)
        )
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

@Composable
private fun RowScope.SendMessageButton(
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val alpha = remember(isEnabled) { if (isEnabled) 1.0F else 0.6F }

    Box(
        modifier = Modifier
            .alpha(alpha)
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
                onClick = { if (isEnabled) onClick() }
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