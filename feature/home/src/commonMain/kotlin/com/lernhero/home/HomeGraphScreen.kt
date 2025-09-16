package com.lernhero.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lernhero.GameScreen
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
            val route = currentRoute.value?.destination?.route.toString()
            when {
                route.contains(BottomBarDestination.Home.route.toString()) -> BottomBarDestination.Home
                route.contains(BottomBarDestination.Fight.route.toString()) -> BottomBarDestination.Fight
                route.contains(BottomBarDestination.Shop.route.toString()) -> BottomBarDestination.Shop
                else -> BottomBarDestination.Home
            }
        }
    }
    Scaffold(
        containerColor = Surface,
        topBar = {
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary

                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
        ) {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = Screen.Home
            ) {
                composable<Screen.Home> {  }
                composable<Screen.FightScreen> {
                    GameScreen()
                }
                composable<Screen.Shop> {  }

            }
            Spacer(modifier = Modifier.height(12.dp))
            NavBottomBar(
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