/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.level.Level
 */
package net.blay09.mods.balm.api.event;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class PlayerChangedDimensionEvent
extends BalmEvent {
    private final ServerPlayer player;
    private final ResourceKey<Level> fromDim;
    private final ResourceKey<Level> toDim;

    public PlayerChangedDimensionEvent(ServerPlayer player, ResourceKey<Level> fromDim, ResourceKey<Level> toDim) {
        this.player = player;
        this.fromDim = fromDim;
        this.toDim = toDim;
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }

    public ResourceKey<Level> getFromDim() {
        return this.fromDim;
    }

    public ResourceKey<Level> getToDim() {
        return this.toDim;
    }
}

