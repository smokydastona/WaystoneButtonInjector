/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.packs.resources.PreparableReloadListener
 *  net.minecraft.server.packs.resources.ResourceManager
 */
package net.blay09.mods.balm.api;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.BalmProxy;
import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.provider.BalmProviders;
import net.blay09.mods.balm.api.proxy.ModProxy;
import net.blay09.mods.balm.api.proxy.PlatformProxy;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

public interface BalmRuntime {
    public BalmConfig getConfig();

    public BalmEvents getEvents();

    public BalmWorldGen getWorldGen();

    public BalmBlocks getBlocks();

    public BalmBlockEntities getBlockEntities();

    public BalmItems getItems();

    public BalmMenus getMenus();

    public BalmNetworking getNetworking();

    public BalmHooks getHooks();

    public BalmRegistries getRegistries();

    public BalmSounds getSounds();

    public BalmEntities getEntities();

    public BalmCapabilities getCapabilities();

    @Deprecated(forRemoval=true, since="1.21.5")
    public BalmProviders getProviders();

    public BalmCommands getCommands();

    public BalmLootTables getLootTables();

    public BalmStats getStats();

    public BalmRecipes getRecipes();

    public BalmModSupport getModSupport();

    public BalmParticles getParticles();

    public BalmPermissions getPermissions();

    public boolean isModLoaded(String var1);

    public String getModName(String var1);

    public <T> SidedProxy<T> sidedProxy(String var1, String var2);

    public void initializeMod(String var1, BalmRuntimeLoadContext var2, Runnable var3);

    public void initializeIfLoaded(String var1, String var2);

    public void addServerReloadListener(ResourceLocation var1, PreparableReloadListener var2);

    public void addServerReloadListener(ResourceLocation var1, Consumer<ResourceManager> var2);

    public <T> PlatformProxy<T> platformProxy();

    public <T> ModProxy<T> modProxy();

    public String getPlatform();

    default public void initializeModule(BalmModule module) {
        String modId = module.getId().m_135827_();
        module.registerConfig(this.getConfig());
        module.registerAdditional(this.getRegistries());
        module.registerBlocks(this.getBlocks().scoped(modId));
        module.registerBlockEntities(this.getBlockEntities());
        module.registerItems(this.getItems().scoped(modId));
        module.registerEntities(this.getEntities());
        module.registerWorldGen(this.getWorldGen());
        module.registerNetworking(this.getNetworking());
        module.registerMenus(this.getMenus());
        module.registerCapabilities(this.getCapabilities());
        module.registerCommands(this.getCommands());
        module.registerRecipes(this.getRecipes());
        module.registerLootTables(this.getLootTables());
        module.registerStats(this.getStats());
        module.registerSounds(this.getSounds());
        module.registerPermissions(this.getPermissions());
        module.registerParticles(this.getParticles());
        module.registerEvents(this.getEvents());
        module.initialize();
    }

    public BalmProxy getProxy();

    public boolean isReady();

    public void onRuntimeAvailable(Runnable var1);

    public void registerModule(BalmModule var1);

    public BalmEnvironment getEnvironment();

    public boolean isDevelopmentEnvironment();

    public Map<String, Path> lookupAllModPaths(String var1);

    public Optional<Path> lookupModPath(String var1, String var2);
}

