package com.lernhero.game.Character

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lernhero.game.GameViewModel
import com.stevdza_san.sprite.component.SpriteView
import com.stevdza_san.sprite.domain.SpriteSheet
import com.stevdza_san.sprite.domain.SpriteSpec
import com.stevdza_san.sprite.domain.rememberSpriteState
import com.stevdza_san.sprite.util.getScreenWidth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Character(
    hp: Float = 80f,
    mana: Float = 50f,
    maxHp: Float = 100f,
    maxMana: Float = 100f,
    sprite : DrawableResource,
    targetScale : Float = 1.0f,
    onClickPlus : () -> Unit,
    onClickMinus : () -> Unit,
    onClickEffect : () -> Unit,
    effectSprite: DrawableResource,
) {
    val  scale by animateFloatAsState(
        targetScale,
        animationSpec = tween(600,100, LinearOutSlowInEasing),
    )
    Box(
        modifier = Modifier
            .wrapContentSize()
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Staty NAD postacią
            CharacterStats(
                hp = hp,
                mana = mana,
                maxHp = maxHp,
                maxMana = maxMana,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(6.dp))

            // Sama postać
            AnimatedCharacter(
                spriteCharacter = sprite,
                spriteEffect = effectSprite
            )
            Button(
                onClick = {
                    onClickPlus()
                }
            ) {
                Text("Change Scale + test")
            }
            Button(
                onClick = {
                    onClickMinus()
                }
            ) {
                Text("Change Scale - test")
            }
            Button(
                onClick = {
                    onClickEffect()
                }
            ) {
                Text("Effect test")
            }
        }

    }
}

@Composable
fun AnimatedCharacter(
    spriteCharacter : DrawableResource,
    spriteEffect : DrawableResource,
){
    val viewModel = koinViewModel<GameViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    val scope = rememberCoroutineScope()
    val screenWidth = getScreenWidth()


    val spriteCharacterState = rememberSpriteState(
        totalFrames = 9,
        framesPerRow = 3,
        animationSpeed = 100
    )
    val spriteEffectState = rememberSpriteState(
        totalFrames = 21,
        framesPerRow = 7,
        animationSpeed = 100
    )
    val currentEffectFrame by spriteEffectState.currentFrame.collectAsState()

    val animationRunning by spriteCharacterState.isRunning.collectAsState()
    DisposableEffect(Unit) {
        onDispose {
            spriteCharacterState.stop()
            spriteCharacterState.cleanup()
        }
    }

    
    LaunchedEffect(Unit) {
            spriteCharacterState.start()
    }


    Box(
        contentAlignment = Alignment.Center
    ){
        SpriteView(
            spriteState = spriteCharacterState,
            spriteSpec = SpriteSpec(
                screenWidth = screenWidth.value,
                default = SpriteSheet(
                    frameWidth = 587,
                    frameHeight = 707,
                    image = spriteCharacter
            )
        )
    )
        LaunchedEffect(uiState.effectState) {
            if (uiState.effectState) {
                spriteEffectState.start()
            }
        }
        AnimatedVisibility(
            visible = uiState.effectState
        ) {
            LaunchedEffect(currentEffectFrame) {
                if (currentEffectFrame == 20){
                    spriteEffectState.stop()
                    viewModel.changeEffectVisibility(false)
                }
            }
            SpriteView(
                modifier = Modifier.scale(8f),
                spriteState = spriteEffectState,
                spriteSpec = SpriteSpec(
                    screenWidth = screenWidth.value,
                    default = SpriteSheet(
                        frameWidth = 128,
                        frameHeight = 128,
                        image = spriteEffect
                    )
                )
            )
        }

    }

}
@Composable
fun CharacterStats(
    modifier: Modifier,
    hp: Float,
    mana: Float,
    maxHp: Float,
    maxMana: Float,
){
    val animatedHp by animateFloatAsState(
        targetValue = hp/maxHp,
        animationSpec = tween(600,100, LinearOutSlowInEasing),
    )
    val animatedMana by animateFloatAsState(
        targetValue = mana/maxMana,
        animationSpec = tween(600,100, LinearOutSlowInEasing),
    )
    Column(
        modifier = modifier
    ) {
        Text("HP: ${hp.toInt()} / ${maxHp.toInt()}")
        LinearProgressIndicator(
            progress = { animatedHp },
            color = Color.Red,
            trackColor = Color.DarkGray,
            modifier = Modifier.fillMaxWidth(0.2f)
        )

        Text("Mana: ${mana.toInt()} / ${maxMana.toInt()}")
        LinearProgressIndicator(
            progress = { animatedMana },
            color = Color.Blue,
            trackColor = Color.DarkGray,
            modifier = Modifier.fillMaxWidth(0.2f)
        )
    }
}
