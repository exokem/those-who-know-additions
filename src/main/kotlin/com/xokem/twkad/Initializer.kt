package com.xokem.twkad

import com.xokem.twkad.datagen.XokItemModelProvider
import com.xokem.twkad.datagen.XokLangProvider
import com.xokem.twkad.datagen.generate
import com.xokem.twkad.model.*
import com.xokem.twkad.model.firearmCategories
import com.xokem.twkad.model.metalComponentItems
import net.minecraft.world.item.Item
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.RegistryObject
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
        indexItem(XokTab.Items, incompleteFirearm)
        indexItem(XokTab.Items, incompleteFirearmComponent)
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
//        FORGE_BUS.addListener(::onEvaluateItemTooltip)
//
//        FORGE_BUS.addListener(::onRegisterCapabilities)
//        FORGE_BUS.addGenericListener(Level::class.java, ::onAttachLevelCapabilities)
//        FORGE_BUS.addGenericListener(Entity::class.java, ::onAttachPlayerCapabilities)
    }
}