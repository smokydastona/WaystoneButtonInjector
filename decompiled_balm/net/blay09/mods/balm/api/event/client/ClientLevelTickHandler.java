/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 */
package net.blay09.mods.balm.api.event.client;

import net.minecraft.client.multiplayer.ClientLevel;

@FunctionalInterface
public interface ClientLevelTickHandler {
    public void handle(ClientLevel var1);
}

