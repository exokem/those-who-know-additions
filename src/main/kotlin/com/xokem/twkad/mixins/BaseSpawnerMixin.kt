package com.xokem.twkad.mixins

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BaseSpawner
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect

@Suppress("NonJavaMixin")
@Mixin(BaseSpawner::class)
class BaseSpawnerMixin
{
    private fun isUnlimitedSpawningAllowed(level: Level, position: BlockPos): Boolean
    {
        return level.getBlockState(position.above()).`is`(Blocks.NETHERITE_BLOCK)
    }

    @Redirect(method = ["clientTick", "serverTick"], at = At(value = "INVOKE", target = "Lnet/minecraft/world/level/BaseSpawner;isNearPlayer(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"))
    private fun isNearPlayerProxy(caller: BaseSpawner, level: Level, position: BlockPos): Boolean
    {
        return isUnlimitedSpawningAllowed(level, position) || (caller as BaseSpawnerAccessor).callIsNearPlayer(level, position)
    }
}