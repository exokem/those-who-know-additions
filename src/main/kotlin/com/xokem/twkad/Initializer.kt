package com.xokem.twkad

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem
import com.xokem.twkad.datagen.*
import com.xokem.twkad.datagen.generate
import com.xokem.twkad.model.*
import com.xokem.twkad.model.firearmCategories
import com.xokem.twkad.model.metalComponentItems
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.screens.Screen
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(XokMod.ID)
object Initializer
{
    const val ID = XokMod.ID

    init
    {
        initializeRegistries()
        bindEvents()

        Registry.register()
    }

    private fun initializeRegistries()
    {
//        registerNetworkTypes()
//        registerTags()
//        registerExtraSounds()
//
        registerTabs()
        registerContent()
//        registerItems()
//        registerBlocks()
//        registerModifiers()
//        registerRecipeTypes()
//
//        ItemEntryContainer.registerEntries()
    }

    private fun onGatherData(e: GatherDataEvent)
    {
        e.generator.addProvider(true, XokItemModelProvider(e.generator.packOutput, e.existingFileHelper))
        e.generator.addProvider(true, XokLangProvider(e.generator.packOutput, "en_us"))
        e.generator.addProvider(true, XokLangProvider(e.generator.packOutput, "en_uk"))
        e.generator.addProvider(true, XokRecipeProvider(e.generator.packOutput))
        e.generator.addProvider(true, XokFillingRecipeProvider(e.generator.packOutput))
        e.generator.addProvider(true, XokDeployingRecipeProvider(e.generator.packOutput))
        e.generator.addProvider(true, XokSequencedAssemblyRecipeProvider(e.generator.packOutput))
        e.generator.addProvider(true, XokPressingRecipeProvider(e.generator.packOutput))
        e.generator.addProvider(true, XokCuttingRecipeProvider(e.generator.packOutput))
        e.generator.addProvider(true, XokCompactingRecipeProvider(e.generator.packOutput))
        generate()
    }

    private fun onBuildCreativeTabContents(e: BuildCreativeModeTabContentsEvent)
    {
        // Determine which VoltaicTab matches the event tab
        val tabDef = XokTab.values()
            .firstOrNull { e.tab == Registry.TabIndex.resolve(it) } ?: return

        // Add items to the tab
        Registry.TabItems[tabDef].forEach {
            e.accept(it.get())
        }

        firearmCategories.entries.forEach {
            val tag = CompoundTag()
            tag.putString("category", it.key)
            tag.putInt("selectedIndex", 0)
            val stack = ItemStack(firearmSchematic.get())
            stack.tag = tag
            e.accept(stack)
        }
    }

    private fun onEvaluateItemTooltip(e: ItemTooltipEvent)
    {
        val stack = e.itemStack

        if (stack.`is`(incompleteFirearm.get()))
        {
            val nbt = resolveNBT(stack, defaultIncompleteFirearmNbt)

            val categoryName = nbt.getString("category")
            val index = nbt.getInt("selectedIndex")

            if (categoryName === "")
            {
                return
            }

            val category = firearmCategories[categoryName] ?: return
            val entry = category.entries[index]

            e.toolTip.add(Component.literal(entry.name).withStyle(ChatFormatting.GRAY))
        }
        else if (stack.`is`(firearmSchematic.get()))
        {
            val nbt = resolveNBT(stack, defaultFirearmSchematicNbt)

            val categoryName = nbt.getString("category")

            if (categoryName == "")
            {
                return
            }

            val index = nbt.getInt("selectedIndex")

            val category = firearmCategories[categoryName] ?: return
            val entry = category.entries[index]

            val current = entry.name

            e.toolTip.add(Component.literal(entry.name).withStyle(ChatFormatting.GRAY))

            if (Screen.hasShiftDown())
            {
                e.toolTip.add(
                    Component
                        .literal("Hold [").withStyle(ChatFormatting.DARK_GRAY)
                        .append(
                            Component.literal("Shift").withStyle(ChatFormatting.WHITE)
                        )
                        .append(
                            Component.literal("] to view configurations").withStyle(ChatFormatting.DARK_GRAY)
                        )
                )

                category.entries.forEach {
                    e.toolTip.add(
                        if (it.name == current)
                            Component.literal("> ${it.name}").withStyle(ChatFormatting.GRAY)
                        else
                            Component.literal("> ${it.name}").withStyle(ChatFormatting.DARK_GRAY)
                    )
                }
            }

            else
            {
                e.toolTip.add(
                    Component
                        .literal("Hold [").withStyle(ChatFormatting.DARK_GRAY)
                        .append(
                            Component.literal("Shift").withStyle(ChatFormatting.GRAY)
                        )
                        .append(
                            Component.literal("] to view configurations").withStyle(ChatFormatting.DARK_GRAY)
                        )
                )
            }
        }
    }

    private fun registerContent()
    {
        CurrencyItem.values().forEach {
            indexItem(XokTab.Items, it.blankItemHolder)
        }

        CurrencyItem.values().forEach {
            indexItem(XokTab.Items, it.formItemHolder)
        }

        metalComponentItems.forEach {
            indexItem(XokTab.Items, it)
        }

        firearmComponentItems.forEach {
            indexItem(XokTab.Items, it)
        }

        indexItem(XokTab.Items, blazingGunpowder)

        incompleteFirearm.isPresent
        incompleteFirearmComponent.isPresent
        firearmSchematic.isPresent
        incompleteGunmetalIngot.isPresent
        incompleteGunmetalDoubleSheet.isPresent

        firearmComponents["Cartridge"]!!.variants?.forEach {
            registerItem("incomplete_${it.key.lowercase()}_cartridge") {
                object : SequencedAssemblyItem(Item.Properties().stacksTo(1))
                {
                    override fun getName(pStack: ItemStack): Component
                    {
                        return Component.literal("Incomplete ${it.key} Cartridge")
                    }
                }
            }
        }

        registerItem("incomplete_shotgun_shell") {
            object : SequencedAssemblyItem(Properties().stacksTo(1))
            {
                override fun getName(pStack: ItemStack): Component
                {
                    return Component.literal("Incomplete Shotgun Shell")
                }
            }
        }

        registerItem("incomplete_firearm_mechanism") {
            object : SequencedAssemblyItem(Properties().stacksTo(1))
            {
                override fun getName(pStack: ItemStack): Component
                {
                    return Component.literal("Incomplete Firearm Mechanism")
                }
            }
        }
    }

    private fun bindEvents()
    {


//        FORGE_BUS.addListener(::onServerStarted)
//
//        MOD_BUS.addListener(::onCommonSetup)
//        MOD_BUS.addListener(::onClientSetup)
        MOD_BUS.addListener(::onBuildCreativeTabContents)
//
//        MOD_BUS.addListener(::onRegisterItemDecorations)
//        MOD_BUS.addListener(::onRegisterAdditionalModels)
//        MOD_BUS.addListener(::onRegisterEntityRenderers)
//
        MOD_BUS.addListener(::onGatherData)
//
////        FORGE_BUS.addListener(::onBlockBroken)
//        FORGE_BUS.addListener(::onEntityPlace)
////        FORGE_BUS.addListener(::onExplosion)
//
//        FORGE_BUS.addListener(::onLevelTick)
//        FORGE_BUS.addListener(::onPlayerTick)
//
        FORGE_BUS.addListener(::onEvaluateItemTooltip)
//
//        FORGE_BUS.addListener(::onRegisterCapabilities)
//        FORGE_BUS.addGenericListener(Level::class.java, ::onAttachLevelCapabilities)
//        FORGE_BUS.addGenericListener(Entity::class.java, ::onAttachPlayerCapabilities)
    }
}