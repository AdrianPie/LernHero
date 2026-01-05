package com.lernhero.game.Character

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lernhero.game.GameViewModel
import com.lernhero.shared.SpriteAsset
import com.stevdza_san.sprite.component.SpriteView
import com.stevdza_san.sprite.domain.SpriteSheet
import com.stevdza_san.sprite.domain.SpriteSpec
import com.stevdza_san.sprite.domain.rememberSpriteState
import com.stevdza_san.sprite.util.getScreenWidth
import org.koin.compose.viewmodel.koinViewModel


/*───────────────────────────────────────────────────────────────
    MAIN CHARACTER COMPOSABLE
───────────────────────────────────────────────────────────────*/
@Composable
fun Character(
    hp: Float = 85f,
    mana: Float = 67f,
    maxHp: Float = 100f,
    maxMana: Float = 100f,
    spriteCharacter: SpriteAsset,
    spriteEffect: SpriteAsset,
    targetScale: Float = 1f,
    onClickPlus: () -> Unit = {},
    onClickMinus: () -> Unit = {},
    effect: () -> Unit = {},
) {
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(600, 100, LinearOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .requiredWidth(spriteCharacter.localWidth)
            .wrapContentHeight()
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // Sprite + feet stats underneath
            Box(
                modifier = Modifier
                    .requiredWidth(spriteCharacter.localWidth)
                    .requiredHeight(spriteCharacter.localWidth * spriteCharacter.ratio),
                contentAlignment = Alignment.Center
            ) {
                AnimatedCharacter(
                    spriteCharacter = spriteCharacter,
                    spriteEffect = spriteEffect
                )

                FeetStatsRow(
                    hp = hp,
                    maxHp = maxHp,
                    mana = mana,
                    maxMana = maxMana,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (10).dp)   // how much circles overlap sprite feet
                        .zIndex(10f)            // guarantee on top
                )
            }
        }
    }
}


/*───────────────────────────────────────────────────────────────
    FEET HP & MANA CIRCLES (LEFT = HP, RIGHT = MANA)
───────────────────────────────────────────────────────────────*/
@Composable
fun FeetStatsRow(
    modifier: Modifier = Modifier,
    hp: Float,
    maxHp: Float,
    mana: Float,
    maxMana: Float,
    size: Dp = 34.dp,
    spacing: Dp = 30.dp,
    stroke: Dp = 5.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // HP circle
        CircularProgressIndicator(
            progress = { (hp / maxHp).coerceIn(0f, 1f) }, // LAMBDA as requested
            modifier = Modifier.size(size),
            color = Color.Red,
            trackColor = Color.DarkGray,
            strokeWidth = stroke
        )

        Spacer(Modifier.width(spacing))

        // MANA circle
        CircularProgressIndicator(
            progress = { (mana / maxMana).coerceIn(0f, 1f) }, // LAMBDA
            modifier = Modifier.size(size),
            color = Color.Blue,
            trackColor = Color.DarkGray,
            strokeWidth = stroke
        )
    }
}


/*───────────────────────────────────────────────────────────────
    THE ORIGINAL ANIMATED CHARACTER (UNTOUCHED)
───────────────────────────────────────────────────────────────*/
@Composable
fun AnimatedCharacter(
    spriteCharacter: SpriteAsset,
    spriteEffect: SpriteAsset,
) {
    val viewModel = koinViewModel<GameViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val density = LocalDensity.current
    val screenWidth = getScreenWidth()

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
    ) {

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

        // effect animation
        LaunchedEffect(uiState.effectState) {
            if (uiState.effectState) spriteEffectState.start()
        }

        AnimatedVisibility(visible = uiState.effectState) {
            LaunchedEffect(currentEffectFrame) {
                if (currentEffectFrame == spriteEffect.totalFrames - 1) {
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
