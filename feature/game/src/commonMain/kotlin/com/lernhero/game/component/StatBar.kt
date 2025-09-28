package com.lernhero.game.component

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomStatBar(
    current: Float,
    max: Float,
    progress: Float,          // 0f..1f
    color: Color,             // kolor wypełnienia (np. HP = czerwony, Mana = niebieski)
    backgroundColor: Color,   // kolor tracka
    height: Dp = 16.dp,
    modifier: Modifier = Modifier
) {
    // Animacja płynnego przesuwania
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(600, easing = LinearOutSlowInEasing),
        label = "statBarAnim"
    )

    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(8.dp)) // zaokrąglenia
            .background(backgroundColor)    // tło
            .border(2.dp, Color.Black, RoundedCornerShape(8.dp)) // ramka
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress) // szerokość zależy od progressu
                .fillMaxHeight()
                .background(
                    brush = Brush.horizontalGradient( // fajny efekt gradientu
                        listOf(color, color.copy(alpha = 0.7f))
                    )
                )
        )

        // Tekst na środku
        Text(
            text = "$current / $max",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
    }

