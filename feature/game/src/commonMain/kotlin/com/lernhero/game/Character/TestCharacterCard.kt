package com.lernhero.game.Character

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes.Companion.Pentagon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lernhero.shared.Resources
import com.lernhero.shared.Yellowish
import org.jetbrains.compose.resources.painterResource

@Composable
fun TestCharacterCard() {
    Box() {
        // Kontener testowy
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable

fun CharacterCard(
 character: CharacterUiModel
) {
    // Rozmiar dopasowany do ramki (jest nieco większa niż avatar)
    Box(
        modifier = Modifier.size(250.dp),
        contentAlignment = Alignment.Center // To kluczowe: wszystko w środku będzie wycentrowane
    ) {
        // 1. WARSTWA DOLNA: Avatar postaci
        Box(
            modifier = Modifier
                .size(220.dp) // Mniejszy od ramki, by pasował do otworu
                .clip(Pentagon.toShape()) // Twoja maska pentagonu
                .border(4.dp, Yellowish)
        ) {
            Image(
                painter = painterResource(Resources.Image.battleMageAvatarGame),
                contentDescription = null,
                contentScale = ContentScale.Fit, // Crop zazwyczaj wygląda lepiej przy portretach
                modifier = Modifier.fillMaxSize()
            )
        }

        // 2. WARSTWA GÓRNA: Ramka smoka
        // Ponieważ ten Image jest w tym samym Boxie, nałoży się na wierzch postaci
        Image(
            painter = painterResource(Resources.Image.boundsDruidArmor),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize() // Wypełnia całe 250.dp
        )
        StatisticBadge(
            value = character.attack,
            label = "ATK",
            badgeColor = Color(0xFFC62828), // Ciemnoczerwony
            modifier = Modifier
                .align(Alignment.BottomStart) // Wstępne pozycjonowanie w rogu
                // Precyzyjne przesunięcie, aby pasowało do grafiki ramki
                .offset(x = 16.dp, y = (-16).dp)
        )

        // HP - Prawy dolny róg
        StatisticBadge(
            value = character.hp,
            label = "HP",
            badgeColor = Color(0xFF2E7D32), // Ciemnozielony
            modifier = Modifier
                .align(Alignment.BottomEnd) // Wstępne pozycjonowanie w rogu
                // Precyzyjne przesunięcie
                .offset(x = (-16).dp, y = (-16).dp)
        )
    }
}
@Composable
fun StatisticBadge(
    value: Int,
    label: String, // opcjonalnie, np. "ATK"
    badgeColor: Color, // Kolor tła/ramki, np. czerwony dla HP, mieczowy dla ATK
    modifier: Modifier = Modifier
) {
    // Kontener dla statystyki, domyślnie koło
    Box(
        modifier = modifier
            .size(48.dp) // Rozmiar plakietki
            // Dodajemy cień dla efektu głębi
            .shadow(4.dp, CircleShape)
            // Tło w kształcie koła (wymienna ramka)
            .background(badgeColor, CircleShape)
            // Opcjonalna, cienka krawędź
            .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
            .padding(4.dp), // Wewnętrzny padding
        contentAlignment = Alignment.Center // Centrowanie tekstu
    ) {
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White, // Liczba na samej górze
                textAlign = TextAlign.Center
            )
    }
}

//@Preview(showBackground = false)
//@Composable
//fun TestCharacterCardPreview() {
//    CharacterCard(attack = 2, health = 3)
//}
