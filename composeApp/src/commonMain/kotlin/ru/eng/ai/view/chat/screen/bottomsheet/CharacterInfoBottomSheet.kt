package ru.eng.ai.view.chat.screen.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ru.eng.ai.view.shared.Avatar
import ru.eng.ai.view.theme.EngTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterInfoBottomSheet(
    avatarResource: DrawableResource,
    name: String,
    description: String,
    shortDescription: String,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = EngTheme.colors.primary,
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