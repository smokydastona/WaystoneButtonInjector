/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.api.provider;

import net.blay09.mods.balm.api.Balm;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

@Deprecated(forRemoval=true, since="1.21.5")
public class ProviderUtils {
    @Deprecated(forRemoval=true, since="1.21.5")
    @Nullable
    public static <T> T getProvider(BlockEntity blockEntity, Class<T> clazz) {
        return Balm.getProviders().getProvider(blockEntity, clazz);
    }
}

