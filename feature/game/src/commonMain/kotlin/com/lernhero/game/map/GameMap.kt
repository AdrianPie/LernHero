package com.lernhero.game.map

/**
 * Represents the branching structure of a run on the world map.
 */
data class GameMap(
    val levels: List<GameMapLevel>,
) {
    private val nodeLookup: Map<String, GameMapNode>
    private val levelLookup: Map<String, Int>

    init {
        require(levels.isNotEmpty()) { "Game map must contain at least one level." }
        val ids = mutableSetOf<String>()
        val levelMap = mutableMapOf<String, Int>()
        levels.forEachIndexed { levelIndex, level ->
            require(level.nodes.isNotEmpty()) { "Level $levelIndex must contain at least one node." }
            level.nodes.forEach { node ->
                require(ids.add(node.id)) { "Duplicate node id \"${node.id}\" detected." }
                levelMap[node.id] = levelIndex
            }
        }
        levelLookup = levelMap
        nodeLookup = levels.flatMap { it.nodes }.associateBy { it.id }

        levels.forEachIndexed { levelIndex, level ->
            level.nodes.forEach { node ->
                node.nextNodeIds.forEach { targetId ->
                    val targetLevel = levelLookup[targetId]
                        ?: error("Node ${node.id} references unknown node $targetId.")
                    require(targetLevel > levelIndex) {
                        "Node ${node.id} can only connect to nodes in a later level."
                    }
                }
            }
        }
    }

    fun findNode(id: String): GameMapNode? = nodeLookup[id]

    fun levelOf(id: String): Int? = levelLookup[id]
}

data class GameMapLevel(
    val nodes: List<GameMapNode>,
)

data class GameMapNode(
    val id: String,
    val type: GameMapNodeType,
    val title: String = type.displayName,
    val nextNodeIds: List<String>,
)

enum class GameMapNodeType(
    val displayName: String,
    val icon: String,
) {
    Start(displayName = "Start", icon = "★"),
    Fight(displayName = "Walka", icon = "⚔"),
    Elite(displayName = "Elita", icon = "🔥"),
    Event(displayName = "Wydarzenie", icon = "?"),
    Shop(displayName = "Sklep", icon = "🛒"),
    Rest(displayName = "Odpoczynek", icon = "⛺"),
    Treasure(displayName = "Skarb", icon = "💰"),
    Boss(displayName = "Boss", icon = "👑"),
}

val DefaultGameMap = GameMap(
    levels = listOf(
        GameMapLevel(
            nodes = listOf(
                GameMapNode(
                    id = "start",
                    type = GameMapNodeType.Start,
                    nextNodeIds = listOf("fight_1", "event_1", "shop_1"),
                ),
            ),
        ),
        GameMapLevel(
            nodes = listOf(
                GameMapNode(
                    id = "fight_1",
                    type = GameMapNodeType.Fight,
                    nextNodeIds = listOf("event_2", "rest_1"),
                ),
                GameMapNode(
                    id = "event_1",
                    type = GameMapNodeType.Event,
                    nextNodeIds = listOf("fight_2", "treasure_1"),
                ),
                GameMapNode(
                    id = "shop_1",
                    type = GameMapNodeType.Shop,
                    nextNodeIds = listOf("fight_2", "rest_1"),
                ),
            ),
        ),
        GameMapLevel(
            nodes = listOf(
                GameMapNode(
                    id = "event_2",
                    type = GameMapNodeType.Event,
                    nextNodeIds = listOf("elite_1"),
                ),
                GameMapNode(
                    id = "rest_1",
                    type = GameMapNodeType.Rest,
                    nextNodeIds = listOf("fight_3"),
                ),
                GameMapNode(
                    id = "fight_2",
                    type = GameMapNodeType.Fight,
                    nextNodeIds = listOf("elite_1", "fight_3"),
                ),
                GameMapNode(
                    id = "treasure_1",
                    type = GameMapNodeType.Treasure,
                    nextNodeIds = listOf("fight_3"),
                ),
            ),
        ),
        GameMapLevel(
            nodes = listOf(
                GameMapNode(
                    id = "elite_1",
                    type = GameMapNodeType.Elite,
                    nextNodeIds = listOf("boss"),
                ),
                GameMapNode(
                    id = "fight_3",
                    type = GameMapNodeType.Fight,
                    nextNodeIds = listOf("boss"),
                ),
            ),
        ),
        GameMapLevel(
            nodes = listOf(
                GameMapNode(
                    id = "boss",
                    type = GameMapNodeType.Boss,
                    nextNodeIds = emptyList(),
                ),
            ),
        ),
    ),
)
