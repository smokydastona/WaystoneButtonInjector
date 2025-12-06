/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.packs.resources.PreparableReloadListener
 *  net.minecraft.server.packs.resources.ReloadableResourceManager
 *  net.minecraft.server.packs.resources.ResourceManager
 *  net.minecraftforge.eventbus.api.IEventBus
 *  net.minecraftforge.fml.ModLoadingContext
 *  net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
 */
package net.blay09.mods.balm.forge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.LegacyNamespaceResolver;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.client.CommonBalmClientRuntime;
import net.blay09.mods.balm.forge.ForgeLoadContext;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.blay09.mods.balm.forge.client.keymappings.ForgeBalmKeyMappings;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmModels;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmRenderers;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmTextures;
import net.blay09.mods.balm.forge.client.screen.ForgeBalmScreens;
import net.blay09.mods.balm.forge.event.ForgeBalmClientEvents;
import net.blay09.mods.balm.forge.event.ForgeBalmEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ForgeBalmClientRuntime
extends CommonBalmClientRuntime<ForgeLoadContext> {
    private final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> ModLoadingContext.get().getActiveNamespace());
    private final BalmRenderers renderers = new ForgeBalmRenderers(this.legacyNamespaceResolver);
    private final BalmScreens screens = new ForgeBalmScreens(this.legacyNamespaceResolver);
    private final BalmKeyMappings keyMappings = new ForgeBalmKeyMappings(this.legacyNamespaceResolver);
    private final BalmModels models = new ForgeBalmModels(this.legacyNamespaceResolver);
    @Deprecated(forRemoval=true, since="1.21.5")
    private final BalmTextures textures = new ForgeBalmTextures();

    public ForgeBalmClientRuntime() {
        ForgeBalmClientEvents.registerEvents((ForgeBalmEvents)Balm.getEvents());
    }

    @Override
    public BalmRenderers getRenderers() {
        return this.renderers;
    }

    @Override
    @Deprecated(forRemoval=true, since="1.21.5")
    public BalmTextures getTextures() {
        return this.textures;
    }

    @Override
    public BalmScreens getScreens() {
        return this.screens;
    }

    @Override
    public BalmModels getModels() {
        return this.models;
    }

    @Override
    public BalmKeyMappings getKeyMappings() {
        return this.keyMappings;
    }

    @Override
    public void initializeMod(String modId, BalmRuntimeLoadContext context, Runnable initializer) {
        ForgeLoadContext forgeLoadContext = context instanceof ForgeLoadContext ? (ForgeLoadContext)context : new ForgeLoadContext(FMLJavaModLoadingContext.get().getModEventBus());
        BalmLoadContexts.register(modId, forgeLoadContext);
        initializer.run();
        IEventBus modEventBus = forgeLoadContext.modEventBus();
        ModBusEventRegisters.register(modId, modEventBus);
    }

    @Override
    public void addResourceReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        ResourceManager resourceManager = Minecraft.m_91087_().m_91098_();
        if (resourceManager instanceof ReloadableResourceManager) {
            ReloadableResourceManager reloadableResourceManager = (ReloadableResourceManager)resourceManager;
            reloadableResourceManager.m_7217_(reloadListener);
        }
    }
}

