/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.player.Inventory
 */
package net.blay09.mods.balm.api.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

@FunctionalInterface
public interface BalmScreenFactory<T, S> {
    public S create(T var1, Inventory var2, Component var3);
}

