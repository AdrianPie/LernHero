package com.lernhero.game

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.lernhero.game.Character.Character
import com.lernhero.shared.Resources
import com.lernhero.shared.Surface
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun GameScreen(
) {
    val viewModel = koinViewModel<GameViewModel>()
    val state by viewModel.uiState.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Surface
    ){
        var scale by remember { mutableStateOf(1f) }
        Character(
            targetScale = scale,
            spriteCharacter = Resources.Sprite.knight,
            spriteEffect = Resources.Sprite.fireEffect,
            onClickPlus = { scale += 0.1f },
            onClickMinus = { scale -= 0.1f },
            onClickEffect = {  viewModel.changeEffectVisibility(!state.effectState) }
        )


    }

}
