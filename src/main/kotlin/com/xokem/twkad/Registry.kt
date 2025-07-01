package com.xokem.twkad

import com.xokem.twkad.adt.DeferredIndex
import com.xokem.twkad.adt.DeferredListIndex
import net.minecraft.core.registries.Registries.*
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.*
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries.*
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.RegistryObject
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import java.util.function.Supplier

internal object Registry
{
    //<editor-fold desc="Registers">

    private fun <B, C : net.minecraft.core.Registry<B>> register(key: ResourceKey<C>): DeferredRegister<B> =
        DeferredRegister.create(key, XokMod.ID)

    val Items             = register(ITEM)
    val Blocks            = register(BLOCK)
//    val Sounds            = register(SOUND_EVENT)
    val Tabs              = register(CREATIVE_MODE_TAB)
//    val BlockEntities     = register(BLOCK_ENTITY_TYPE)
//    val Menus             = register(MENU)
//    val Recipes           = register(RECIPE_TYPE)
//    val RecipeSerializers = register(RECIPE_SERIALIZER)
//    val Modifiers         = register(Keys.BIOME_MODIFIER_SERIALIZERS)

    fun register()
    {
        Items.register(MOD_BUS)
        Blocks.register(MOD_BUS)
//        Sounds.register(MOD_BUS)
        Tabs.register(MOD_BUS)
//        BlockEntities.register(MOD_BUS)
//        Menus.register(MOD_BUS)
//        Recipes.register(MOD_BUS)
//        RecipeSerializers.register(MOD_BUS)
//        Modifiers.register(MOD_BUS)
    }

    //</editor-fold>

    val TabIndex = DeferredIndex<XokTab, CreativeModeTab>()
    val TabItems = DeferredListIndex<XokTab, Item>()

    //<editor-fold desc="Accessors">

    private fun <T> get(registry: IForgeRegistry<T>, identifier: String): T? =
        registry.getValue(XokMod.resource(identifier))

    fun getItem(identifier: String): Item? =
        get(ITEMS, identifier)

    fun getBlock(identifier: String): Block? =
        get(BLOCKS, identifier)

    //</editor-fold>
}

internal fun registerItem(key: String, item: Supplier<Item>): RegistryObject<Item> =
    Registry.Items.register(key, item)

internal fun registerItem(key: String, properties: Item.Properties, vararg tooltips: Component) =
    registerItem(key) {
        object : Item(properties)
        {
            override fun appendHoverText(stack: ItemStack, level: Level?, tooltip: MutableList<Component>, flag: TooltipFlag) =
                tooltips.forEach { tooltip.add(it) }
        }
    }

internal fun registerItem(key: String, vararg tooltips: Component) =
    registerItem(key, Item.Properties(), *tooltips)

internal fun registerFuelItem(key: String, fuel: Int, properties: Item.Properties = Item.Properties()) =
    registerItem(key) {
        object: Item(properties)
        {
            override fun getBurnTime(itemStack: ItemStack?, recipeType: RecipeType<*>?): Int =
                fuel
        }
    }

internal fun registerBlock(key: String, block: Supplier<Block>): Pair<RegistryObject<Block>, RegistryObject<Item>>
{
    val entry = Registry.Blocks.register(key, block)
    val item = registerItem(key) { BlockItem(entry.get(), Item.Properties())}
    return Pair(entry, item)
}

internal fun registerBlock(key: String, block: Supplier<Block>, vararg tooltips: Component): Pair<RegistryObject<Block>, RegistryObject<Item>>
{
    val entry = Registry.Blocks.register(key, block)

    val item = registerItem(key) {
        object: BlockItem(entry.get(), Properties())
        {
            override fun appendHoverText(stack: ItemStack, level: Level?, tooltip: MutableList<Component>, flag: TooltipFlag) =
                tooltips.forEach { tooltip.add(it) }
        }
    }

    return Pair(entry, item)
}

internal fun registerBlock(key: String, properties: BlockBehaviour.Properties, vararg tooltips: Component) =
    registerBlock(key, { Block(properties) }, *tooltips)

internal fun registerFuelBlock(key: String, properties: BlockBehaviour.Properties, fuelTime: Int, vararg tooltips: Component): Pair<RegistryObject<Block>, RegistryObject<Item>>
{
    val entry = Registry.Blocks.register(key) { Block(properties) }
    val item = registerItem(key) {
        object: BlockItem(entry.get(), Properties())
        {
            override fun getBurnTime(itemStack: ItemStack?, recipeType: RecipeType<*>?): Int =
                fuelTime

            override fun appendHoverText(stack: ItemStack, level: Level?, tooltip: MutableList<Component>, flag: TooltipFlag) =
                tooltips.forEach { tooltip.add(it) }
        }
    }

    return Pair(entry, item)
}

internal fun registerItem(tab: XokTab, identifier: String, vararg tooltips: Component)
{
    val item = registerItem(identifier, *tooltips)
    Registry.TabItems.add(tab, item)
}

internal fun indexItem(tab: XokTab, item: Supplier<Item>) =
    Registry.TabItems.add(tab, item)

internal fun indexItems(tab: XokTab, vararg items: RegistryObject<Item>) =
    Registry.TabItems.addAll(tab, *items.map { Supplier { it.get() } }.toTypedArray())

internal fun indexAllBlocks(tab: XokTab, vararg entries: Pair<RegistryObject<Block>, RegistryObject<Item>>) =
    indexItems(tab, *entries.map { it.second }.toTypedArray())

///**
// * NOTE: Tracks must be mono, 48khz, ogg.
// * Multiply the duration in seconds to get the tick duration.
// * Make sure to add the sound to resources/voltaic/sounds.json.
// * Make sure to add the music disc to data/minecraft/tags/items/music_discs.json.
// */
//internal fun registerRecordItem(key: String, signal: Int, durationTicks: Int): RegistryObject<Item>
//{
//    val soundKey = "music_disc.${key}"
//    val sound = Registry.Sounds.register(soundKey) { SoundEvent.createVariableRangeEvent(XokMod.resource(soundKey)) }
//
//    val itemKey = "music_disc_${key}"
//    return registerItem(itemKey) {
//        RecordItem(signal, sound, Item.Properties().rarity(Rarity.EPIC).stacksTo(1), durationTicks)
//    }
//}
