package com.lernhero.game.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class GameMapState internal constructor(
    internal val map: GameMap,
    startNodeId: String?,
    onNodeSelected: (GameMapNode) -> Unit,
) {
    private var onNodeSelected: (GameMapNode) -> Unit = onNodeSelected

    private val visitedNodeIds = mutableStateListOf<String>()

    var currentNodeId by mutableStateOf<String?>(null)
        private set

    var availableNodeIds by mutableStateOf(map.levels.firstOrNull()?.nodes?.map { it.id }?.toSet().orEmpty())
        private set

    val visitedNodes: List<String>
        get() = visitedNodeIds

    init {
        if (startNodeId != null) {
            selectInternal(startNodeId, notify = false)
        }
    }

    fun selectNode(nodeId: String) {
        selectInternal(nodeId, notify = true)
    }

    fun reset() {
        visitedNodeIds.clear()
        currentNodeId = null
        availableNodeIds = map.levels.firstOrNull()?.nodes?.map { it.id }?.toSet().orEmpty()
    }

    internal fun updateOnNodeSelected(listener: (GameMapNode) -> Unit) {
        onNodeSelected = listener
    }

    internal fun forceSelect(nodeId: String?) {
        reset()
        if (nodeId != null) {
            selectInternal(nodeId, notify = false)
        }
    }

    private fun selectInternal(nodeId: String, notify: Boolean) {
        val node = map.findNode(nodeId) ?: return
        val isCurrent = currentNodeId == nodeId
        val canSelect = availableNodeIds.isEmpty() || nodeId in availableNodeIds || isCurrent
        if (!canSelect) return

        currentNodeId = nodeId
        if (!visitedNodeIds.contains(nodeId)) {
            visitedNodeIds.add(nodeId)
        }
        availableNodeIds = node.nextNodeIds.filter { map.findNode(it) != null }.toSet()
        if (notify) {
            onNodeSelected(node)
        }
    }
}

@Composable
fun rememberGameMapState(
    gameMap: GameMap,
    startNodeId: String? = null,
    onNodeSelected: (GameMapNode) -> Unit = {},
): GameMapState {
    val state = remember(gameMap) {
        GameMapState(
            map = gameMap,
            startNodeId = startNodeId,
            onNodeSelected = onNodeSelected,
        )
    }

    SideEffect {
        state.updateOnNodeSelected(onNodeSelected)
    }

    LaunchedEffect(gameMap, startNodeId) {
        state.forceSelect(startNodeId)
    }

    return state
}
