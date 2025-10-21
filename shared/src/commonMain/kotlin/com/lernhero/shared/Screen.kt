package com.lernhero.shared

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Auth : Screen()
    @Serializable
    data object HomeGraph : Screen()
    @Serializable
    data object Home: Screen()
    @Serializable
    data object FightScreen : Screen()
    @Serializable
    data object GameScreen : Screen()
    @Serializable
    data object Shop : Screen()
}