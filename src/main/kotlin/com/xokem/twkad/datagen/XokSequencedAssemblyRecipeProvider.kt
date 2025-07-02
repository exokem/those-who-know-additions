package com.xokem.twkad.datagen

import com.simibubi.create.AllItems
import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen
import com.simibubi.create.content.fluids.transfer.FillingRecipe
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe
import com.simibubi.create.content.kinetics.press.PressingRecipe
import com.simibubi.create.content.kinetics.saw.CuttingRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder
import com.xokem.twkad.XokMod
import com.xokem.twkad.model.blazingGunpowder
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.minecraftforge.common.Tags
import slimeknights.tconstruct.common.TinkerTags
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

        sequenced("light_cartridge") { builder ->
            builder.require(XokMod.getItem("light_casing")).loops(1)
                .transitionTo(XokMod.getItem("incomplete_light_casing"))
                .addOutput(XokMod.getItem("light_cartridge"), 1.0F)
                .addStep(::DeployerApplicationRecipe) {
                    it.require(blazingGunpowder.get())
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_nugget"))
                }
                .addStep(::PressingRecipe) {
                    it
                }
        }

        sequenced("rifle_cartridge") { builder ->
            builder.require(XokMod.getItem("rifle_casing")).loops(1)
                .transitionTo(XokMod.getItem("incomplete_rifle_casing"))
                .addOutput(XokMod.getItem("rifle_cartridge"), 1.0F)
                .addStep(::DeployerApplicationRecipe) {
                    it.require(blazingGunpowder.get())
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(blazingGunpowder.get())
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_nugget"))
                }
                .addStep(::PressingRecipe) {
                    it
                }
        }

        sequenced("precision_cartridge") { builder ->
            builder.require(XokMod.getItem("precision_casing")).loops(1)
                .transitionTo(XokMod.getItem("incomplete_precision_casing"))
                .addOutput(XokMod.getItem("precision_cartridge"), 1.0F)
                .addStep(::DeployerApplicationRecipe) {
                    it.require(blazingGunpowder.get())
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(blazingGunpowder.get())
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(blazingGunpowder.get())
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(blazingGunpowder.get())
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_nugget"))
                }
                .addStep(::PressingRecipe) {
                    it
                }
        }

        sequenced("amr_cartridge") { builder ->
            builder.require(XokMod.getItem("amr_casing")).loops(8)
                .transitionTo(XokMod.getItem("incomplete_amr_casing"))
                .addOutput(XokMod.getItem("amr_cartridge"), 1.0F)
                .addStep(::DeployerApplicationRecipe) {
                    it.require(blazingGunpowder.get())
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_nugget"))
                }
                .addStep(::PressingRecipe) {
                    it
                }
        }

        sequenced("shotgun_shell") { builder ->
            builder.require(XokMod.getItem("light_casing")).loops(5)
                .transitionTo(XokMod.getItem("incomplete_shotgun_shell"))
                .addOutput(XokMod.getItem("shotgun_shell"), 1.0F)
                .addStep(::DeployerApplicationRecipe) {
                    it.require(blazingGunpowder.get())
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_nugget"))
                }
                .addStep(::PressingRecipe) {
                    it
                }
        }

        sequenced("firearm_mechanism") { builder ->
            builder.require(XokMod.getItem("gunmetal_double_sheet")).loops(4)
                .transitionTo(XokMod.getItem("incomplete_firearm_mechanism"))
                .addOutput(XokMod.getItem("firearm_mechanism"), 1.0F)
                .addStep(::DeployerApplicationRecipe) {
                    it.require(AllItems.POLISHED_ROSE_QUARTZ)
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(AllItems.BRASS_SHEET)
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_nugget"))
                }
                .addStep(::PressingRecipe) {
                    it
                }
        }

        sequenced("light_receiver") { builder ->
            builder.require(XokMod.getItem("short_barrel")).loops(1)
                .transitionTo(XokMod.getItem("incomplete_light_receiver"))
                .addOutput(XokMod.getItem("light_receiver"), 1.0F)
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_sheet"))
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("firearm_mechanism"))
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_sheet"))
                }
                .addStep(::PressingRecipe) {
                    it
                }
        }

        sequenced("rifle_receiver") { builder ->
            builder.require(XokMod.getItem("carbine_barrel")).loops(1)
                .transitionTo(XokMod.getItem("incomplete_rifle_receiver"))
                .addOutput(XokMod.getItem("rifle_receiver"), 1.0F)
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_sheet"))
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("firearm_mechanism"))
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("firearm_mechanism"))
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_sheet"))
                }
                .addStep(::PressingRecipe) {
                    it
                }
        }

        sequenced("precision_receiver") { builder ->
            builder.require(XokMod.getItem("rifle_barrel")).loops(1)
                .transitionTo(XokMod.getItem("incomplete_precision_receiver"))
                .addOutput(XokMod.getItem("precision_receiver"), 1.0F)
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_sheet"))
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("firearm_mechanism"))
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(AllItems.PRECISION_MECHANISM)
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(AllItems.POLISHED_ROSE_QUARTZ)
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_sheet"))
                }
                .addStep(::PressingRecipe) {
                    it
                }
        }

        sequenced("amr_receiver") { builder ->
            builder.require(XokMod.getItem("heavy_barrel")).loops(1)
                .transitionTo(XokMod.getItem("incomplete_amr_receiver"))
                .addOutput(XokMod.getItem("amr_receiver"), 1.0F)
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_double_sheet"))
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("firearm_mechanism"))
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(AllItems.PRECISION_MECHANISM)
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(AllItems.POLISHED_ROSE_QUARTZ)
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_double_sheet"))
                }
                .addStep(::PressingRecipe) {
                    it
                }
        }

        sequenced("shotgun_receiver") { builder ->
            builder.require(XokMod.getItem("double_barrel")).loops(1)
                .transitionTo(XokMod.getItem("incomplete_shotgun_receiver"))
                .addOutput(XokMod.getItem("shotgun_receiver"), 1.0F)
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_double_sheet"))
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("firearm_mechanism"))
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("firearm_mechanism"))
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(AllItems.POLISHED_ROSE_QUARTZ)
                }
                .addStep(::DeployerApplicationRecipe) {
                    it.require(XokMod.getItem("gunmetal_double_sheet"))
                }
                .addStep(::PressingRecipe) {
                    it
                }
        }
    }

    fun sequenced(name: String, transform: UnaryOperator<SequencedAssemblyRecipeBuilder>) =
        create(name, transform)
}