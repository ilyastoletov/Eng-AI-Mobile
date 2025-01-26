package ru.eng.ai.model

import engai.composeapp.generated.resources.Res
import engai.composeapp.generated.resources.traveler_avatar
import engai.composeapp.generated.resources.scientist_avatar
import engai.composeapp.generated.resources.native_speaker_avatar
import org.jetbrains.compose.resources.DrawableResource

sealed class Character(
    val name: String,
    val shortDescription: String,
    val description: String,
    val avatarResource: DrawableResource,
) {

    data object Traveler : Character(
        name = "Грегори",
        shortDescription = "Путешественник",
        description = "Любознательный и активный путешественник. Побывал во многих странах и с удовольствием расскажет о культуре и особенности языка каждой из стран. ",
        avatarResource = Res.drawable.traveler_avatar
    )

    data object Scientist : Character(
        name = "Мисс Дейвис",
        shortDescription = "Профессор",
        description = "Бренда Дейвис профессор в Оксфордском университете. Имеет научных степеней, с легкостью побеседует на научные темы.",
        avatarResource = Res.drawable.scientist_avatar
    )

    data object NativeSpeaker : Character(
        name = "Тайлер",
        shortDescription = "Носитель языка",
        description = "Дружелюбный житель Калифорнии. Быстро адаптируется к стилю общения собеседника, часто использует сленги в разговоре.",
        avatarResource = Res.drawable.native_speaker_avatar
    )

}
