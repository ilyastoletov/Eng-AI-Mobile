package ru.eng.ai.view.chat.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import engai.composeapp.generated.resources.Res
import engai.composeapp.generated.resources.ic_delete
import engai.composeapp.generated.resources.ic_person_list
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.eng.ai.model.Character
import ru.eng.ai.view.chat.screen.bottomsheet.CharacterInfoBottomSheet
import ru.eng.ai.view.chat.viewmodel.enumeration.ChatStatus
import ru.eng.ai.view.shared.Avatar
import ru.eng.ai.view.theme.EngTheme

@Composable
fun ChatTopBar(
    character: Character,
    chatStatus: ChatStatus,
    onClickChangeCharacter: () -> Unit,
    onClickClearChatHistory: () -> Unit,
) {
    var characterInfoBottomSheetVisible by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = EngTheme.colors.primary)
            .padding(
                vertical = 13.dp,
                horizontal = 20.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(0.8f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClickChangeCharacter) {
                Icon(
                    painter = painterResource(Res.drawable.ic_person_list),
                    tint = EngTheme.colors.dimSecondary,
                    contentDescription = null,
                )
            }
            Spacer(
                modifier = Modifier.width(8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { characterInfoBottomSheetVisible = true }
                )
            ) {
                Avatar(
                    painter = painterResource(character.avatarResource),
                    size = 60.dp
                )
                Spacer(
                    modifier = Modifier.width(12.dp)
                )
                Column {
                    Text(
                        text = stringResource(character.nameResource),
                        style = EngTheme.typography.bold20,
                        color = EngTheme.colors.dimSecondary
                    )
                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )
                    ChatStatusIndicator(
                        currentStatus = chatStatus,
                        characterName = character.shortDescriptionResource
                    )
                }
            }
        }

        IconButton(
            onClick = onClickClearChatHistory,
            modifier = Modifier.weight(0.2f)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_delete),
                tint = EngTheme.colors.dimTertiary,
                contentDescription = null,
            )
        }
    }

    if (characterInfoBottomSheetVisible) {
        CharacterInfoBottomSheet(
            avatarResource = character.avatarResource,
            description = stringResource(character.descriptionResource),
            name = stringResource(character.nameResource),
            shortDescription = stringResource(character.shortDescriptionResource),
            onDismiss = { characterInfoBottomSheetVisible = false }
        )
    }
}