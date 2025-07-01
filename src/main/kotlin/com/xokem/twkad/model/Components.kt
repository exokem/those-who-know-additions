package com.xokem.twkad.model

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem
import com.xokem.twkad.registerItem
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.contents.LiteralContents
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.awt.event.ComponentEvent
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

internal fun resolveNBT(stack: ItemStack, defaultData: CompoundTag): CompoundTag
{
    if (stack.tag == null)
        stack.tag = defaultData.copy()

    return stack.tag!!
}

val defaultIncompleteFirearmNbt by lazy {
    val tag = CompoundTag()
    tag.putString("category", "")
    tag.putInt("selectedIndex", -1)
    return@lazy tag
}

val defaultIncompleteFirearmComponentNbt by lazy {
    val tag = CompoundTag()
    tag.putString("category", "")
    tag.putString("variant", "")
    return@lazy tag
}

internal val incompleteFirearmComponent by lazy {
    registerItem("incomplete_firearm_component") {
        object : SequencedAssemblyItem(Properties().stacksTo(1))
        {
            override fun getName(stack: ItemStack): Component
            {
                val nbt = resolveNBT(stack, defaultIncompleteFirearmComponentNbt)
                val categoryName = nbt.getString("category")

                if (categoryName == "")
                {
                    return Component.literal("Incomplete Firearm Component")
                }

                val variantName = nbt.getString("variant")

                if (variantName == "")
                {
                    return Component.literal("Incomplete Firearm Component")
                }

                val category = firearmComponents[categoryName] ?: return Component.literal("Incomplete Firearm Component")
                category.variants ?: return Component.literal("Incomplete Firearm Component")

                stack.tag = nbt

                return if (category.includeCategoryName == false)
                    Component.literal("Incomplete $variantName")
                else Component.literal("Incomplete $variantName $categoryName")
            }
        }
    }
}

internal val incompleteFirearm by lazy {
    registerItem("incomplete_firearm") {
        object : SequencedAssemblyItem(Properties().stacksTo(1))
        {
            override fun getName(stack: ItemStack): Component
            {
//                val stack = player.getItemInHand(hand)
                val nbt = resolveNBT(stack, defaultIncompleteFirearmNbt)
                val categoryName = nbt.getString("category")

                if (categoryName == "")
                {
                    return Component.literal("Incomplete Firearm")
                }

                val category = firearmCategories[categoryName] ?: return Component.literal("Incomplete Firearm")

                stack.tag = nbt

                return Component.literal("Incomplete ${category.name}")
            }

//            override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack>
//            {
//
//            }
        }
    }
}
