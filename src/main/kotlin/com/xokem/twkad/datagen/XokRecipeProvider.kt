package com.xokem.twkad.datagen

import com.xokem.twkad.XokMod
import com.xokem.twkad.model.blazingGunpowder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.stats.Stat.buildName
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Recipe
import java.util.function.Consumer

class XokRecipeProvider(output: PackOutput) : RecipeProvider(output), Consumer<FinishedRecipe>
{
    private fun Item.getResourceKey(): String = BuiltInRegistries.ITEM.getKey(this).path
    private fun TagKey<*>.getResourceKey(): String = location().path

    private fun TagKey<*>.getSafeResourceKey() = getResourceKey().replace("/", "_")

    fun buildRecipeName(output: Item, inputs: Iterable<Item>, tags: Iterable<TagKey<Item>>): String
    {
        val inputNames = inputs.map { it.getResourceKey() } + tags.map { it.getSafeResourceKey() }
        return "${output.getResourceKey()}_from_${inputNames.joinToString("_")}"
    }

    private fun shapeless(
        output: Item,
        amount: Int = 1,
        category: RecipeCategory = RecipeCategory.MISC,
        consumer: (item: (Item) -> Unit, tag: (TagKey<Item>) -> Unit) -> Unit
    )
    {
        val inputs = arrayListOf<Item>()
        val tags = arrayListOf<TagKey<Item>>()

        consumer(inputs::add, tags::add)

        val builder = ShapelessRecipeBuilder.shapeless(category, output, amount)
        inputs.forEach(builder::requires)
        tags.forEach(builder::requires)
        builder.unlockedBy(getHasName(inputs.first()), has(inputs.first()))
        builder.save(this, XokMod.resource("shapeless/${buildRecipeName(output, inputs, tags)}"))
    }

    private fun shaped(
        output: Item,
        pattern: String,
        amount: Int = 1,
        category: RecipeCategory = RecipeCategory.MISC,
        consumer: (item: (Char, Item) -> Unit, tag: (Char, TagKey<Item>) -> Unit) -> Unit
    )
    {
        val inputs = hashMapOf<Char, Item>()
        val tags = hashMapOf<Char, TagKey<Item>>()

        consumer(inputs::put, tags::put)

        val patternParts = pattern.split("-")
//        val patternParts = pattern.chunked(sqrt(pattern.length.toDouble()).toInt())

        val builder = ShapedRecipeBuilder.shaped(category, output, amount)
        patternParts.forEach(builder::pattern)
        inputs.forEach(builder::define)
        tags.forEach(builder::define)
        builder.unlockedBy(getHasName(inputs.values.first()), has(inputs.values.first()))
        builder.save(this, XokMod.resource("shaped/${buildRecipeName(output, inputs.values, tags.values)}"))
    }

    private lateinit var receiver: Consumer<FinishedRecipe>

    override fun buildRecipes(consumer: Consumer<FinishedRecipe>)
    {
        receiver = consumer

        shapeless(blazingGunpowder.get(), 8, RecipeCategory.MISC) { item, tag ->
            item(Items.GUNPOWDER)
            item(Items.BLAZE_POWDER)
        }
    }

    override fun accept(t: FinishedRecipe) = receiver.accept(t)
}