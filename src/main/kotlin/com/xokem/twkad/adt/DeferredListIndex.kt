package com.xokem.twkad.adt

import java.util.function.Supplier

class DeferredListIndex<TKey, TValue>
{
    private val map: HashMap<TKey, ArrayList<Supplier<TValue>>> = hashMapOf()

    fun <T: Supplier<TValue>> add(key: TKey, value: T)
    {
        if (map.containsKey(key))
            map[key]!!.add(value)
        else
            map[key] = arrayListOf(value)
    }

    fun <T: Supplier<TValue>> addAll(key: TKey, vararg values: T)
            = values.forEach { add(key, it) }

    operator fun get(key: TKey): List<Supplier<TValue>> = map[key]?.toList() ?: listOf()

    fun resolve(key: TKey): List<TValue> = map[key]?.map { it.get() }?.toList() ?: listOf()

    val keys
        get() = map.keys

    val values
        get() = map.values

    val resolvedValues
        get() = map.values.map { list -> list.map { it.get() } }
}