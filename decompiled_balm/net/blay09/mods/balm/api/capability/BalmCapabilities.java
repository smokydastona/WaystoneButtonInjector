/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.api.capability;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.capability.CapabilityType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface BalmCapabilities {
    public <TApi, TContext> TApi getCapability(Level var1, BlockPos var2, BlockState var3, @Nullable BlockEntity var4, TContext var5, CapabilityType<Block, TApi, TContext> var6);

    public <TScope, TApi, TContext> CapabilityType<TScope, TApi, TContext> registerType(ResourceLocation var1, Class<TScope> var2, Class<TApi> var3, Class<TContext> var4);

    public <TScope, TApi, TContext> CapabilityType<TScope, TApi, TContext> getType(ResourceLocation var1, Class<TScope> var2, Class<TApi> var3, Class<TContext> var4);

    default public <TApi> TApi getCapability(BlockEntity blockEntity, CapabilityType<Block, TApi, ?> type) {
        return this.getCapability(blockEntity, null, type);
    }

    default public <TApi, TContext> TApi getCapability(BlockEntity blockEntity, TContext context, CapabilityType<Block, TApi, TContext> type) {
        return this.getCapability(blockEntity.m_58904_(), blockEntity.m_58899_(), blockEntity.m_58900_(), blockEntity, context, type);
    }

    public <TApi, TContext> void registerProvider(ResourceLocation var1, CapabilityType<Block, TApi, TContext> var2, BiFunction<BlockEntity, TContext, TApi> var3, Supplier<List<BlockEntityType<?>>> var4);

    public <TApi, TContext> void registerFallbackBlockEntityProvider(ResourceLocation var1, CapabilityType<Block, TApi, TContext> var2, BiFunction<BlockEntity, TContext, TApi> var3);
}

