/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 */
package net.blay09.mods.balm.api.event;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.server.level.ServerPlayer;

public class PlayerRespawnEvent
extends BalmEvent {
    private final ServerPlayer oldPlayer;
    private final ServerPlayer newPlayer;

    public PlayerRespawnEvent(ServerPlayer oldPlayer, ServerPlayer newPlayer) {
        this.oldPlayer = oldPlayer;
        this.newPlayer = newPlayer;
    }

    public ServerPlayer getOldPlayer() {
        return this.oldPlayer;
    }

    public ServerPlayer getNewPlayer() {
        return this.newPlayer;
    }
}

