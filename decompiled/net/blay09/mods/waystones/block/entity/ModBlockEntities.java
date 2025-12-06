/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.DeferredObject
 *  net.blay09.mods.balm.api.block.BalmBlockEntities
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  org.apache.commons.lang3.ArrayUtils
 */
package net.blay09.mods.waystones.block.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.waystones.block.ModBlocks;
import net.blay09.mods.waystones.block.entity.PortstoneBlockEntity;
import net.blay09.mods.waystones.block.entity.SharestoneBlockEntity;
import net.blay09.mods.waystones.block.entity.WarpPlateBlockEntity;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.lang3.ArrayUtils;

public class ModBlockEntities {
    public static DeferredObject<BlockEntityType<WaystoneBlockEntity>> waystone;
    public static DeferredObject<BlockEntityType<SharestoneBlockEntity>> sharestone;
    public static DeferredObject<BlockEntityType<WarpPlateBlockEntity>> warpPlate;
    public static DeferredObject<BlockEntityType<PortstoneBlockEntity>> portstone;

    public static void initialize(BalmBlockEntities blockEntities) {
        waystone = blockEntities.registerBlockEntity(ModBlockEntities.id("waystone"), WaystoneBlockEntity::new, () -> new Block[]{ModBlocks.waystone, ModBlocks.mossyWaystone, ModBlocks.sandyWaystone, ModBlocks.blackstoneWaystone, ModBlocks.endStoneWaystone, ModBlocks.deepslateWaystone});
        sharestone = blockEntities.registerBlockEntity(ModBlockEntities.id("sharestone"), SharestoneBlockEntity::new, () -> (Block[])ArrayUtils.add((Object[])ModBlocks.scopedSharestones, (Object)ModBlocks.sharestone));
        warpPlate = blockEntities.registerBlockEntity(ModBlockEntities.id("warp_plate"), WarpPlateBlockEntity::new, () -> new Block[]{ModBlocks.warpPlate});
        portstone = blockEntities.registerBlockEntity(ModBlockEntities.id("portstone"), PortstoneBlockEntity::new, () -> new Block[]{ModBlocks.portstone});
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation("waystones", name);
    }
}

