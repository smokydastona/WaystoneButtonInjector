/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.biome.Biome
 */
package net.blay09.mods.balm.api.world;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

@FunctionalInterface
public interface BiomePredicate {
    public boolean test(ResourceLocation var1, Holder<Biome> var2);
}

