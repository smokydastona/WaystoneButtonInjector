/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.BlockPos$MutableBlockPos
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.levelgen.Heightmap$Types
 *  net.minecraft.world.level.levelgen.placement.PlacementContext
 *  net.minecraft.world.level.levelgen.placement.PlacementModifier
 *  net.minecraft.world.level.levelgen.placement.PlacementModifierType
 */
package net.blay09.mods.waystones.worldgen;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.worldgen.ModWorldGen;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class WaystonePlacement
extends PlacementModifier {
    public static final Codec<WaystonePlacement> CODEC = RecordCodecBuilder.create(builder -> builder.group((App)Heightmap.Types.f_64274_.fieldOf("heightmap").forGetter(placement -> placement.heightmap)).apply((Applicative)builder, WaystonePlacement::new));
    private final Heightmap.Types heightmap;

    public WaystonePlacement() {
        this(Heightmap.Types.OCEAN_FLOOR_WG);
    }

    public WaystonePlacement(Heightmap.Types heightmap) {
        this.heightmap = heightmap;
    }

    public Stream<BlockPos> m_213676_(PlacementContext context, RandomSource random, BlockPos pos) {
        if (this.isWaystoneChunk(context, pos)) {
            int z;
            if (context.m_191831_().m_6018_().m_46472_() == Level.f_46429_) {
                BlockPos.MutableBlockPos mutablePos = pos.m_122032_();
                int topMostY = context.m_191824_(this.heightmap, pos.m_123341_(), pos.m_123343_());
                mutablePos.m_142448_(topMostY);
                BlockState stateAbove = context.m_191831_().m_8055_((BlockPos)mutablePos);
                for (int i = mutablePos.m_123342_(); i >= 1; --i) {
                    mutablePos.m_142448_(mutablePos.m_123342_() - 1);
                    BlockState state = context.m_191831_().m_8055_((BlockPos)mutablePos);
                    if (!state.m_60795_() && state.m_60819_().m_76178_() && stateAbove.m_60795_() && !state.m_60713_(Blocks.f_50752_)) {
                        mutablePos.m_142448_(mutablePos.m_123342_() + 1);
                        break;
                    }
                    stateAbove = state;
                }
                return mutablePos.m_123342_() > 0 ? Stream.of(mutablePos) : Stream.empty();
            }
            int x = pos.m_123341_();
            int y = context.m_191824_(this.heightmap, x, z = pos.m_123343_());
            return y > context.m_191830_() ? Stream.of(new BlockPos(x, y, z)) : Stream.of(new BlockPos[0]);
        }
        return Stream.empty();
    }

    public PlacementModifierType<?> m_183327_() {
        return (PlacementModifierType)ModWorldGen.waystonePlacement.get();
    }

    private boolean isWaystoneChunk(PlacementContext world, BlockPos pos) {
        int chunkDistance = WaystonesConfig.getActive().worldGen.frequency;
        if (chunkDistance == 0) {
            return false;
        }
        ResourceLocation dimension = world.m_191831_().m_6018_().m_46472_().m_135782_();
        List<String> dimensionAllowList = WaystonesConfig.getActive().worldGen.dimensionAllowList;
        List<String> dimensionDenyList = WaystonesConfig.getActive().worldGen.dimensionDenyList;
        if (!dimensionAllowList.isEmpty() && !dimensionAllowList.contains(dimension.toString())) {
            return false;
        }
        if (!dimensionDenyList.isEmpty() && dimensionDenyList.contains(dimension.toString())) {
            return false;
        }
        int maxDeviation = (int)Math.ceil((float)chunkDistance / 2.0f);
        int chunkX = pos.m_123341_() / 16;
        int chunkZ = pos.m_123343_() / 16;
        int devGridX = pos.m_123341_() / 16 * maxDeviation;
        int devGridZ = pos.m_123343_() / 16 * maxDeviation;
        long seed = world.m_191831_().m_7328_();
        Random random = new Random(seed * (long)devGridX * (long)devGridZ);
        int chunkOffsetX = random.nextInt(maxDeviation);
        int chunkOffsetZ = random.nextInt(maxDeviation);
        return (chunkX + chunkOffsetX) % chunkDistance == 0 && (chunkZ + chunkOffsetZ) % chunkDistance == 0;
    }
}

