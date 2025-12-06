/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.packs.resources.PreparableReloadListener
 */
package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface BalmClientRuntime {
    public BalmRenderers getRenderers();

    public BalmScreens getScreens();

    public BalmModels getModels();

    public BalmKeyMappings getKeyMappings();

    public void initializeMod(String var1, BalmRuntimeLoadContext var2, Runnable var3);

    default public void initializeModule(BalmClientModule module) {
        String modId = module.getId().m_135827_();
        module.registerEvents(Balm.getEvents());
        module.registerRenderers(this.getRenderers().scoped(modId));
        module.registerScreens(this.getScreens().scoped(modId));
        module.registerModels(this.getModels().scoped(modId));
        module.registerKeyMappings(this.getKeyMappings().scoped(modId));
        module.initialize();
    }

    public boolean isReady();

    public void onRuntimeAvailable(Runnable var1);

    public void registerModule(BalmClientModule var1);

    public void addResourceReloadListener(ResourceLocation var1, PreparableReloadListener var2);

    @Deprecated(forRemoval=true, since="1.21.5")
    public BalmTextures getTextures();
}

