package com.xokem.twkad

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTab.Builder
import net.minecraft.world.item.ItemStack

internal fun registerTabs() = XokTab.values().forEach {
    Registry.TabIndex[it] = Registry.Tabs.register(it.identifier) { it.builder().build() }
}

internal enum class XokTab(private val icon: String)
{
    Items("bevel_blank"),
    ;

    val identifier = name.lowercase()
    private val resource = XokMod.resource(identifier)

    private fun tabsBefore(): Array<ResourceLocation>
            = if (ordinal == 0) arrayOf() else arrayOf(values()[ordinal - 1].resource)

    fun builder(): Builder = CreativeModeTab.builder()
        .title(XokMod.translate("tab.$identifier"))
        .icon { ItemStack(XokMod.getItem(icon)) }
        .withTabsBefore(*tabsBefore())
}