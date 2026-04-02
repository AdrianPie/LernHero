package com.lernhero.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lernhero.game.Character.CharacterCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FightScreen() {
    val viewModel = koinViewModel<GameViewModel>()
    val state by viewModel.uiState.collectAsState()

    // Wyciągamy postacie z mapy i dzielimy na grupy
    val allCharacters = state.characters.values.toList()
    val enemies = allCharacters.filter { !it.isPlayer }
    val players = allCharacters.filter { it.isPlayer }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0B08))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- SEKCJA GÓRNA: PRZECIWNICY ---
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                enemies.forEach { enemy ->
                    CharacterCard(character = enemy)
                }
            }
        }

        // --- SEKCJA ŚRODKOWA: STATUS ---
        Text(
            text = "WALKA TRWA",
            color = Color(0xFFFFE5B1),
            style = MaterialTheme.typography.headlineSmall
        )

        // --- SEKCJA DOLNA: GRACZE ---
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                players.forEach { player ->
                    CharacterCard(character = player)
                }
            }
        }
    }
}