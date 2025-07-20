package ru.eng.ai.view.chat.screen.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import engai.composeapp.generated.resources.Res
import engai.composeapp.generated.resources.feedback_text
import org.jetbrains.compose.resources.stringResource
import ru.eng.ai.view.theme.EngTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackBottomSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = EngTheme.colors.primary,
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .padding(
                    top = 20.dp,
                    bottom = 60.dp
                )
        ) {
            Text(
                text = stringResource(Res.string.feedback_text),
                style = EngTheme.typography.medium16,
                color = EngTheme.colors.dimSecondary
            )
        }
    }
}