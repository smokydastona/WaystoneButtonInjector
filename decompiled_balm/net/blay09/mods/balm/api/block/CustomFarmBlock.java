/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 */
package net.blay09.mods.balm.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface CustomFarmBlock {
    public boolean canSustainPlant(BlockState var1, BlockGetter var2, BlockPos var3, Direction var4, Block var5);

    public boolean isFertile(BlockState var1, BlockGetter var2, BlockPos var3);
}

