package com.lernhero.domain.data

import com.lernhero.shared.domain.Character

object CharacterPreset {
    val Knight = Character(
        id = "knight",
        isPlayer = false,
        hp = 50,
        mana = 20,
        maxHp = 50,
        maxMana = 20,
        attack = 10,
        defense = 15,
        speed = 12
    )
    val Sorcerer = Character(
        id = "sorcerer",
        isPlayer = false,
        hp = 30,
        mana = 50,
        maxHp = 30,
        maxMana = 50,
        attack = 15,
        defense = 11,
        speed = 20
    )

}