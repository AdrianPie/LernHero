package com.lernhero.game

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.lernhero.shared.Surface

@Composable
fun GameScreen (
    navigateToFightScreen: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Surface,
    ) {

        Button(
            onClick = { navigateToFightScreen() }
        ){
            Text(text = "Game Screen")
        }


    }
}