package com.lernhero

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.lernhero.game.Character.Character
import com.lernhero.game.map.DefaultGameMap
import com.lernhero.game.map.GameMap
import com.lernhero.game.map.GameMapNode
import com.lernhero.game.map.GameMapNodeType
import com.lernhero.game.map.GameMap as GameMapComposable
import com.lernhero.game.map.rememberGameMapState
import com.lernhero.shared.FontFirst
import com.lernhero.shared.FontSecond
import com.lernhero.shared.FontSize
import com.lernhero.shared.Resources
import com.lernhero.shared.Surface
import com.lernhero.shared.SurfaceLighter
import com.lernhero.shared.TextPrimary
import com.stevdza_san.sprite.component.SpriteView
import com.stevdza_san.sprite.domain.SpriteSheet
import com.stevdza_san.sprite.domain.SpriteSpec
import com.stevdza_san.sprite.domain.rememberSpriteState
import com.stevdza_san.sprite.util.getScreenWidth
import lernhero.feature.game.generated.resources.Res


@Composable
fun GameScreen(
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Surface
    ){
        var scale by remember { mutableStateOf(1f) }
        Character(
            targetScale = scale,
            sprite = Resources.Sprite.knightIdle3,
            onClickPlus = { scale += 0.1f },
            onClickMinus = { scale -= 0.1f },
        )


    }

}
