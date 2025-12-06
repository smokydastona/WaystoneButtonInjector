/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.waystones.api;

import net.minecraft.resources.ResourceLocation;

public class WaystoneStyle {
    private final ResourceLocation blockRegistryName;
    private int runeColor = -1;

    public WaystoneStyle(ResourceLocation blockRegistryName) {
        this.blockRegistryName = blockRegistryName;
    }

    public ResourceLocation getBlockRegistryName() {
        return this.blockRegistryName;
    }

    public int getRuneColor() {
        return this.runeColor;
    }

    public WaystoneStyle withRuneColor(int runeColor) {
        this.runeColor = runeColor;
        return this;
    }
}

