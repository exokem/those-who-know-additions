package com.xokem.twkad.datagen

import com.xokem.twkad.CurrencyItem
import com.xokem.twkad.XokMod
import net.minecraft.data.PackOutput
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.common.data.ExistingFileHelper

class XokItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) : ItemModelProvider(output, XokMod.ID, existingFileHelper)
{
    override fun registerModels()
    {
        CurrencyItem.values().forEach {
            super.basicItem(it.blankItemHolder.get())
            super.basicItem(it.formItemHolder.get())
        }
    }
}