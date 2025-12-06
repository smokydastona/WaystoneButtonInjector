/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.StemBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.block.CustomFarmBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={StemBlock.class})
public class StemBlockMixin {
    @Inject(method={"mayPlaceOn(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z"}, at={@At(value="HEAD")}, cancellable=true)
    public void mayPlaceOn(BlockState state, BlockGetter blockGetter, BlockPos pos, CallbackInfoReturnable<Boolean> callbackInfo) {
        Block block = state.m_60734_();
        if (block instanceof CustomFarmBlock) {
            CustomFarmBlock customFarmBlock = (CustomFarmBlock)block;
            callbackInfo.setReturnValue((Object)customFarmBlock.canSustainPlant(state, blockGetter, pos, Direction.UP, blockGetter.m_8055_(pos.m_121945_(Direction.UP)).m_60734_()));
        }
    }
}

