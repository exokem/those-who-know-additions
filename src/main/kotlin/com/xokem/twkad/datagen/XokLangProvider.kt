package com.xokem.twkad.datagen

import com.xokem.twkad.CurrencyItem
import com.xokem.twkad.XokMod
import com.xokem.twkad.adt.LangStack
import net.minecraft.data.PackOutput
import net.minecraftforge.common.data.LanguageProvider

class XokLangProvider(output: PackOutput, locale: String) : LanguageProvider(output, XokMod.ID, locale)
{
    private val stack = object: LangStack(this)
    {
        override fun addEntries()
        {
            push("item") {
                push("twkad") {
                    CurrencyItem.values().forEach {
                        add(it.blankId)
                        add(it.formId)
                    }
                }
            }
        }
    }

    override fun addTranslations()
    {
        stack.addEntries()
    }
}