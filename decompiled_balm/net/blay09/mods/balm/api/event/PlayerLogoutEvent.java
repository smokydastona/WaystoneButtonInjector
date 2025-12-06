/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 */
package net.blay09.mods.balm.api.event;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.server.level.ServerPlayer;

public class PlayerLogoutEvent
extends BalmEvent {
    private final ServerPlayer player;

    public PlayerLogoutEvent(ServerPlayer player) {
        this.player = player;
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }
}

