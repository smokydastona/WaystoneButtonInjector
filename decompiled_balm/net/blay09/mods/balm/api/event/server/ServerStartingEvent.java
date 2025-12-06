/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.MinecraftServer
 */
package net.blay09.mods.balm.api.event.server;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.server.MinecraftServer;

public class ServerStartingEvent
extends BalmEvent {
    private final MinecraftServer server;

    public ServerStartingEvent(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return this.server;
    }
}

