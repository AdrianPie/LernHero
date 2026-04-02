package com.lernhero.domain.preset

object CharacterPreset {
    val Knight = Character(
        id = "knight",
        isPlayer = true,
        hp = 50,
        mana = 20,
        maxHp = 50,
        maxMana = 20,
        attack = 10,

    )
    val Sorcerer = Character(
        id = "sorcerer",
        isPlayer = true,
        hp = 30,
        mana = 50,
        maxHp = 30,
        maxMana = 50,
        attack = 15,
    )

}