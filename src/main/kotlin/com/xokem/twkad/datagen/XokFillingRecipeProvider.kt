package com.xokem.twkad.datagen

import com.simibubi.create.AllFluids
import com.simibubi.create.AllItems
import com.simibubi.create.api.data.recipe.FillingRecipeGen
import com.simibubi.create.content.processing.recipe.ProcessingRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder
import com.xokem.twkad.XokMod
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import slimeknights.tconstruct.fluids.TinkerFluids
import java.util.function.UnaryOperator

class XokFillingRecipeProvider(output: PackOutput?) : FillingRecipeGen(output, XokMod.ID)
{
    init
    {
        filling("light_casing") {
            it.require(TinkerFluids.moltenBrass.get(), 4)
                .require(XokMod.getItem("light_casing_form"))
                .output(XokMod.getItem("light_casing"))
        }

        filling("rifle_casing") {
            it.require(TinkerFluids.moltenBrass.get(), 8)
                .require(XokMod.getItem("rifle_casing_form"))
                .output(XokMod.getItem("rifle_casing"))
        }

        filling("precision_casing") {
            it.require(TinkerFluids.moltenBrass.get(), 24)
                .require(XokMod.getItem("precision_casing_form"))
                .output(XokMod.getItem("precision_casing"))
        }

        filling("amr_casing") {
            it.require(TinkerFluids.moltenBrass.get(), 48)
                .require(XokMod.getItem("amr_casing_form"))
                .output(XokMod.getItem("amr_casing"))
        }
    }

    fun filling(name: String, transform: UnaryOperator<ProcessingRecipeBuilder<ProcessingRecipe<*>>>) =
        create(name, transform)
}