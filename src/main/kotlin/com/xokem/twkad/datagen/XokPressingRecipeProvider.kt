package com.xokem.twkad.datagen

import com.simibubi.create.AllFluids
import com.simibubi.create.AllItems
import com.simibubi.create.api.data.recipe.FillingRecipeGen
import com.simibubi.create.api.data.recipe.PressingRecipeGen
import com.simibubi.create.content.processing.recipe.ProcessingRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder
import com.xokem.twkad.XokMod
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import slimeknights.tconstruct.fluids.TinkerFluids
import java.util.function.UnaryOperator

class XokPressingRecipeProvider(output: PackOutput?) : PressingRecipeGen(output, XokMod.ID)
{
    init
    {
        pressing("gunmetal_sheet") {
            it.require(XokMod.getItem("gunmetal_ingot"))
                .output(XokMod.getItem("gunmetal_sheet"))
        }
    }

    fun pressing(name: String, transform: UnaryOperator<ProcessingRecipeBuilder<ProcessingRecipe<*>>>) =
        create(name, transform)
}