package com.xokem.twkad.datagen

import com.simibubi.create.api.data.recipe.CompactingRecipeGen
import com.simibubi.create.api.data.recipe.CuttingRecipeGen
import com.simibubi.create.api.data.recipe.FillingRecipeGen
import com.simibubi.create.content.processing.recipe.ProcessingRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder
import com.xokem.twkad.XokMod
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import slimeknights.tconstruct.fluids.TinkerFluids
import java.util.function.UnaryOperator

class XokCompactingRecipeProvider(output: PackOutput?) : CompactingRecipeGen(output, XokMod.ID)
{
    init
    {
        compacting("double_barrel") {
            it.require(XokMod.getItem("rifle_barrel"))
                .require(XokMod.getItem("rifle_barrel"))
                .output(XokMod.getItem("double_barrel"))
        }
    }

    fun compacting(name: String, transform: UnaryOperator<ProcessingRecipeBuilder<ProcessingRecipe<*>>>) =
        create(name, transform)
}