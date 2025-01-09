package ru.eng.ai.view.chat.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import engai.composeapp.generated.resources.Res
import engai.composeapp.generated.resources.ic_info
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ru.eng.ai.model.Character
import ru.eng.ai.view.shared.Avatar
import ru.eng.ai.view.theme.EngTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCharacterBottomSheet(
    selected: Character,
    onDismiss: () -> Unit,
    onSelect: (Character) -> Unit
) {
    val characters = remember {
        listOf(Character.Traveler, Character.Scientist, Character.NativeSpeaker)
    }

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = onDismiss
    ) {
        characters.fastForEach { character ->
            SelectCharacterItem(
                avatarResource = character.avatarResource,
                name = character.name,
                shortDescription = character.shortDescription,
                isSelected = selected == character,
                onClick = {
                    onSelect(character)
                    onDismiss()
                }
            )
        }
        Spacer(
            modifier = Modifier.height(60.dp)
        )
    }

}

@Composable
private fun SelectCharacterItem(
    avatarResource: DrawableResource,
    name: String,
    shortDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) {
                    EngTheme.colors.primaryVariant
                } else {
                    EngTheme.colors.primary
                }
            )
            .padding(
                horizontal = 30.dp,
                vertical = 10.dp
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar(
            painter = painterResource(avatarResource),
            size = 60.dp
        )
        Spacer(
            modifier = Modifier.width(12.dp)
        )
        Column {
            Text(
                text = name,
                style = EngTheme.typography.bold20,
                color = EngTheme.colors.dimSecondary
            )
            Spacer(
                modifier = Modifier.height(4.dp)
            )
            Text(
                text = shortDescription,
                style = EngTheme.typography.semiBold12,
                color = EngTheme.colors.dimTertiary
            )
        }
    }
}