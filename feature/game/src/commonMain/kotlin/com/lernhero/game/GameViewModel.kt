package com.lernhero.game

import androidx.lifecycle.ViewModel
import com.lernhero.domain.enum.CharacterState
import com.lernhero.domain.preset.Character
import com.lernhero.game.Character.CharacterSprites
import com.lernhero.game.Character.CharacterUiModel
import com.lernhero.shared.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        GameStateUi(characters = buildInitialCharacters())
    )
    val uiState = _uiState.asStateFlow()

    private fun buildInitialCharacters(): Map<String, CharacterUiModel> {
        val rawData = listOf(
            Character(
                id = "PLAYER_1",
                isPlayer = true,
                avatar = "knight",
                hp = 100,
                maxHp = 100,
                mana = 50,
                maxMana = 50,
                attack = 20
            ),
            Character(
                id = "PLAYER_2",
                isPlayer = true,
                avatar = "knight",
                hp = 80,
                maxHp = 80,
                mana = 100,
                maxMana = 100,
                attack = 12
            ),
            Character(
                id = "PLAYER_3",
                isPlayer = true,
                avatar = "knight",
                hp = 90,
                maxHp = 90,
                mana = 90,
                maxMana = 90,
                attack = 15
            ),
            Character(
                id = "PLAYER_4",
                isPlayer = true,
                avatar = "knight",
                hp = 110,
                maxHp = 110,
                mana = 40,
                maxMana = 40,
                attack = 18
            ),
            Character(
                id = "PLAYER_5",
                isPlayer = true,
                avatar = "knight",
                hp = 85,
                maxHp = 85,
                mana = 70,
                maxMana = 70,
                attack = 24
            ),
            Character(
                id = "ENEMY_1",
                isPlayer = false,
                avatar = "knight",
                hp = 150,
                maxHp = 150,
                mana = 0,
                maxMana = 0,
                attack = 15
            ),
            Character(
                id = "ENEMY_2",
                isPlayer = false,
                avatar = "knight",
                hp = 60,
                maxHp = 60,
                mana = 20,
                maxMana = 20,
                attack = 10
            ),
            Character(
                id = "ENEMY_3",
                isPlayer = false,
                avatar = "knight",
                hp = 150,
                maxHp = 150,
                mana = 0,
                maxMana = 0,
                attack = 15
            ),
            Character(
                id = "ENEMY_4",
                isPlayer = false,
                avatar = "2",
                hp = 140,
                maxHp = 140,
                mana = 0,
                maxMana = 0,
                attack = 14
            ),
            Character(
                id = "ENEMY_5",
                isPlayer = false,
                avatar = "2",
                hp = 120,
                maxHp = 120,
                mana = 0,
                maxMana = 0,
                attack = 17
            )
        )

        return rawData.map { char ->
            CharacterUiModel(
                id = char.id,
                hp = char.hp,
                maxHp = char.maxHp,
                attack = char.attack,
                mana = char.mana,
                isPlayer = char.isPlayer,
                sprites = mapCharacterSprites(char.avatar)
            )
        }.associateBy { it.id }
    }

    fun changeCharacterData(characterId: String) {

        _uiState.update { state ->
            val character = state.characters[characterId]
                ?: return@update state
            val updatedCharacter = character.copy(
                hp = character.hp - 10
            )
            state.copy(
                characters = state.characters.plus(characterId to updatedCharacter)
            )
        }
    }

    fun startTestAttack(attackerId: String, targetId: String) {
        _uiState.update { state ->
            val attacker = state.characters[attackerId] ?: return@update state
            if (!state.characters.containsKey(targetId)) return@update state

            state.copy(
                characters = state.characters.plus(
                    attackerId to attacker.copy(
                        state = CharacterState.MOVING_TO_ATK,
                        currentTargetId = targetId
                    )
                )
            )
        }
    }

    fun onMoveToAttackFinished(characterId: String) {
        updateCharacterState(characterId, CharacterState.ATTACK)
    }

    fun onAttackAnimationFinished(characterId: String) {
        _uiState.update { state ->
            val attacker = state.characters[characterId] ?: return@update state
            val targetId = attacker.currentTargetId ?: return@update state
            val target = state.characters[targetId] ?: return@update state
            val updatedTarget = target.copy(
                hp = (target.hp - attacker.attack).coerceAtLeast(0),
                state = if (target.hp - attacker.attack <= 0) CharacterState.DEAD else CharacterState.HIT
            )
            val updatedAttacker = attacker.copy(state = CharacterState.MOVING_BACK)

            state.copy(
                characters = state.characters
                    .plus(targetId to updatedTarget)
                    .plus(characterId to updatedAttacker)
            )
        }
    }

    fun onMoveBackFinished(characterId: String) {
        _uiState.update { state ->
            val character = state.characters[characterId] ?: return@update state
            state.copy(
                characters = state.characters.plus(
                    characterId to character.copy(
                        state = CharacterState.IDLE,
                        currentTargetId = null
                    )
                )
            )
        }
    }

    private fun updateCharacterState(characterId: String, characterState: CharacterState) {
        _uiState.update { state ->
            val character = state.characters[characterId] ?: return@update state
            state.copy(
                characters = state.characters.plus(
                    characterId to character.copy(state = characterState)
                )
            )
        }
    }

    private fun mapCharacterSprites(key: String) = when (key) {
        "2" -> CharacterSprites(
            idle = Resources.Sprite.knight,
            attack = Resources.Sprite.knightAtk,
            hit = Resources.Sprite.knight,
            dead = Resources.Sprite.knight
        )
        "1", "knight" -> CharacterSprites(
            idle = Resources.Sprite.knight,
            attack = Resources.Sprite.knightAtk,
            hit = Resources.Sprite.knight,
            dead = Resources.Sprite.knight
        )
        else -> CharacterSprites(
            idle = Resources.Sprite.knight,
            attack = Resources.Sprite.knightAtk,
            hit = Resources.Sprite.knight,
            dead = Resources.Sprite.knight
        )
    }
}
