package com.lernhero.game.Character

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lernhero.game.GameViewModel
import com.lernhero.game.component.CustomStatBar
import com.lernhero.shared.SpriteAsset
import com.stevdza_san.sprite.component.SpriteView
import com.stevdza_san.sprite.domain.SpriteSheet
import com.stevdza_san.sprite.domain.SpriteSpec
import com.stevdza_san.sprite.domain.rememberSpriteState
import com.stevdza_san.sprite.util.getScreenWidth
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Character(
    hp: Float = 85f,
    mana: Float = 67f,
    maxHp: Float = 100f,
    maxMana: Float = 100f,
    spriteCharacter : SpriteAsset,
    spriteEffect: SpriteAsset,
    targetScale : Float = 1.0f,
    onClickPlus : () -> Unit,
    onClickMinus : () -> Unit,
    onClickEffect : () -> Unit,
) {
    val  scale by animateFloatAsState(
        targetScale,
        animationSpec = tween(600,100, LinearOutSlowInEasing),
    )
    Box(
        modifier = Modifier
            .requiredWidth(spriteCharacter.localWidth)
            .wrapContentHeight()
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CharacterStats(
                modifier = Modifier.fillMaxWidth(),
                hp = hp,
                mana = mana,
                maxHp = maxHp,
                maxMana = maxMana,
            )

            Spacer(Modifier.height(6.dp))

            AnimatedCharacter(
                spriteCharacter = spriteCharacter,
                spriteEffect = spriteEffect,
            )
        }

    }
}

@Composable
fun AnimatedCharacter(
    spriteCharacter : SpriteAsset,
    spriteEffect : SpriteAsset,
){
    val viewModel = koinViewModel<GameViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val screenWidth = getScreenWidth()
    val density = LocalDensity.current

    val scaleCharacter = remember(spriteCharacter, density) {
        spriteCharacter.scaleWidthFactor(density)
    }
    val scaleEffect = remember(spriteCharacter, spriteEffect, density) {
        spriteEffect.scaleFactorRelativeTo(spriteCharacter, 0.8f, density)
    }


    val spriteCharacterState = rememberSpriteState(
        totalFrames = spriteCharacter.totalFrames,
        framesPerRow = spriteCharacter.framesPerRow,
        animationSpeed = spriteCharacter.animationSpeed
    )
    val spriteEffectState = rememberSpriteState(
        totalFrames = spriteEffect.totalFrames,
        framesPerRow = spriteEffect.framesPerRow,
        animationSpeed = spriteEffect.animationSpeed
    )
    val currentEffectFrame by spriteEffectState.currentFrame.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            spriteCharacterState.stop()
            spriteCharacterState.cleanup()
        }
    }
    
    LaunchedEffect(Unit) { spriteCharacterState.start() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(spriteCharacter.localWidth * spriteCharacter.ratio)
            .border(2.dp, Color.Red),
        contentAlignment = Alignment.Center
    ){
        SpriteView(
            modifier = Modifier.scale(scaleCharacter),
            spriteState = spriteCharacterState,
            spriteSpec = SpriteSpec(
                screenWidth = screenWidth.value,
                default = SpriteSheet(
                    frameWidth = spriteCharacter.frameWidth,
                    frameHeight = spriteCharacter.frameHeight,
                    image = spriteCharacter.drawable
            )
        )
    )
        LaunchedEffect(uiState.effectState) {
            if (uiState.effectState) spriteEffectState.start()
        }
        AnimatedVisibility(visible = uiState.effectState) {
            LaunchedEffect(currentEffectFrame) {
                if (currentEffectFrame == (spriteEffect.totalFrames - 1)) {
                    spriteEffectState.stop()
                    viewModel.changeEffectVisibility(false)
                }
            }
            SpriteView(
                modifier = Modifier.scale(scaleEffect),
                spriteState = spriteEffectState,
                spriteSpec = SpriteSpec(
                    screenWidth = screenWidth.value,
                    default = SpriteSheet(
                        frameWidth = spriteEffect.frameWidth,
                        frameHeight = spriteEffect.frameHeight,
                        image = spriteEffect.drawable
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
    Column(
            modifier = modifier.fillMaxWidth(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        CircularProgressIndicator(
        progress = { hp / maxHp },

        modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
        color = Color.Red,
        trackColor = Color.DarkGray,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { mana / maxMana },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Color.Red,
            trackColor = Color.DarkGray,
            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
        )

        }



}
