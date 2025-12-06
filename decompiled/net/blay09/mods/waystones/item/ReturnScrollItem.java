/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.item;

import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.item.BoundScrollItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ReturnScrollItem
extends BoundScrollItem {
    public ReturnScrollItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    protected IWaystone getBoundTo(Player player, ItemStack itemStack) {
        return PlayerWaystoneManager.getNearestWaystone(player);
    }

    @Override
    protected WarpMode getWarpMode() {
        return WarpMode.RETURN_SCROLL;
    }
}

