/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 *  net.minecraft.SharedConstants
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.packs.resources.PreparableReloadListener
 *  net.minecraft.server.packs.resources.ResourceManager
 *  net.minecraft.server.packs.resources.ResourceManagerReloadListener
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.eventbus.api.IEventBus
 *  net.minecraftforge.fml.ModList
 *  net.minecraftforge.fml.ModLoadingContext
 *  net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
 *  net.minecraftforge.fml.loading.FMLEnvironment
 *  net.minecraftforge.forgespi.language.IModFileInfo
 */
package net.blay09.mods.balm.forge;

import com.mojang.datafixers.util.Pair;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.balm.api.BalmHooks;
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
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.provider.BalmProviders;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.CommonBalmLootTables;
import net.blay09.mods.balm.common.CommonBalmRuntime;
import net.blay09.mods.balm.common.LegacyNamespaceResolver;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.blay09.mods.balm.forge.ForgeBalmHooks;
import net.blay09.mods.balm.forge.ForgeBalmRegistries;
import net.blay09.mods.balm.forge.ForgeLoadContext;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.blay09.mods.balm.forge.block.ForgeBalmBlocks;
import net.blay09.mods.balm.forge.block.entity.ForgeBalmBlockEntities;
import net.blay09.mods.balm.forge.capability.ForgeBalmCapabilities;
import net.blay09.mods.balm.forge.command.ForgeBalmCommands;
import net.blay09.mods.balm.forge.compat.ForgeBalmModSupport;
import net.blay09.mods.balm.forge.config.ForgeBalmConfig;
import net.blay09.mods.balm.forge.entity.ForgeBalmEntities;
import net.blay09.mods.balm.forge.event.ForgeBalmCommonEvents;
import net.blay09.mods.balm.forge.event.ForgeBalmEvents;
import net.blay09.mods.balm.forge.item.ForgeBalmItems;
import net.blay09.mods.balm.forge.menu.ForgeBalmMenus;
import net.blay09.mods.balm.forge.network.ForgeBalmNetworking;
import net.blay09.mods.balm.forge.particle.ForgeBalmParticles;
import net.blay09.mods.balm.forge.permission.ForgeBalmPermissions;
import net.blay09.mods.balm.forge.provider.ForgeBalmProviders;
import net.blay09.mods.balm.forge.recipe.ForgeBalmRecipes;
import net.blay09.mods.balm.forge.sound.ForgeBalmSounds;
import net.blay09.mods.balm.forge.stats.ForgeBalmStats;
import net.blay09.mods.balm.forge.world.ForgeBalmWorldGen;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.language.IModFileInfo;

public class ForgeBalmRuntime
extends CommonBalmRuntime {
    private final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> ModLoadingContext.get().getActiveNamespace());
    private final BalmWorldGen worldGen = new ForgeBalmWorldGen();
    private final BalmItems items = new ForgeBalmItems(this.legacyNamespaceResolver);
    private final BalmBlocks blocks = new ForgeBalmBlocks(this.legacyNamespaceResolver, this.items);
    private final BalmBlockEntities blockEntities = new ForgeBalmBlockEntities();
    private final ForgeBalmEvents events = new ForgeBalmEvents();
    private final BalmMenus menus = new ForgeBalmMenus();
    private final BalmNetworking networking = new ForgeBalmNetworking();
    private final BalmConfig config = new ForgeBalmConfig();
    private final BalmHooks hooks = new ForgeBalmHooks();
    private final BalmRegistries registries = new ForgeBalmRegistries();
    private final BalmSounds sounds = new ForgeBalmSounds();
    private final BalmEntities entities = new ForgeBalmEntities(this.legacyNamespaceResolver);
    private final BalmCapabilities capabilities = new ForgeBalmCapabilities(this.legacyNamespaceResolver);
    @Deprecated(forRemoval=true, since="1.21.5")
    private final BalmProviders providers = new ForgeBalmProviders();
    private final BalmCommands commands = new ForgeBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    private final BalmStats stats = new ForgeBalmStats(this.legacyNamespaceResolver);
    private final BalmRecipes recipes = new ForgeBalmRecipes();
    private final BalmModSupport modSupport = new ForgeBalmModSupport(this);
    private final BalmParticles particles = new ForgeBalmParticles();
    private final BalmPermissions permissions = new ForgeBalmPermissions();

    public ForgeBalmRuntime() {
        ForgeBalmCommonEvents.registerEvents(this.events);
    }

    @Override
    public BalmConfig getConfig() {
        return this.config;
    }

    @Override
    public BalmEvents getEvents() {
        return this.events;
    }

    @Override
    public BalmWorldGen getWorldGen() {
        return this.worldGen;
    }

    @Override
    public BalmBlocks getBlocks() {
        return this.blocks;
    }

    @Override
    public BalmBlockEntities getBlockEntities() {
        return this.blockEntities;
    }

    @Override
    public BalmItems getItems() {
        return this.items;
    }

    @Override
    public BalmMenus getMenus() {
        return this.menus;
    }

    @Override
    public BalmNetworking getNetworking() {
        return this.networking;
    }

    @Override
    public BalmHooks getHooks() {
        return this.hooks;
    }

    @Override
    public BalmRegistries getRegistries() {
        return this.registries;
    }

    @Override
    public BalmSounds getSounds() {
        return this.sounds;
    }

    @Override
    public BalmEntities getEntities() {
        return this.entities;
    }

    @Override
    public BalmCapabilities getCapabilities() {
        return this.capabilities;
    }

    @Override
    @Deprecated(forRemoval=true, since="1.21.5")
    public BalmProviders getProviders() {
        return this.providers;
    }

    @Override
    public BalmCommands getCommands() {
        return this.commands;
    }

    @Override
    public BalmLootTables getLootTables() {
        return this.lootTables;
    }

    @Override
    public BalmStats getStats() {
        return this.stats;
    }

    @Override
    public BalmRecipes getRecipes() {
        return this.recipes;
    }

    @Override
    public BalmPermissions getPermissions() {
        return this.permissions;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public String getModName(String modId) {
        return ModList.get().getModContainerById(modId).map(it -> it.getModInfo().getDisplayName()).orElse(modId);
    }

    @Override
    public void initializeMod(String modId, BalmRuntimeLoadContext context, Runnable initializer) {
        ForgeLoadContext forgeLoadContext = context instanceof ForgeLoadContext ? (ForgeLoadContext)context : new ForgeLoadContext(FMLJavaModLoadingContext.get().getModEventBus());
        BalmLoadContexts.register(modId, forgeLoadContext);
        initializer.run();
        IEventBus modEventBus = forgeLoadContext.modEventBus();
        DeferredRegisters.register(modId, modEventBus);
        ModBusEventRegisters.register(modId, modEventBus);
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        MinecraftForge.EVENT_BUS.addListener(event -> event.addListener(reloadListener));
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        MinecraftForge.EVENT_BUS.addListener(event -> event.addListener((PreparableReloadListener)((ResourceManagerReloadListener)reloadListener::accept)));
    }

    @Override
    public BalmModSupport getModSupport() {
        return this.modSupport;
    }

    @Override
    public BalmParticles getParticles() {
        return this.particles;
    }

    @Override
    public String getPlatform() {
        return "forge";
    }

    @Override
    public void initializeRuntime() {
        MinecraftForge.EVENT_BUS.register((Object)this.capabilities);
        super.initializeRuntime();
    }

    @Override
    public BalmEnvironment getEnvironment() {
        return switch (FMLEnvironment.dist) {
            default -> throw new IncompatibleClassChangeError();
            case Dist.CLIENT -> BalmEnvironment.CLIENT;
            case Dist.DEDICATED_SERVER -> BalmEnvironment.SERVER;
        };
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return SharedConstants.f_136183_;
    }

    @Override
    public Map<String, Path> lookupAllModPaths(String path) {
        return ModList.get().getMods().stream().map(it -> new Pair((Object)it.getModId(), (Object)it.getOwningFile().getFile().findResource(new String[]{path}))).filter(it -> Files.exists((Path)it.getSecond(), new LinkOption[0])).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    @Override
    public Optional<Path> lookupModPath(String modId, String path) {
        IModFileInfo modFile = ModList.get().getModFileById(modId);
        Path nioPath = modFile.getFile().findResource(new String[]{path});
        return Files.exists(nioPath, new LinkOption[0]) ? Optional.of(nioPath) : Optional.empty();
    }
}

