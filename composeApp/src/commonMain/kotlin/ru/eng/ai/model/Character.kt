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
) {

    data object Traveler : Character(
        nameResource = Res.string.traveler_name,
        shortDescriptionResource = Res.string.traveler_short_description,
        descriptionResource = Res.string.traveler_description,
        avatarResource = Res.drawable.traveler_avatar
    )

    data object Scientist : Character(
        nameResource = Res.string.professor_name,
        shortDescriptionResource = Res.string.professor_short_description,
        descriptionResource = Res.string.professor_description,
        avatarResource = Res.drawable.scientist_avatar
    )

    data object NativeSpeaker : Character(
        nameResource = Res.string.native_speaker_name,
        shortDescriptionResource = Res.string.native_speaker_short_description,
        descriptionResource = Res.string.native_speaker_description,
        avatarResource = Res.drawable.native_speaker_avatar
    )

}
