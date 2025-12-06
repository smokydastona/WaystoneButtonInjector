/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.balm.api.client.module;

import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.minecraft.resources.ResourceLocation;

public interface BalmClientModule {
    public ResourceLocation getId();

    default public void registerEvents(BalmEvents events) {
    }

    default public void registerModels(BalmModels models) {
    }

    default public void registerRenderers(BalmRenderers renderers) {
    }

    default public void registerScreens(BalmScreens screens) {
    }

    default public void registerKeyMappings(BalmKeyMappings keyMappings) {
    }

    default public void initialize() {
    }
}

