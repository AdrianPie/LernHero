package com.lernhero.game

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lernhero.game.Character.Character
import com.lernhero.shared.Resources
import com.lernhero.shared.Surface
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun FightScreen(
) {
    val viewModel = koinViewModel<GameViewModel>()
    val state by viewModel.uiState.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Surface
    ){

        Row(modifier = Modifier
            .fillMaxSize()
        ) {
            PlayerContainer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                yourCharacter = { Character(
                    targetScale = scale,
                    spriteCharacter = Resources.Sprite.knightAtk,
                    spriteEffect = Resources.Sprite.fireEffect,
                    onClickPlus = { scale += 0.1f },
                    onClickMinus = { scale -= 0.1f },
                    effect = { viewModel.changeEffectVisibility(!state.playerFirst.effectActive) },
                    hp = 85f,
                    mana = 67f,
                    maxHp = 100f,
                    maxMana = 100f
                ) },
                allyCharacter = { Character(
                    targetScale = scale,
                    spriteCharacter = Resources.Sprite.knight,
                    spriteEffect = Resources.Sprite.fireEffect,
                    onClickPlus = { scale += 0.1f },
                    onClickMinus = { scale -= 0.1f },
                    effect = { viewModel.changeEffectVisibility(!state.playerSecond.effectActive) },
                    hp = 60f,
                    mana = 35f,
                    maxHp = 80f,
                    maxMana = 60f
                )}
            )


            CentralContainer(modifier = Modifier
                .weight(2f)
                .fillMaxHeight()) {

            }
            OpponentContainer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                opponent = { Character(
                    targetScale = scale,
                    spriteCharacter = Resources.Sprite.knight,
                    spriteEffect = Resources.Sprite.fireEffect,
                    onClickPlus = { scale += 0.1f },
                    onClickMinus = { scale -= 0.1f },
                    effect = { viewModel.changeEffectVisibility(!state.effectState) },
                    hp = 95f,
                    mana = 80f,
                    maxHp = 120f,
                    maxMana = 100f
                )},
                opponent2 = { Character(
                    targetScale = scale,
                    spriteCharacter = Resources.Sprite.knight,
                    spriteEffect = Resources.Sprite.fireEffect,
                    onClickPlus = { scale += 0.1f },
                    onClickMinus = { scale -= 0.1f },
                    effect = { viewModel.changeEffectVisibility(!state.effectState) },
                    hp = 40f,
                    mana = 20f,
                    maxHp = 60f,
                    maxMana = 40f
                )},
                opponent3 = { Character(
                    targetScale = scale,
                    spriteCharacter = Resources.Sprite.knight,
                    spriteEffect = Resources.Sprite.fireEffect,
                    onClickPlus = { scale += 0.1f },
                    onClickMinus = { scale -= 0.1f },
                    effect = { viewModel.changeEffectVisibility(!state.effectState) },
                    hp = 70f,
                    mana = 50f,
                    maxHp = 90f,
                    maxMana = 70f
                )}
            )
        }

    }
}

@Composable
fun PlayerContainer(
    modifier: Modifier = Modifier,
    yourCharacter: @Composable () -> Unit,
    allyCharacter: @Composable () -> Unit
){
    Box(
        modifier = modifier
            .border(2.dp, Color.Red),
        contentAlignment = Alignment.Center

    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(modifier = Modifier.fillMaxWidth().border(3.dp, Color.Blue)) { yourCharacter() }
            Row(modifier = Modifier.fillMaxWidth().border(3.dp, Color.Blue)) { allyCharacter() }
        }
    }

}
@Composable
fun CentralContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    Box(
        modifier = modifier
            .border(2.dp, Color.Red),
        contentAlignment = Alignment.Center

    ){
        content()

    }

}
@Composable
fun OpponentContainer(
    modifier: Modifier = Modifier,
    opponent: @Composable () -> Unit,
    opponent2: @Composable () -> Unit,
    opponent3: @Composable () -> Unit,
){
    Box(
        modifier = modifier
            .border(2.dp, Color.Red),
        contentAlignment = Alignment.Center

    ){
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().border(3.dp, Color.Blue),
                horizontalArrangement = Arrangement.Start
            ) { opponent() }
            Row(
                modifier = Modifier.fillMaxWidth().border(3.dp, Color.Blue),
                horizontalArrangement = Arrangement.End
            ) { opponent2() }
            Row(horizontalArrangement = Arrangement.Start) { opponent3() }
        }
    }
}
