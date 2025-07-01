package com.xokem.twkad.adt

import net.minecraftforge.common.data.LanguageProvider
import java.util.*

abstract class LangStack(private val provider: LanguageProvider)
{
    private val stack = Stack<String>()

    private fun toLocalizedString(id: String): String
    {
        return id.split("_").map {
            it.capitalize()
        }.joinToString(" ")
    }

    protected fun push(key: String, action: () -> Unit)
    {
        stack.push(key)
        action()
        stack.pop()
    }

    protected fun addManual(key: String, name: String)
    {
        val path = stack.joinToString(".")

        provider.add("$path.$key", name)
    }

    protected fun add(key: String)
    {
        val path = stack.joinToString(".")

        if (key.isNotEmpty())
            provider.add("$path.$key", toLocalizedString(key))
        else
            provider.add(path, toLocalizedString(key))
    }

    protected fun addInverse(name: String)
    {
        val path = stack.joinToString(".")
        val key = name.replace(" ", "_").lowercase()
        provider.add("$path.$key", name)
    }

    abstract fun addEntries()
}