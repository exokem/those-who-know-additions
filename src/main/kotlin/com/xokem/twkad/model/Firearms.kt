package com.xokem.twkad.model

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.InputStreamReader

@Serializable
data class CategoryData(
    val categories: Map<String, Category>
)

@Serializable
data class Category(
    val id: String,
    val name: String,
    val ammunition: String,
    val components: Components,
    val entries: List<Entry>
)

@Serializable
data class Components(
    val receiver: String,
    val barrel: String,
    val grip: Boolean,
    val magazine: Boolean,
    val attachments: Boolean,
    val addons: List<String>
)

@Serializable
data class Entry(
    val id: String,
    val name: String,
    val fireMode: String
)

private val data by lazy {
    val classLoader = Thread.currentThread().contextClassLoader
    val inputStream = classLoader.getResourceAsStream("firearms.json")
        ?: error("firearms.json not found in resources")

    val jsonText = InputStreamReader(inputStream).readText()

    return@lazy Json.decodeFromString<CategoryData>(jsonText)
}

internal val firearmCategories get() = data.categories
