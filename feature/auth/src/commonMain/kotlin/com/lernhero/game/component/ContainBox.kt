package com.lernhero.game.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GameBox() {
    Row {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {

        }
        Box(
            modifier = Modifier
                .weight(1f)
        ) {

        }
    }

}