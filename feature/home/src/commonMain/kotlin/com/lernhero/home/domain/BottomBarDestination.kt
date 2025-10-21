package com.lernhero.home.domain

import com.lernhero.shared.Resources
import com.lernhero.shared.Screen
import org.jetbrains.compose.resources.DrawableResource

enum class BottomBarDestination(
    val icon : DrawableResource,
    val title: String,
    val route: Screen
){
    Home(
        icon = Resources.Icon.Home,
        title = "Home",
        route = Screen.Home

    ),
    Game(
        icon = Resources.Icon.swords,
        title = "Game",
        route = Screen.GameScreen
    ),

    Shop(
        icon = Resources.Icon.ShoppingCart,
        title = "Shop",
        route = Screen.Shop
    )
}