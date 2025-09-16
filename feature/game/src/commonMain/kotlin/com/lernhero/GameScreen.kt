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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.lernhero.game.map.DefaultGameMap
import com.lernhero.game.map.GameMap
import com.lernhero.game.map.GameMapNode
import com.lernhero.game.map.GameMapNodeType
import com.lernhero.game.map.GameMap as GameMapComposable
import com.lernhero.game.map.rememberGameMapState
import com.lernhero.shared.FontFirst
import com.lernhero.shared.FontSecond
import com.lernhero.shared.FontSize
import com.lernhero.shared.Surface
import com.lernhero.shared.SurfaceLighter
import com.lernhero.shared.TextPrimary


@Composable
fun GameScreen(
    modifier: Modifier = Modifier,

    gameMap: GameMap = DefaultGameMap,
    startNodeId: String? = null,
    onNodeSelected: (GameMapNode) -> Unit = {},
) {
    val firstLevel = gameMap.levels.firstOrNull()
    val defaultStartNodeId = when {
        firstLevel == null -> null
        firstLevel.nodes.any { it.type == GameMapNodeType.Start } ->
            firstLevel.nodes.firstOrNull { it.type == GameMapNodeType.Start }?.id
        firstLevel.nodes.size == 1 -> firstLevel.nodes.firstOrNull()?.id
        else -> null
    }
    val resolvedStartNodeId = startNodeId ?: defaultStartNodeId
    val mapState = rememberGameMapState(
        gameMap = gameMap,
        startNodeId = resolvedStartNodeId,
        onNodeSelected = onNodeSelected,



    )

    Column(
        modifier = modifier
            .fillMaxSize()

            .background(Surface)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Wybierz swoją trasę",
            fontFamily = FontFirst(),
            fontSize = FontSize.EXTRA_MEDIUM,
            color = TextPrimary,
        )

        val upcomingTitles = mapState.availableNodeIds
            .mapNotNull { gameMap.findNode(it) }
            .joinToString(separator = ", ") { it.title }

        Text(
            text = if (upcomingTitles.isNotEmpty()) {
                "Dostępne odnogi: $upcomingTitles"
            } else {
                "Dotarłeś do końca tej ścieżki."
            },
            fontFamily = FontSecond(),
            fontSize = FontSize.REGULAR,
            color = TextPrimary.copy(alpha = 0.7f),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(32.dp))
                .background(SurfaceLighter)
                .padding(vertical = 24.dp, horizontal = 16.dp),
        ) {
            GameMapComposable(
                map = gameMap,
                state = mapState,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        val currentNode = mapState.currentNodeId?.let { gameMap.findNode(it) }
        Text(
            text = currentNode?.let { node ->
                if (node.type == GameMapNodeType.Boss) {
                    "Przed Tobą finałowe starcie!"
                } else {
                    "Aktualny punkt: ${node.title}"
                }
            } ?: "Wybierz miejsce, do którego chcesz się udać.",
            fontFamily = FontSecond(),
            fontSize = FontSize.EXTRA_REGULAR,
            color = TextPrimary,
        )        

    }
}
