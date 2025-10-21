package com.lernhero.shared

import androidx.compose.ui.graphics.Color
import lernhero.shared.generated.resources.Res
import lernhero.shared.generated.resources.heart
import lernhero.shared.generated.resources.mana
import lernhero.shared.generated.resources.shopping_cart
import lernhero.shared.generated.resources.speed
import lernhero.shared.generated.resources.swords_24px

object StatsHelper {
    val colorMap = mapOf(
        "maxHp"   to Color(0xFFEF4444), // czerwony
        "maxMana" to Color(0xFF6366F1), // indigo (odróżnia się od teal/blue)
        "attack"  to Color(0xFFF97316), // pomarańcz (agresywny akcent)
        "defense" to Color(0xFF14B8A6), // teal (spokojny, „obrona”)
        "speed"   to Color(0xFF06B6D4)  // cyan (czytelniejszy niż żółty)
    )
    val iconMap = mapOf(
        "maxHp"   to Res.drawable.heart,
        "maxMana" to Res.drawable.mana,
        "attack"  to Res.drawable.swords_24px,
        "defense" to Res.drawable.shopping_cart,
        "speed"   to Res.drawable.speed
    )
}