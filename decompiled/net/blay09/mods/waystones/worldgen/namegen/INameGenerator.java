/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.RandomSource
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.worldgen.namegen;

import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public interface INameGenerator {
    @Nullable
    public String randomName(RandomSource var1);
}

