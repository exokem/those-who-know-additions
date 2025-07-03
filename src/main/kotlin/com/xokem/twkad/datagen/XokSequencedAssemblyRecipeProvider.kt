package com.xokem.twkad.datagen

import com.simibubi.create.AllItems
import com.simibubi.create.AllRecipeTypes
import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe
import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen
import com.simibubi.create.content.fluids.transfer.FillingRecipe
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe
import com.simibubi.create.content.kinetics.press.PressingRecipe
import com.simibubi.create.content.processing.recipe.ProcessingOutput
import com.simibubi.create.content.processing.recipe.ProcessingRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeSerializer
import com.simibubi.create.content.processing.sequenced.SequencedRecipe
import com.xokem.twkad.XokMod
import com.xokem.twkad.model.blazingGunpowder
import com.xokem.twkad.model.firearmCategories
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraftforge.common.crafting.StrictNBTIngredient
import net.minecraftforge.common.crafting.conditions.ICondition
import slimeknights.tconstruct.fluids.TinkerFluids
import java.util.function.Consumer
import java.util.function.UnaryOperator

class NBTSequencedAssemblyRecipe(recipeId: ResourceLocation?, serializer: SequencedAssemblyRecipeSerializer?) : SequencedAssemblyRecipe(recipeId, serializer)
{
    fun setTransitionalItem(output: ProcessingOutput)
    {
        transitionalItem = output
    }

    fun setIngredient(ingredient: Ingredient)
    {
        this.ingredient = ingredient
    }

    fun setLoops(loops: Int)
    {
        this.loops = loops
    }
}

class NBTSequencedAssemblyRecipeBuilder(id: ResourceLocation?) : SequencedAssemblyRecipeBuilder(id)
{
    private val recipe: NBTSequencedAssemblyRecipe = NBTSequencedAssemblyRecipe(id, AllRecipeTypes.SEQUENCED_ASSEMBLY.getSerializer())
    private val conditions = arrayListOf<ICondition>()

    override fun transitionTo(item: ItemLike): SequencedAssemblyRecipeBuilder
    {
        recipe.setTransitionalItem(ProcessingOutput(ItemStack(item), 1.0f))
        return this
    }

    override fun <T : ProcessingRecipe<*>?> addStep(factory: ProcessingRecipeBuilder.ProcessingRecipeFactory<T>, builder: UnaryOperator<ProcessingRecipeBuilder<T>>): SequencedAssemblyRecipeBuilder
    {
        val recipeBuilder: ProcessingRecipeBuilder<T> = ProcessingRecipeBuilder(factory, ResourceLocation("dummy"))
        val placeHolder = this.recipe.transitionalItem.item
        this.recipe.sequence.add(SequencedRecipe((builder.apply(recipeBuilder.require(placeHolder).output(placeHolder)) as ProcessingRecipeBuilder<*>).build()))
        return this
    }

    override fun require(ingredient: Ingredient?): SequencedAssemblyRecipeBuilder
    {
        recipe.ingredient = ingredient
        return this
    }

    override fun loops(loops: Int): SequencedAssemblyRecipeBuilder
    {
        recipe.loops = loops
        return super.loops(loops)
    }

    override fun addOutput(item: ItemStack?, weight: Float): SequencedAssemblyRecipeBuilder
    {
        recipe.resultPool.add(ProcessingOutput(item, weight))
        return this
    }

    override fun build(): SequencedAssemblyRecipe = recipe

    override fun build(consumer: Consumer<FinishedRecipe>) =
        consumer.accept(DataGenResult(build(), conditions))

    fun transitionTo(stack: ItemStack): NBTSequencedAssemblyRecipeBuilder
    {
        recipe.setTransitionalItem(ProcessingOutput(stack, 1.0F))
        return this
    }
}

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
                .addOutput(XokMod.getAnyItem("tacz:light_cartridge"), 1.0F)
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
                .addOutput(XokMod.getAnyItem("tacz:rifle_cartridge"), 1.0F)
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
                .addOutput(XokMod.getAnyItem("tacz:precision_cartridge"), 1.0F)
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
                .addOutput(XokMod.getAnyItem("tacz:amr_cartridge"), 1.0F)
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
                .addOutput(XokMod.getAnyItem("tacz:shotgun_shell"), 1.0F)
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

        firearmCategories.entries.forEach { (categoryName, category) ->
            category.entries.forEachIndexed { index, entry ->

                val gunItem = ItemStack(XokMod.getAnyItem("tacz:modern_kinetic_gun")!!)
                val tag = CompoundTag()
                tag.putString("GunId", "tacz:${entry.id}")
                tag.putString("GunFireMode", entry.fireMode)
                gunItem.tag = tag

                val schematicItem = ItemStack(XokMod.getItem("firearm_schematic"))
                val schematicTag = CompoundTag()
                schematicTag.putString("category", categoryName)
                schematicTag.putInt("selectedIndex", index)
                schematicItem.tag = schematicTag

                val transItem = ItemStack(XokMod.getItem("incomplete_firearm"))
                val transTag = CompoundTag()
                transTag.putString("category", categoryName)
                transTag.putInt("selectedIndex", index)
                transItem.tag = transTag

                sequencedNBT(entry.id) { builder ->
                    builder.transitionTo(transItem)
                        .require(blazingGunpowder.get()).loops(1)
                        .addOutput(gunItem, 1.0F)
                        .addStep(::DeployerApplicationRecipe) {
                            it.require(StrictNBTIngredient.of(schematicItem))
                                .toolNotConsumed()
                        }
                        .addStep(::DeployerApplicationRecipe) {
                            it.require(XokMod.getItem("${category.components.receiver}_receiver"))
                        }
                        .addStep(::DeployerApplicationRecipe) {
                            it.require(XokMod.getItem("${category.components.barrel}_barrel"))
                        }
                        .addStep(::DeployerApplicationRecipe) {
                            it.require(XokMod.getItem("gunmetal_double_sheet"))
                        }

                    as NBTSequencedAssemblyRecipeBuilder
                }
            }
        }
    }

    fun sequencedNBT(name: String?, transform: UnaryOperator<NBTSequencedAssemblyRecipeBuilder>): GeneratedRecipe
    {
        val generatedRecipe = GeneratedRecipe { c: Consumer<FinishedRecipe?>? ->
            (transform.apply(NBTSequencedAssemblyRecipeBuilder(this.asResource(name))) as SequencedAssemblyRecipeBuilder).build(c)
        }
        all.add(generatedRecipe)
        return generatedRecipe
    }

    fun sequenced(name: String, transform: UnaryOperator<SequencedAssemblyRecipeBuilder>) =
        create(name, transform)
}