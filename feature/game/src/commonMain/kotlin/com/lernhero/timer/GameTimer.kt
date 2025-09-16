package com.lernhero.timer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

class GameTimerState internal constructor(
    internal val totalDurationMillis: Long,
    private val coroutineScope: CoroutineScope,
) {
    init {
        require(totalDurationMillis > 0) { "totalDurationMillis must be greater than 0." }
    }

    private var onFinished: () -> Unit = {}
    private var timerJob: Job? = null

    var isRunning by mutableStateOf(false)
        private set

    var progress by mutableStateOf(1f)
        private set

    val timeRemainingMillis: Long
        get() = (totalDurationMillis.toFloat() * progress).roundToLong()

    fun start() {
        if (isRunning) return
        if (progress <= 0f) {
            progress = 1f
        }
        isRunning = true
        launchTimer()
    }

    fun pause() {
        if (!isRunning) return
        isRunning = false
        timerJob?.cancel()
        timerJob = null
    }

    fun reset() {
        pause()
        progress = 1f
    }

    fun restart() {
        reset()
        start()
    }

    internal fun updateOnFinished(listener: () -> Unit) {
        onFinished = listener
    }

    internal fun cancel() {
        isRunning = false
        timerJob?.cancel()
        timerJob = null
    }

    private fun launchTimer() {
        timerJob?.cancel()
        val total = totalDurationMillis
        val startProgress = progress.coerceIn(0f, 1f)
        val job = coroutineScope.launch {
            var remainingMillis = (total * startProgress).roundToLong()
            if (remainingMillis <= 0L) {
                finish()
                return@launch
            }
            var lastFrameTime = withFrameNanos { it }
            while (isActive && isRunning && remainingMillis > 0L) {
                val frameTime = withFrameNanos { it }
                if (!isRunning) break
                val deltaMillis = ((frameTime - lastFrameTime) / 1_000_000L).coerceAtLeast(0L)
                lastFrameTime = frameTime
                if (deltaMillis == 0L) continue
                remainingMillis = (remainingMillis - deltaMillis).coerceAtLeast(0L)
                progress = (remainingMillis.toFloat() / total).coerceIn(0f, 1f)
            }
            if (remainingMillis <= 0L && isRunning) {
                finish()
            }
        }
        job.invokeOnCompletion { timerJob = null }
        timerJob = job
    }

    private fun finish() {
        progress = 0f
        isRunning = false
        onFinished()
    }
}

@Composable
fun rememberGameTimerState(
    totalTimeMillis: Long,
    autoStart: Boolean = true,
    onFinished: () -> Unit = {},
): GameTimerState {
    val coroutineScope = rememberCoroutineScope()
    val state = remember(totalTimeMillis) {
        GameTimerState(
            totalDurationMillis = totalTimeMillis,
            coroutineScope = coroutineScope,
        )
    }
    state.updateOnFinished(onFinished)

    DisposableEffect(state) {
        onDispose { state.cancel() }
    }

    LaunchedEffect(state, autoStart) {
        if (autoStart) {
            state.start()
        }
    }

    return state
}

@Composable
fun GameTimerLine(
    state: GameTimerState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
    burnColors: List<Color> = listOf(
        Color(0xFFFFB74D),
        Color(0xFFFF7043),
        Color(0xFFD84315),
    ),
    height: Dp = 8.dp,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = state.progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 120, easing = LinearEasing),
        label = "gameTimerProgress",
    )

    Box(
        modifier = modifier
            .height(height)
            .clip(shape = RoundedCornerShape(height / 2))
            .background(backgroundColor),
    ) {
        if (animatedProgress > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = if (burnColors.isNotEmpty()) burnColors else listOf(MaterialTheme.colorScheme.primary),
                        ),
                    ),
            )
        }
    }
}
