/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
 */
package net.blay09.mods.balm.forge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.event.client.DisconnectedFromServerEvent;
import net.blay09.mods.balm.forge.client.ForgeBalmClientRuntime;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ForgeBalmClient {
    public static void onInitializeClient(FMLClientSetupEvent setupEvent) {
        ((ForgeBalmClientRuntime)BalmClient.getRuntime()).initializeRuntime();
        Balm.getEvents().onEvent(DisconnectedFromServerEvent.class, event -> Balm.getConfig().resetToBackingConfigs());
    }
}

