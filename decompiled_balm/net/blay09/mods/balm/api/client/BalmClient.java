/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.packs.resources.PreparableReloadListener
 *  net.minecraft.world.entity.player.Player
 */
package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.client.BalmClientRuntime;
import net.blay09.mods.balm.api.client.BalmClientRuntimeSpi;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.entity.player.Player;

public class BalmClient {
    private static final BalmClientRuntime runtime = BalmClientRuntimeSpi.create();

    public static void registerModule(BalmClientModule module) {
        runtime.registerModule(module);
    }

    public static void onRuntimeAvailable(Runnable callback) {
        runtime.onRuntimeAvailable(callback);
    }

    @Deprecated(forRemoval=true, since="1.21.1")
    public static void initialize(String modId, Runnable initializer) {
        BalmClient.initializeMod(modId, EmptyLoadContext.INSTANCE, initializer);
    }

    @Deprecated(forRemoval=true, since="1.21.1")
    public static void initialize(String modId) {
        BalmClient.initialize(modId, () -> {});
    }

    @Deprecated(forRemoval=true, since="1.21.1")
    public static Player getClientPlayer() {
        return Minecraft.m_91087_().f_91074_;
    }

    @Deprecated(forRemoval=true, since="1.22")
    public static <T extends BalmRuntimeLoadContext> void initialize(String modId, T context, Runnable initializer) {
        BalmClient.initializeMod(modId, context, initializer);
    }

    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, Runnable initializer) {
        runtime.initializeMod(modId, context, initializer);
    }

    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, BalmClientModule module) {
        runtime.initializeMod(modId, context, () -> BalmClient.registerModule(module));
    }

    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, BalmClientModule ... modules) {
        runtime.initializeMod(modId, context, () -> {
            for (BalmClientModule module : modules) {
                BalmClient.registerModule(module);
            }
        });
    }

    public static BalmRenderers getRenderers() {
        return runtime.getRenderers();
    }

    public static BalmKeyMappings getKeyMappings() {
        return runtime.getKeyMappings();
    }

    public static BalmScreens getScreens() {
        return runtime.getScreens();
    }

    public static BalmModels getModels() {
        return runtime.getModels();
    }

    public static void addResourceReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        runtime.addResourceReloadListener(identifier, reloadListener);
    }

    public static BalmClientRuntime getRuntime() {
        return runtime;
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    public static BalmTextures getTextures() {
        return runtime.getTextures();
    }
}

