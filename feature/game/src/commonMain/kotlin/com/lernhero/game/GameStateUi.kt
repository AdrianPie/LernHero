package com.lernhero.game

import com.lernhero.game.Character.CharacterUiModel
import com.lernhero.game.Character.EffectId

typealias CharacterId = String

data class GameStateUi(
    val characters: Map<CharacterId, CharacterUiModel> = emptyMap()
)

data class EffectUiState(
    val effectId: EffectId
)
