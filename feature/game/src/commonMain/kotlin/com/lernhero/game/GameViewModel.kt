package com.lernhero.game

import androidx.lifecycle.ViewModel
import com.lernhero.domain.preset.Character
import com.lernhero.game.Character.CharacterUiModel
import com.lernhero.shared.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameStateUi())
    val uiState = _uiState.asStateFlow()

    init {
        startBattle()
    }

    fun startBattle() {
        val rawData = listOf(
            // TWOJA DRUŻYNA
            Character(
                id = "PLAYER_1",
                isPlayer = true,
                avatar = "1",
                armor = "1",
                hp = 100, maxHp = 100, mana = 50, maxMana = 50, attack = 20
            ),
            Character(
                id = "PLAYER_2",
                isPlayer = true,
                avatar = "1",
                armor = "1",
                hp = 80, maxHp = 80, mana = 100, maxMana = 100, attack = 12
            ),

            // PRZECIWNICY
            Character(
                id = "ENEMY_1",
                isPlayer = false,
                avatar = "1",
                armor = "1",
                hp = 150, maxHp = 150, mana = 0, maxMana = 0, attack = 15
            ),
            Character(
                id = "ENEMY_2",
                isPlayer = false,
                avatar = "1",
                armor = "1",
                hp = 60, maxHp = 60, mana = 20, maxMana = 20, attack = 10
            )
        )
        val uiModels = rawData.map { char ->
            CharacterUiModel(
                id = char.id,
                hp = char.hp,
                maxHp = char.maxHp,
                attack = char.attack,
                mana = char.mana,
                isPlayer = char.isPlayer,
                avatarRes = mapAvatar(char.avatar),
                armorRes = mapArmor(char.armor)
            )
        }.associateBy { it.id }

        _uiState.value = GameStateUi(characters = uiModels)
    }


    private fun mapAvatar(key: String) = when(key) {
        "1" -> Resources.Image.battleMageAvatarGame
        else -> Resources.Image.battleMageAvatarGame
    }

    private fun mapArmor(key: String) = when(key) {
        "1" -> Resources.Image.boundsDragonArmor
        else -> Resources.Image.boundsDragonArmor
    }


//    fun addEffect(targetId: CharacterId, effectId: EffectId) {
//        _uiState.update { state ->
//            state.copy(
//                characters = state.characters.mapValues { (id, c) ->
//                    if (id == targetId) {
//                        c.copy(
//                            effects = c.effects + EffectUiState(effectId)
//                        )
//                    } else c
//                }
//            )
//        }
//    }

//    fun removeEffect(targetId: CharacterId, effectId: EffectId) {
//        _uiState.update { state ->
//            state.copy(
//                characters = state.characters.mapValues { (id, c) ->
//                    if (id == targetId) {
//                        c.copy(
//                            effects = c.effects.filterNot { it.effectId == effectId }
//                        )
//                    } else c
//                }
//            )
//        }
//    }

}
