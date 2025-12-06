/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.Minecraft;

public class ConnectedToServerEvent
extends BalmEvent {
    private final Minecraft client;

    public ConnectedToServerEvent(Minecraft client) {
        this.client = client;
    }

    public Minecraft getClient() {
        return this.client;
    }
}

