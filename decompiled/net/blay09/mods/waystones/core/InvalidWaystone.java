/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.core;

import java.util.UUID;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class InvalidWaystone
implements IWaystone {
    public static final IWaystone INSTANCE = new InvalidWaystone();

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    @Nullable
    public UUID getOwnerUid() {
        return null;
    }

    @Override
    public UUID getWaystoneUid() {
        return UUID.randomUUID();
    }

    @Override
    public String getName() {
        return "invalid";
    }

    @Override
    public ResourceKey<Level> getDimension() {
        return Level.f_46428_;
    }

    @Override
    public WaystoneOrigin getOrigin() {
        return WaystoneOrigin.UNKNOWN;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

    @Override
    public boolean isOwner(Player player) {
        return false;
    }

    @Override
    public BlockPos getPos() {
        return BlockPos.f_121853_;
    }

    @Override
    public ResourceLocation getWaystoneType() {
        return new ResourceLocation("waystones", "invalid");
    }
}

