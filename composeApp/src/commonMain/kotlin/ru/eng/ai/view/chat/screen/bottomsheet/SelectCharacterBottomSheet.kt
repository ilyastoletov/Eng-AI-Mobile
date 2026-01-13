package ru.eng.ai.view.chat.screen.bottomsheet

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
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import engai.composeapp.generated.resources.Res
import engai.composeapp.generated.resources.feedback_subtitle
import engai.composeapp.generated.resources.feedback_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.eng.ai.model.Character
import ru.eng.ai.view.shared.Avatar
import ru.eng.ai.view.theme.EngTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCharacterBottomSheet(
    selected: Character,
    onDismiss: () -> Unit,
    onSelect: (Character) -> Unit,
    onClickFeedback: () -> Unit
) {
    val characters = remember {
        listOf(Character.Traveler, Character.Scientist, Character.NativeSpeaker)
    }

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = EngTheme.colors.primary,
        onDismissRequest = onDismiss
    ) {
        characters.fastForEach { character ->
            SelectCharacterItem(
                avatarResource = character.avatarResource,
                name = stringResource(character.nameResource),
                shortDescription = stringResource(character.shortDescriptionResource),
                isSelected = selected == character,
                onClick = {
                    onSelect(character)
                    onDismiss()
                }
            )
        }
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        ApplicationFeedback(
            onClick = onClickFeedback
        )
        Spacer(
            modifier = Modifier.height(30.dp)
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

@Composable
private fun ApplicationFeedback(onClick: () -> Unit) {
    val feedbackText = buildAnnotatedString {
        append(stringResource(Res.string.feedback_title))
        withStyle(
            style = SpanStyle(
                color = EngTheme.colors.background,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(stringResource(Res.string.feedback_subtitle))
        }
    }

    Text(
        text = feedbackText,
        style = EngTheme.typography.medium12,
        color = EngTheme.colors.dimSecondary,
        modifier = Modifier
            .alpha(0.5F)
            .padding(horizontal = 30.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    )
}