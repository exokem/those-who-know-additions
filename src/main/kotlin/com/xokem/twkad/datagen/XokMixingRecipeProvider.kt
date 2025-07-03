package com.xokem.twkad.datagen

import com.simibubi.create.AllFluids
import com.simibubi.create.AllItems
import com.simibubi.create.api.data.recipe.FillingRecipeGen
import com.simibubi.create.api.data.recipe.MixingRecipeGen
import com.simibubi.create.content.processing.recipe.HeatCondition
import com.simibubi.create.content.processing.recipe.ProcessingRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder
import com.xokem.twkad.XokMod
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.common.Tags
import slimeknights.tconstruct.fluids.TinkerFluids
import java.util.function.UnaryOperator

class XokMixingRecipeProvider(output: PackOutput?) : MixingRecipeGen(output, XokMod.ID)
{
    init
    {
        mixing("steel_mixing") {
            it.output(XokMod.getAnyItem("mekanism:steel_ingot"), 2)
                .require(Tags.Items.INGOTS_IRON)
                .require(Tags.Items.INGOTS_IRON)
                .require(Items.COAL)
                .requiresHeat(HeatCondition.HEATED)
        }
    }

    fun mixing(name: String, transform: UnaryOperator<ProcessingRecipeBuilder<ProcessingRecipe<*>>>) =
        create(name, transform)
}