package com.lernhero.game.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lernhero.shared.BorderIdle
import com.lernhero.shared.CategoryBlue
import com.lernhero.shared.CategoryGreen
import com.lernhero.shared.CategoryPurple
import com.lernhero.shared.CategoryRed
import com.lernhero.shared.CategoryYellow
import com.lernhero.shared.FontSecond
import com.lernhero.shared.FontSize
import com.lernhero.shared.SurfaceDarker
import com.lernhero.shared.SurfaceSecondary
import com.lernhero.shared.TextPrimary
import com.lernhero.shared.TextWhite
import com.lernhero.shared.White

@Composable
fun GameMap(
    map: GameMap,
    state: GameMapState,
    modifier: Modifier = Modifier,
    connectorHeight: Dp = 64.dp,
    levelSpacing: Dp = 40.dp,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(levelSpacing),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        map.levels.forEachIndexed { index, level ->
            MapLevel(
                level = level,
                nextLevel = map.levels.getOrNull(index + 1),
                state = state,
                connectorHeight = connectorHeight,
            )
        }
    }
}

@Composable
private fun MapLevel(
    level: GameMapLevel,
    nextLevel: GameMapLevel?,
    state: GameMapState,
    connectorHeight: Dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (level.nodes.size == 1) Arrangement.Center else Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            level.nodes.forEach { node ->
                MapNode(
                    node = node,
                    state = state,
                    onClick = { state.selectNode(node.id) },
                )
            }
        }
        if (nextLevel != null && level.nodes.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            MapConnector(
                fromNodes = level.nodes,
                toNodes = nextLevel.nodes,
                state = state,
                height = connectorHeight,
            )
        }
    }
}

@Composable
private fun MapConnector(
    fromNodes: List<GameMapNode>,
    toNodes: List<GameMapNode>,
    state: GameMapState,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    if (fromNodes.isEmpty() || toNodes.isEmpty()) return

    val density = LocalDensity.current
    val highlightStrokeWidth = with(density) { 4.dp.toPx() }
    val idleStrokeWidth = with(density) { 2.dp.toPx() }
    val idleColor = BorderIdle.copy(alpha = 0.45f)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
    ) {
        val width = size.width
        val startCount = fromNodes.size
        val endCount = toNodes.size
        val startPositions = fromNodes.indices.associateWith { index ->
            width * ((index + 1f) / (startCount + 1f))
        }
        val endPositions = toNodes.indices.associateWith { index ->
            width * ((index + 1f) / (endCount + 1f))
        }
        fromNodes.forEachIndexed { startIndex, startNode ->
            val startX = startPositions[startIndex] ?: return@forEachIndexed
            startNode.nextNodeIds.forEach { targetId ->
                val targetIndex = toNodes.indexOfFirst { it.id == targetId }
                if (targetIndex == -1) return@forEach
                val endX = endPositions[targetIndex] ?: return@forEach
                val fromActive = startNode.id in state.visitedNodes ||
                    state.currentNodeId == startNode.id ||
                    startNode.id in state.availableNodeIds
                val toActive = targetId in state.visitedNodes || targetId in state.availableNodeIds
                val isHighlighted = fromActive && toActive
                val strokeWidth = if (isHighlighted) highlightStrokeWidth else idleStrokeWidth
                val path = Path().apply {
                    moveTo(startX, 0f)
                    val controlY = size.height * 0.55f
                    cubicTo(
                        startX,
                        controlY,
                        endX,
                        controlY,
                        endX,
                        size.height,
                    )
                }
                drawPath(
                    path = path,
                    color = if (isHighlighted) SurfaceSecondary else idleColor,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )
            }
        }
    }
}

private enum class MapNodeDisplayState { Current, Completed, Available, Locked }

@Composable
private fun MapNode(
    node: GameMapNode,
    state: GameMapState,
    onClick: () -> Unit,
) {
    val displayState = when {
        state.currentNodeId == node.id -> MapNodeDisplayState.Current
        state.visitedNodes.contains(node.id) -> MapNodeDisplayState.Completed
        state.availableNodeIds.contains(node.id) -> MapNodeDisplayState.Available
        else -> MapNodeDisplayState.Locked
    }
    val colors = mapNodeColors(displayState, node.type)
    val isClickable = displayState == MapNodeDisplayState.Current || displayState == MapNodeDisplayState.Available

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(colors.background)
                .border(colors.borderWidth, colors.borderColor, CircleShape)
                .let { base ->
                    if (isClickable) base.clickable(onClick = onClick) else base
                }
                .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colors.iconBackground)
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = node.type.icon,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = node.title,
            fontFamily = FontSecond(),
            fontSize = FontSize.EXTRA_REGULAR,
            color = colors.label,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
        )
    }
}

private data class MapNodeColors(
    val background: Color,
    val iconBackground: Color,
    val label: Color,
    val borderColor: Color,
    val borderWidth: Dp,
)

private fun mapNodeColors(state: MapNodeDisplayState, type: GameMapNodeType): MapNodeColors {
    val typeColor = nodeTypeColor(type)
    return when (state) {
        MapNodeDisplayState.Current -> MapNodeColors(
            background = SurfaceSecondary,
            iconBackground = typeColor,
            label = TextWhite,
            borderColor = typeColor,
            borderWidth = 3.dp,
        )
        MapNodeDisplayState.Completed -> MapNodeColors(
            background = SurfaceSecondary,
            iconBackground = typeColor,
            label = TextWhite,
            borderColor = typeColor.copy(alpha = 0.6f),
            borderWidth = 2.dp,
        )
        MapNodeDisplayState.Available -> MapNodeColors(
            background = White,
            iconBackground = typeColor,
            label = TextPrimary,
            borderColor = SurfaceSecondary,
            borderWidth = 2.dp,
        )
        MapNodeDisplayState.Locked -> MapNodeColors(
            background = SurfaceDarker,
            iconBackground = typeColor.copy(alpha = 0.4f),
            label = TextPrimary.copy(alpha = 0.5f),
            borderColor = SurfaceDarker,
            borderWidth = 1.dp,
        )
    }
}

private fun nodeTypeColor(type: GameMapNodeType): Color = when (type) {
    GameMapNodeType.Start -> CategoryBlue
    GameMapNodeType.Fight -> CategoryRed
    GameMapNodeType.Elite -> CategoryPurple
    GameMapNodeType.Event -> CategoryYellow
    GameMapNodeType.Shop -> CategoryBlue
    GameMapNodeType.Rest -> CategoryGreen
    GameMapNodeType.Treasure -> CategoryYellow
    GameMapNodeType.Boss -> CategoryPurple
}
