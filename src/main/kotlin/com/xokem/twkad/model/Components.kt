package com.xokem.twkad.model

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem
import com.xokem.twkad.registerItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
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
        }
    }
}

internal val incompleteGunmetalIngot by lazy {
    registerItem("incomplete_gunmetal_ingot") {
        object : SequencedAssemblyItem(Properties().stacksTo(1))
        {
            override fun getName(pStack: ItemStack): Component
            {
                return Component.literal("Incomplete Gunmetal Ingot")
            }
        }
    }
}

internal val incompleteGunmetalDoubleSheet by lazy {
    registerItem("incomplete_gunmetal_double_sheet") {
        object : SequencedAssemblyItem(Properties().stacksTo(1))
        {
            override fun getName(pStack: ItemStack): Component
            {
                return Component.literal("Incomplete Gunmetal Double Sheet")
            }
        }
    }
}

val defaultFirearmSchematicNbt by lazy {
    val tag = CompoundTag()
    tag.putString("category", "pistol")
    tag.putInt("selectedIndex", 0)
    return@lazy tag
}

internal val firearmSchematic by lazy {
    registerItem("firearm_schematic") {
        object : Item(Properties().stacksTo(1))
        {
            override fun getName(stack: ItemStack): Component
            {
                val nbt = resolveNBT(stack, defaultFirearmSchematicNbt)
                val categoryName = nbt.getString("category")

                if (categoryName == "")
                {
                    return Component.literal("Firearm Schematic")
                }

                val category = firearmCategories[categoryName] ?: return Component.literal("Firearm Schematic")

                stack.tag = nbt

                return Component.literal("${category.name} Schematic")
            }

            val defaultVariantNbt by lazy {
                val tag = CompoundTag()
                tag.putInt("selectedIndex", 0)
                return@lazy tag
            }

            fun cycleVariant(stack: ItemStack, variants: List<Any>, increment: Boolean): Int
            {
                val nbt = resolveNBT(stack, defaultVariantNbt)
                var index = nbt.getInt("selectedIndex")

                if (increment)
                    index += 1
                else
                    index -= 1

                if (index < 0)
                    index = variants.size - 1
                else if (variants.size <= index)
                    index = 0

                nbt.putInt("selectedIndex", index)

                stack.tag = nbt

                return index
            }

            override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack>
            {
                if (level.isClientSide())
                    return InteractionResultHolder.fail(player.getItemInHand(hand))

                val stack = player.getItemInHand(hand)

                val nbt = resolveNBT(stack, defaultFirearmSchematicNbt)
                val categoryName = nbt.getString("category")

                if (categoryName == "")
                {
                    return InteractionResultHolder.fail(stack)
                }

                val index = nbt.getInt("selectedIndex")

                val category = firearmCategories[categoryName] ?: return InteractionResultHolder.fail(stack)

                val variants = category.entries

                stack.tag = nbt

                val nextIndex = cycleVariant(stack, variants, !player.isCrouching)

                player.displayClientMessage(Component.literal("${variants[nextIndex].name} Schematic"), true)

                return InteractionResultHolder.fail(stack)
            }
        }
    }
}
