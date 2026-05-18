package com.lernhero.home

import com.lernhero.game.FightScreen
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lernhero.game.GameScreen
import com.lernhero.home.component.NavBottomBar
import com.lernhero.home.domain.BottomBarDestination
import com.lernhero.shared.FontFirst
import com.lernhero.shared.FontSize
import com.lernhero.shared.IconPrimary
import com.lernhero.shared.Resources
import com.lernhero.shared.Screen
import com.lernhero.shared.Surface
import com.lernhero.shared.TextPrimary
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeGraphScreen() {

    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState()

    val selectedDestination by remember {
        derivedStateOf {
            when {
                currentRoute.value.isOnRoute<Screen.Home>() -> BottomBarDestination.Home
                currentRoute.value.isOnRoute<Screen.GameScreen>() -> BottomBarDestination.Game
                currentRoute.value.isOnRoute<Screen.Shop>() -> BottomBarDestination.Shop
                else -> BottomBarDestination.Home
            }
        }
    }
    val showBars by remember {
        derivedStateOf {
            !currentRoute.value.isOnRoute<Screen.FightScreen>()
        }
    }
    Scaffold(
        containerColor = Surface,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = if (showBars) {
            {
                CenterAlignedTopAppBar(
                    title = {
                        AnimatedContent(
                            targetState = selectedDestination
                        ){destination ->
                            Text(
                                text = destination.title,
                                fontFamily = FontFirst(),
                                fontSize = FontSize.LARGE,
                                color = TextPrimary
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(Resources.Icon.Menu),
                                contentDescription = "Menu Icon"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Surface,
                        scrolledContainerColor = Surface,
                        navigationIconContentColor = IconPrimary,
                        titleContentColor = TextPrimary,
                        actionIconContentColor = IconPrimary

                    )
                )
            }
        } else {
            { }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (showBars) {
                        Modifier.padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding()
                        )
                    } else {
                        Modifier
                    }
                )
        ) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = Screen.Home
            ) {
                composable<Screen.Home> {  }
                composable<Screen.GameScreen> { GameScreen(
                    navigateToFightScreen = {
                        navController.navigate(Screen.FightScreen)
                    }
                ) }
                composable<Screen.Shop> {  }
                composable<Screen.FightScreen> { FightScreen() }

            }
            if (showBars) {
                NavBottomBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    selected = selectedDestination,
                    onSelect = {destination ->
                        navController.navigate(destination.route) {
                            launchSingleTop = true
                            popUpTo<Screen.Home> {
                                saveState = true
                                inclusive = false
                            }
                            restoreState = true
                        }

                    }
                )
            }

        }

    }

}

private inline fun <reified T : Any> androidx.navigation.NavBackStackEntry?.isOnRoute(): Boolean {
    return this?.destination
        ?.hierarchy
        ?.any { destination -> destination.hasRoute(T::class) } == true
}
