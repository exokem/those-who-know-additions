package com.xokem.twkad.datagen

import com.xokem.twkad.CurrencyItem
import com.xokem.twkad.XokMod
import com.xokem.twkad.adt.LangStack
import com.xokem.twkad.model.firearmComponents
import com.xokem.twkad.model.metalComponentItems
import com.xokem.twkad.model.metalComponents
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

                    metalComponents.entries.forEach { (component, metals) ->
                        metals.forEach {
                            addInverse("$it $component")
                        }
                    }

                    firearmComponents.entries.forEach { (component, data) ->
                        data.variants?.keys?.forEach {
                            addInverse(if (data.includeCategoryName == false) it else "$it $component" )
                        }
                    }

                    add("blazing_gunpowder")

                    addManual("incomplete_firearm", "Incomplete Firearm")
                }
            }
        }
    }

    override fun addTranslations()
    {
        stack.addEntries()
    }
}