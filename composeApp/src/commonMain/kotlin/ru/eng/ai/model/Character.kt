package ru.eng.ai.model

import engai.composeapp.generated.resources.Res
import engai.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

sealed class Character(
    val nameResource: StringResource,
    val shortDescriptionResource: StringResource,
    val descriptionResource: StringResource,
    val avatarResource: DrawableResource,
    val internalName: String,
    val fastReplies: List<StringResource>,
) {

    data object Traveler : Character(
        nameResource = Res.string.traveler_name,
        shortDescriptionResource = Res.string.traveler_short_description,
        descriptionResource = Res.string.traveler_description,
        avatarResource = Res.drawable.traveler_avatar,
        internalName = "traveler",
        fastReplies = listOf(
            Res.string.traveler_fast_reply_first,
            Res.string.traveler_fast_reply_second
        )
    )

    data object Scientist : Character(
        nameResource = Res.string.professor_name,
        shortDescriptionResource = Res.string.professor_short_description,
        descriptionResource = Res.string.professor_description,
        avatarResource = Res.drawable.scientist_avatar,
        internalName = "professor",
        fastReplies = listOf(
            Res.string.professor_fast_reply_first,
            Res.string.professor_fast_reply_second
        )
    )

    data object NativeSpeaker : Character(
        nameResource = Res.string.native_speaker_name,
        shortDescriptionResource = Res.string.native_speaker_short_description,
        descriptionResource = Res.string.native_speaker_description,
        avatarResource = Res.drawable.native_speaker_avatar,
        internalName = "native_speaker",
        fastReplies = listOf(
            Res.string.native_speaker_fast_reply_first,
            Res.string.native_speaker_fast_reply_second
        )
    )

//    companion object {
//        val languageCheckFastReplies = listOf(
//            Res.string.grammar_check,
//            Res.string.spelling_check
//        )
//    }
}
