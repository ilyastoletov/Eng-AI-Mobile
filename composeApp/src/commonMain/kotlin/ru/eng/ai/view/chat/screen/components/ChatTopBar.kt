package ru.eng.ai.view.chat.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import engai.composeapp.generated.resources.Res
import engai.composeapp.generated.resources.ic_arrow_down
import engai.composeapp.generated.resources.ic_info
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ru.eng.ai.model.Character
import ru.eng.ai.view.shared.Avatar
import ru.eng.ai.view.theme.EngTheme

@Composable
fun ChatTopBar(
    character: Character,
    onClickChangeCharacter: () -> Unit,
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
                    painter = painterResource(Res.drawable.ic_arrow_down),
                    tint = EngTheme.colors.dimSecondary,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(
                modifier = Modifier.width(8.dp)
            )
            Avatar(
                painter = painterResource(character.avatarResource),
                size = 60.dp
            )
            Spacer(
                modifier = Modifier.width(12.dp)
            )
            Column {
                Text(
                    text = character.name,
                    style = EngTheme.typography.bold20,
                    color = EngTheme.colors.dimSecondary
                )
                Spacer(
                    modifier = Modifier.height(4.dp)
                )
                Text(
                    text = character.shortDescription,
                    style = EngTheme.typography.semiBold12,
                    color = EngTheme.colors.dimTertiary
                )
            }
        }

        IconButton(
            onClick = { characterInfoBottomSheetVisible = true },
            modifier = Modifier.weight(0.2f)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_info),
                tint = EngTheme.colors.dimTertiary,
                contentDescription = null,
            )
        }
    }

    if (characterInfoBottomSheetVisible) {
        CharacterInfoBottomSheet(
            avatarResource = character.avatarResource,
            description = character.description,
            name = character.name,
            shortDescription = character.shortDescription,
            onDismiss = { characterInfoBottomSheetVisible = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterInfoBottomSheet(
    avatarResource: DrawableResource,
    name: String,
    description: String,
    shortDescription: String,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Avatar(
                painter = painterResource(avatarResource),
                size = 100.dp
            )
            Spacer(
                modifier = Modifier.height(12.dp)
            )
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
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            Text(
                text = description,
                textAlign = TextAlign.Center,
                style = EngTheme.typography.semiBold16,
                color = EngTheme.colors.dimTertiary,
                fontSize = 14.sp
            )
        }
    }
}