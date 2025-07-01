package com.xokem.twkad.model

import com.xokem.twkad.registerItem
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import net.minecraft.world.item.Item
import java.io.InputStreamReader

@Serializable
data class ComponentData(
    val metal: Map<String, List<String>>,
    val firearm: Map<String, FirearmComponent>
)

@Serializable
data class FirearmComponent(
    val variants: Map<String, Boolean>? = null,
    val maxStack: Int? = null,
    val includeCategoryName: Boolean? = null
)

private val data by lazy {
    val classLoader = Thread.currentThread().contextClassLoader
    val inputStream = classLoader.getResourceAsStream("components.json")
        ?: error("components.json not found in resources")

    val jsonText = InputStreamReader(inputStream).readText()

    return@lazy Json.decodeFromString<ComponentData>(jsonText)
}

internal val firearmComponents get() = data.firearm

internal val metalComponents get() = data.metal

internal val firearmComponentItems by lazy {
    firearmComponents.entries.flatMap {entry ->
        val component = entry.key
        val data = entry.value
        val props = Item.Properties().stacksTo(data.maxStack ?: 64)
        return@flatMap data.variants?.keys?.map { variant ->
            val name = if (data.includeCategoryName == false) variant.lowercase().replace(" ", "_") else "$variant $component".lowercase().replace(" ", "_")
            println(name)
            return@map registerItem(name, props)
        } ?: listOf()
    }
}

internal val metalComponentItems by lazy {
    return@lazy metalComponents.entries.flatMap {
        it.value.map { metal ->
            val name = "${metal}_${it.key}".lowercase().replace(" ", "_")
            println(name)
            return@map registerItem(name)
        }
    }
}

internal val blazingGunpowder by lazy {
    registerItem("blazing_gunpowder")
}
