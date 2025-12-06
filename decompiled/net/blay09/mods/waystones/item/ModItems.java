/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.DeferredObject
 *  net.blay09.mods.balm.api.item.BalmItems
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.CreativeModeTab
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 */
package net.blay09.mods.waystones.item;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.waystones.block.ModBlocks;
import net.blay09.mods.waystones.item.AttunedShardItem;
import net.blay09.mods.waystones.item.BoundScrollItem;
import net.blay09.mods.waystones.item.CrumblingAttunedShardItem;
import net.blay09.mods.waystones.item.ReturnScrollItem;
import net.blay09.mods.waystones.item.WarpDustItem;
import net.blay09.mods.waystones.item.WarpScrollItem;
import net.blay09.mods.waystones.item.WarpStoneItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ModItems {
    public static DeferredObject<CreativeModeTab> creativeModeTab;
    public static Item returnScroll;
    public static Item boundScroll;
    public static Item warpScroll;
    public static Item warpStone;
    public static Item warpDust;
    public static Item attunedShard;
    public static Item crumblingAttunedShard;

    public static void initialize(BalmItems items) {
        items.registerItem(() -> {
            returnScroll = new ReturnScrollItem(items.itemProperties());
            return returnScroll;
        }, ModItems.id("return_scroll"));
        items.registerItem(() -> {
            boundScroll = new BoundScrollItem(items.itemProperties());
            return boundScroll;
        }, ModItems.id("bound_scroll"));
        items.registerItem(() -> {
            warpScroll = new WarpScrollItem(items.itemProperties());
            return warpScroll;
        }, ModItems.id("warp_scroll"));
        items.registerItem(() -> {
            warpStone = new WarpStoneItem(items.itemProperties());
            return warpStone;
        }, ModItems.id("warp_stone"));
        items.registerItem(() -> {
            warpDust = new WarpDustItem(items.itemProperties());
            return warpDust;
        }, ModItems.id("warp_dust"));
        items.registerItem(() -> {
            attunedShard = new AttunedShardItem(items.itemProperties());
            return attunedShard;
        }, ModItems.id("attuned_shard"), null);
        items.registerItem(() -> {
            crumblingAttunedShard = new CrumblingAttunedShardItem(items.itemProperties());
            return crumblingAttunedShard;
        }, ModItems.id("crumbling_attuned_shard"), null);
        creativeModeTab = items.registerCreativeModeTab(ModItems.id("waystones"), () -> new ItemStack((ItemLike)ModBlocks.waystone));
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation("waystones", name);
    }
}

