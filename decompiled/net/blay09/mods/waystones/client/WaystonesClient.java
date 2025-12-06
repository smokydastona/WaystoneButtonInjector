/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.client.BalmClient
 *  net.minecraft.client.ClientBrandRetriever
 */
package net.blay09.mods.waystones.client;

import java.util.Locale;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.waystones.client.InventoryButtonGuiHandler;
import net.blay09.mods.waystones.client.ModClientEventHandlers;
import net.blay09.mods.waystones.client.ModRenderers;
import net.blay09.mods.waystones.client.ModScreens;
import net.blay09.mods.waystones.client.ModTextures;
import net.blay09.mods.waystones.compat.Compat;
import net.minecraft.client.ClientBrandRetriever;

public class WaystonesClient {
    public static void initialize() {
        ModClientEventHandlers.initialize();
        ModRenderers.initialize(BalmClient.getRenderers());
        ModScreens.initialize(BalmClient.getScreens());
        ModTextures.initialize(BalmClient.getTextures());
        InventoryButtonGuiHandler.initialize();
        Compat.isVivecraftInstalled = ClientBrandRetriever.getClientModName().toLowerCase(Locale.ENGLISH).contains("vivecraft");
    }
}

