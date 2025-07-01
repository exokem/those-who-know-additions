package com.xokem.twkad.adt

import net.minecraftforge.registries.RegistryObject

class DeferredIndex<TKey, TValue>
{
    private val map: HashMap<TKey, RegistryObject<TValue>> = hashMapOf()

    operator fun set(key: TKey, value: RegistryObject<TValue>) = map.set(key, value)

    operator fun get(key: TKey): RegistryObject<TValue>? = map[key]

    fun resolve(key: TKey): TValue? = map[key]?.get()

    val keys
        get() = map.keys

    val values
        get() = map.values

    val resolvedValues
        get() = map.values.map { it.get() }
}
