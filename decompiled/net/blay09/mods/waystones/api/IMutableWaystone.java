/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.level.Level
 */
package net.blay09.mods.waystones.api;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface IMutableWaystone {
    public void setName(String var1);

    public void setGlobal(boolean var1);

    public void setDimension(ResourceKey<Level> var1);

    public void setPos(BlockPos var1);

    public void setOwnerUid(UUID var1);
}

