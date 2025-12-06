/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 */
package net.blay09.mods.balm.api.block;

import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntityFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface BalmBlockEntities {
    public <T extends BlockEntity> DeferredObject<BlockEntityType<T>> registerBlockEntity(ResourceLocation var1, BalmBlockEntityFactory<T> var2, Supplier<Block[]> var3);

    public <T extends BlockEntity> DeferredObject<BlockEntityType<T>> registerBlockEntity(ResourceLocation var1, BalmBlockEntityFactory<T> var2, DeferredObject<Block> ... var3);
}

