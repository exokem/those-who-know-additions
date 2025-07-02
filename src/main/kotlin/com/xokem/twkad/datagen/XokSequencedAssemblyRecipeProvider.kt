package com.xokem.twkad.datagen

import com.simibubi.create.AllItems
import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen
import com.simibubi.create.content.fluids.transfer.FillingRecipe
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe
import com.simibubi.create.content.kinetics.press.PressingRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder
import com.xokem.twkad.XokMod
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import slimeknights.tconstruct.fluids.TinkerFluids
import java.util.function.UnaryOperator

class XokSequencedAssemblyRecipeProvider(output: PackOutput?) : SequencedAssemblyRecipeGen(output, XokMod.ID)
{
    init
    {
        sequenced("gunmetal_ingot") { builder ->
            builder.require(AllItems.IRON_SHEET).loops(1)
                .transitionTo(XokMod.getItem("incomplete_gunmetal_ingot"))
                .addOutput(XokMod.getItem("gunmetal_ingot"), 1.0F)
                .addStep(::DeployerApplicationRecipe) {
                    it.require(Items.GUNPOWDER)
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(AllItems.POWDERED_OBSIDIAN)
                }
                .addStep(::FillingRecipe) {
                    it.require(TinkerFluids.moltenSteel.flowing, 30)
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(AllItems.IRON_SHEET)
                }
                .addStep(::PressingRecipe) {
                    it
                }
        }

        sequenced("gunmetal_double_sheet") { builder ->
            builder.require(XokMod.getItem("gunmetal_sheet")).loops(1)
                .transitionTo(XokMod.getItem("incomplete_gunmetal_double_sheet"))
                .addOutput(XokMod.getItem("gunmetal_double_sheet"), 1.0F)
                .addStep(::DeployerApplicationRecipe) {
                    it.require(Items.GUNPOWDER)
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(AllItems.POWDERED_OBSIDIAN)
                }
                .addStep(::FillingRecipe) {
                    it.require(TinkerFluids.moltenSteel.flowing, 30)
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_sheet"))
                }
                .addStep(::PressingRecipe) {
                    it
                }
        }
    }

    fun sequenced(name: String, transform: UnaryOperator<SequencedAssemblyRecipeBuilder>) =
        create(name, transform)
}