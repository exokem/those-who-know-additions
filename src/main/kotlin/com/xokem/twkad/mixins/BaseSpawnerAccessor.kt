package com.xokem.twkad.mixins

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BaseSpawner
import net.minecraft.world.level.Level
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Invoker

@Suppress("NonJavaMixin")
@Mixin(BaseSpawner::class)
interface BaseSpawnerAccessor
{
    @Invoker("isNearPlayer")
    fun callIsNearPlayer(level: Level, pos: BlockPos): Boolean
}