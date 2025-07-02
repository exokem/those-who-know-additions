package com.xokem.twkad.datagen

import com.simibubi.create.api.data.recipe.CuttingRecipeGen
import com.simibubi.create.api.data.recipe.FillingRecipeGen
import com.simibubi.create.content.processing.recipe.ProcessingRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder
import com.xokem.twkad.XokMod
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import slimeknights.tconstruct.fluids.TinkerFluids
import java.util.function.UnaryOperator

class XokCuttingRecipeProvider(output: PackOutput?) : CuttingRecipeGen(output, XokMod.ID)
{
    init
    {
        cutting("gunmetal_nugget") {
            it.require(XokMod.getItem("gunmetal_ingot"))
                .output(XokMod.getItem("gunmetal_nugget"), 9)
        }

        cutting("carbine_barrel") {
            it.require(XokMod.getItem("rifle_barrel"))
                .output(XokMod.getItem("carbine_barrel"))
        }

        cutting("short_barrel") {
            it.require(XokMod.getItem("rifle_barrel"))
                .output(XokMod.getItem("short_barrel"), 2)
        }
    }

    fun cutting(name: String, transform: UnaryOperator<ProcessingRecipeBuilder<ProcessingRecipe<*>>>) =
        create(name, transform)
}