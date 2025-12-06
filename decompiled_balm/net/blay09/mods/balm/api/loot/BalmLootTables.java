/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.balm.api.loot;

import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.minecraft.resources.ResourceLocation;

public interface BalmLootTables {
    public void registerLootModifier(ResourceLocation var1, BalmLootModifier var2);
}

