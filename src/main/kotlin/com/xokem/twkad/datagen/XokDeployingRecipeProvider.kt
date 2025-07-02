package com.xokem.twkad.datagen

import com.simibubi.create.api.data.recipe.DeployingRecipeGen
import com.simibubi.create.content.processing.recipe.ProcessingRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder
import com.xokem.twkad.XokMod
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import java.util.function.UnaryOperator

class XokDeployingRecipeProvider(output: PackOutput?) : DeployingRecipeGen(output, XokMod.ID)
{
    init
    {
        deploying("light_casing_form") {
            it.require(Items.CLAY_BALL)
                .require(Items.STICK)
                .output(XokMod.getItem("light_casing_form"))
                .toolNotConsumed()
        }
        deploying("rifle_casing_form") {
            it.require(XokMod.getItem("light_casing_form"))
                .require(Items.STICK)
                .output(XokMod.getItem("rifle_casing_form"))
                .toolNotConsumed()
        }
        deploying("precision_casing_form") {
            it.require(XokMod.getItem("rifle_casing_form"))
                .require(Items.STICK)
                .output(XokMod.getItem("precision_casing_form"))
                .toolNotConsumed()
        }
        deploying("amr_casing_form") {
            it.require(XokMod.getItem("precision_casing_form"))
                .require(Items.STICK)
                .output(XokMod.getItem("amr_casing_form"))
                .toolNotConsumed()
        }
    }

    fun deploying(name: String, transform: UnaryOperator<ProcessingRecipeBuilder<ProcessingRecipe<*>>>) =
        create(name, transform)
}