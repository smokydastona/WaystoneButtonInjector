/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.blay09.mods.balm.api.block.BalmBlocks
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.DyeColor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.SoundType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 */
package net.blay09.mods.waystones.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.waystones.block.PortstoneBlock;
import net.blay09.mods.waystones.block.SharestoneBlock;
import net.blay09.mods.waystones.block.WarpPlateBlock;
import net.blay09.mods.waystones.block.WaystoneBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    private static final DyeColor[] sharestoneColors = new DyeColor[]{DyeColor.WHITE, DyeColor.ORANGE, DyeColor.MAGENTA, DyeColor.LIGHT_BLUE, DyeColor.YELLOW, DyeColor.LIME, DyeColor.PINK, DyeColor.GRAY, DyeColor.LIGHT_GRAY, DyeColor.CYAN, DyeColor.PURPLE, DyeColor.BLUE, DyeColor.BROWN, DyeColor.GREEN, DyeColor.RED, DyeColor.BLACK};
    public static Block waystone;
    public static Block mossyWaystone;
    public static Block sandyWaystone;
    public static Block deepslateWaystone;
    public static Block blackstoneWaystone;
    public static Block endStoneWaystone;
    public static Block sharestone;
    public static Block warpPlate;
    public static Block portstone;
    public static Block[] scopedSharestones;

    public static void initialize(BalmBlocks blocks) {
        blocks.register(() -> {
            waystone = new WaystoneBlock(ModBlocks.defaultProperties());
            return waystone;
        }, () -> ModBlocks.itemBlock(waystone), ModBlocks.id("waystone"));
        blocks.register(() -> {
            mossyWaystone = new WaystoneBlock(ModBlocks.defaultProperties());
            return mossyWaystone;
        }, () -> ModBlocks.itemBlock(mossyWaystone), ModBlocks.id("mossy_waystone"));
        blocks.register(() -> {
            sandyWaystone = new WaystoneBlock(ModBlocks.defaultProperties());
            return sandyWaystone;
        }, () -> ModBlocks.itemBlock(sandyWaystone), ModBlocks.id("sandy_waystone"));
        blocks.register(() -> {
            deepslateWaystone = new WaystoneBlock(ModBlocks.defaultProperties().m_60918_(SoundType.f_154677_));
            return deepslateWaystone;
        }, () -> ModBlocks.itemBlock(deepslateWaystone), ModBlocks.id("deepslate_waystone"));
        blocks.register(() -> {
            blackstoneWaystone = new WaystoneBlock(ModBlocks.defaultProperties());
            return blackstoneWaystone;
        }, () -> ModBlocks.itemBlock(blackstoneWaystone), ModBlocks.id("blackstone_waystone"));
        blocks.register(() -> {
            endStoneWaystone = new WaystoneBlock(ModBlocks.defaultProperties());
            return endStoneWaystone;
        }, () -> ModBlocks.itemBlock(endStoneWaystone), ModBlocks.id("end_stone_waystone"));
        blocks.register(() -> {
            sharestone = new SharestoneBlock(ModBlocks.defaultProperties(), null);
            return sharestone;
        }, () -> ModBlocks.itemBlock(sharestone), ModBlocks.id("sharestone"));
        blocks.register(() -> {
            warpPlate = new WarpPlateBlock(ModBlocks.defaultProperties());
            return warpPlate;
        }, () -> ModBlocks.itemBlock(warpPlate), ModBlocks.id("warp_plate"));
        blocks.register(() -> {
            portstone = new PortstoneBlock(ModBlocks.defaultProperties());
            return portstone;
        }, () -> ModBlocks.itemBlock(portstone), ModBlocks.id("portstone"));
        for (DyeColor color : sharestoneColors) {
            blocks.register(() -> {
                SharestoneBlock sharestoneBlock = new SharestoneBlock(ModBlocks.defaultProperties(), color);
                ModBlocks.scopedSharestones[color.ordinal()] = sharestoneBlock;
                return sharestoneBlock;
            }, () -> ModBlocks.itemBlock(scopedSharestones[color.ordinal()]), ModBlocks.id(color.m_7912_() + "_sharestone"));
        }
    }

    private static BlockItem itemBlock(Block block) {
        return new BlockItem(block, Balm.getItems().itemProperties());
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation("waystones", name);
    }

    private static BlockBehaviour.Properties defaultProperties() {
        return Balm.getBlocks().blockProperties().m_60918_(SoundType.f_56742_).m_60913_(5.0f, 2000.0f);
    }

    static {
        scopedSharestones = new SharestoneBlock[sharestoneColors.length];
    }
}

