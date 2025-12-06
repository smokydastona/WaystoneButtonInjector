/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package net.blay09.mods.balm.api.event.client;

import net.minecraft.client.Minecraft;

@FunctionalInterface
public interface ClientTickHandler {
    public void handle(Minecraft var1);
}

