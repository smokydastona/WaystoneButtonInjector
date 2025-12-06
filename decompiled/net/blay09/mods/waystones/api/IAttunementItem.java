/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.api;

import net.blay09.mods.waystones.api.IWaystone;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IAttunementItem {
    @Nullable
    public IWaystone getWaystoneAttunedTo(@Nullable MinecraftServer var1, ItemStack var2);

    public void setWaystoneAttunedTo(ItemStack var1, @Nullable IWaystone var2);
}

