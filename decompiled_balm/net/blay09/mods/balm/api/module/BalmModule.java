/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.balm.api.module;

import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.minecraft.resources.ResourceLocation;

public interface BalmModule {
    public ResourceLocation getId();

    default public void registerStats(BalmStats stats) {
    }

    default public void registerSounds(BalmSounds sounds) {
    }

    default public void registerParticles(BalmParticles particles) {
    }

    default public void registerMenus(BalmMenus menus) {
    }

    default public void registerRecipes(BalmRecipes recipes) {
    }

    default public void registerCommands(BalmCommands commands) {
    }

    default public void registerEntities(BalmEntities entities) {
    }

    default public void registerLootTables(BalmLootTables lootTables) {
    }

    default public void registerItems(BalmItems items) {
    }

    default public void registerBlockEntities(BalmBlockEntities blockEntities) {
    }

    default public void registerWorldGen(BalmWorldGen worldGen) {
    }

    default public void registerNetworking(BalmNetworking networking) {
    }

    default public void registerCapabilities(BalmCapabilities capabilities) {
    }

    default public void registerPermissions(BalmPermissions permissions) {
    }

    default public void registerConfig(BalmConfig config) {
    }

    default public void registerBlocks(BalmBlocks blocks) {
    }

    default public void registerEvents(BalmEvents events) {
    }

    default public void registerAdditional(BalmRegistries registries) {
    }

    default public void initialize() {
    }
}

