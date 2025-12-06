/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.block.entity.BlockEntity
 */
package net.blay09.mods.balm.api.provider;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

@Deprecated(forRemoval=true, since="1.21.5")
public interface BalmProviders {
    public <T> T getProvider(BlockEntity var1, Class<T> var2);

    public <T> T getProvider(BlockEntity var1, Direction var2, Class<T> var3);

    public <T> T getProvider(Entity var1, Class<T> var2);
}

