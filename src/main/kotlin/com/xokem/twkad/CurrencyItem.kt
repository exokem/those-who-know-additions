package com.xokem.twkad

enum class CurrencyItem(val material: String, val amount: Int)
{
    Spur("tconstruct:molten_copper", 10),
    Bevel("tconstruct:molten_zinc", 10),
    Sprocket("tconstruct:molten_iron", 15),
    Cog("tconstruct:molten_brass", 20),
    Crown("tconstruct:molten_crown", 25),
    Sun("tconstruct:molten_netherite", 30);

    val id = name.lowercase()
}