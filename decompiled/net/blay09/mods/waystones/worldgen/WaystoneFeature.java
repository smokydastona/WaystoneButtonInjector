/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.WorldGenLevel
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.levelgen.feature.Feature
 *  net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
 *  net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
 */
package net.blay09.mods.waystones.worldgen;

import com.mojang.serialization.Codec;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.blay09.mods.waystones.block.WaystoneBlock;
import net.blay09.mods.waystones.block.WaystoneBlockBase;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WaystoneFeature
extends Feature<NoneFeatureConfiguration> {
    private final BlockState waystoneState;

    public WaystoneFeature(Codec<NoneFeatureConfiguration> codec, BlockState waystoneState) {
        super(codec);
        this.waystoneState = waystoneState;
    }

    public boolean m_142674_(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.m_159774_();
        BlockPos pos = context.m_159777_();
        RandomSource random = context.m_225041_();
        Direction facing = Direction.values()[2 + random.m_188503_(4)];
        BlockState state = world.m_8055_(pos);
        BlockPos posAbove = pos.m_7494_();
        BlockState stateAbove = world.m_8055_(posAbove);
        if (state.m_60795_() && stateAbove.m_60795_()) {
            world.m_7731_(pos, (BlockState)((BlockState)((BlockState)this.waystoneState.m_61124_((Property)WaystoneBlock.FACING, (Comparable)facing)).m_61124_((Property)WaystoneBlock.HALF, (Comparable)DoubleBlockHalf.LOWER)).m_61124_(WaystoneBlockBase.ORIGIN, (Comparable)((Object)WaystoneOrigin.WILDERNESS)), 2);
            world.m_7731_(posAbove, (BlockState)((BlockState)((BlockState)this.waystoneState.m_61124_((Property)WaystoneBlock.FACING, (Comparable)facing)).m_61124_((Property)WaystoneBlock.HALF, (Comparable)DoubleBlockHalf.UPPER)).m_61124_(WaystoneBlockBase.ORIGIN, (Comparable)((Object)WaystoneOrigin.WILDERNESS)), 2);
            WaystoneBlockEntity tileEntity = (WaystoneBlockEntity)world.m_7702_(pos);
            if (tileEntity != null) {
                tileEntity.initializeWaystone((ServerLevelAccessor)world, null, WaystoneOrigin.WILDERNESS);
                BlockEntity tileEntityAbove = world.m_7702_(pos.m_7494_());
                if (tileEntityAbove instanceof WaystoneBlockEntity) {
                    ((WaystoneBlockEntity)tileEntityAbove).initializeFromBase(tileEntity);
                }
            }
            return true;
        }
        return false;
    }
}

