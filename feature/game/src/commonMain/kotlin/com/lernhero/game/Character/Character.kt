package com.lernhero.game.Character

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lernhero.shared.Resources
import com.stevdza_san.sprite.component.SpriteView
import com.stevdza_san.sprite.domain.SpriteSheet
import com.stevdza_san.sprite.domain.SpriteSpec
import com.stevdza_san.sprite.domain.rememberSpriteState
import com.stevdza_san.sprite.util.getScreenWidth
import org.jetbrains.compose.resources.DrawableResource

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
            AnimatedCharacter(sprite = sprite)
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
        }

    }
}

@Composable
fun AnimatedCharacter(
    sprite : DrawableResource
){

    val screenWidth = getScreenWidth()
    val spriteState = rememberSpriteState(
        totalFrames = 9,
        framesPerRow = 3,
        animationSpeed = 100
    )
    val animationRunning by spriteState.isRunning.collectAsState()
    DisposableEffect(Unit) {
        onDispose {
            spriteState.stop()
            spriteState.cleanup()
        }
    }

    LaunchedEffect(Unit) {
        spriteState.start()
    }
    SpriteView(
        spriteState = spriteState,
        spriteSpec = SpriteSpec(
            screenWidth = screenWidth.value,
            default = SpriteSheet(
                frameWidth = 587,
                frameHeight = 707,
                image = sprite
            )
        )
    )

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
