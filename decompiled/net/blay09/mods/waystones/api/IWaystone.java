/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.api;

import java.util.UUID;
import net.blay09.mods.waystones.api.TeleportDestination;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface IWaystone {
    public UUID getWaystoneUid();

    public String getName();

    public ResourceKey<Level> getDimension();

    default public boolean wasGenerated() {
        return this.getOrigin() == WaystoneOrigin.VILLAGE || this.getOrigin() == WaystoneOrigin.WILDERNESS || this.getOrigin() == WaystoneOrigin.DUNGEON;
    }

    public WaystoneOrigin getOrigin();

    public boolean isGlobal();

    public boolean isOwner(Player var1);

    public BlockPos getPos();

    public boolean isValid();

    @Nullable
    public UUID getOwnerUid();

    public ResourceLocation getWaystoneType();

    default public boolean hasName() {
        return !this.getName().isEmpty();
    }

    default public boolean hasOwner() {
        return this.getOwnerUid() != null;
    }

    default public boolean isValidInLevel(ServerLevel level) {
        return false;
    }

    default public TeleportDestination resolveDestination(ServerLevel level) {
        return new TeleportDestination(level, this.getPos().m_252807_(), Direction.NORTH);
    }
}

