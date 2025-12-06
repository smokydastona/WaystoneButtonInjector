/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.world.entity.player.Player
 */
package net.blay09.mods.balm.common.config;

import java.util.Collection;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategory;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.balm.api.event.client.DisconnectedFromServerEvent;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.network.ClientboundConfigPacket;
import net.blay09.mods.balm.common.config.AbstractBalmConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

public class ConfigSync
implements BalmModule {
    public static boolean hasSyncedProperties(BalmConfigSchema schema) {
        return schema.rootProperties().stream().anyMatch(ConfiguredProperty::synced) || schema.categories().stream().anyMatch(ConfigSync::hasSyncedProperties);
    }

    public static boolean hasSyncedProperties(ConfigCategory category) {
        return category.properties().stream().anyMatch(ConfiguredProperty::synced);
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation("balm", "config_sync");
    }

    @Override
    public void registerNetworking(BalmNetworking networking) {
        networking.registerClientboundPacket(new ResourceLocation("balm", "config"), ClientboundConfigPacket.class, ClientboundConfigPacket::encode, ClientboundConfigPacket::decode, ClientboundConfigPacket::handle);
    }

    @Override
    public void registerEvents(BalmEvents events) {
        events.onEvent(PlayerLoginEvent.class, event -> {
            Collection<BalmConfigSchema> schemas = Balm.getConfig().getSchemas();
            for (BalmConfigSchema schema : schemas) {
                LoadedConfig loaded;
                if (!ConfigSync.hasSyncedProperties(schema) || (loaded = Balm.getConfig().getActiveConfig(schema)) == null) continue;
                ClientboundConfigPacket packet = new ClientboundConfigPacket(schema, loaded);
                Balm.getNetworking().sendTo((Player)event.getPlayer(), packet);
            }
        });
        events.onEvent(ConfigReloadedEvent.class, event -> {
            LoadedConfig loaded;
            BalmConfigSchema schema;
            MinecraftServer server = Balm.getHooks().getServer();
            if (server != null && (schema = event.getSchema()) != null && ConfigSync.hasSyncedProperties(schema) && (loaded = Balm.getConfig().getActiveConfig(schema)) != null) {
                ClientboundConfigPacket packet = new ClientboundConfigPacket(schema, loaded);
                Balm.getNetworking().sendToAll(server, packet);
            }
        });
        events.onEvent(DisconnectedFromServerEvent.class, event -> {
            BalmConfig config = Balm.getConfig();
            if (config instanceof AbstractBalmConfig) {
                AbstractBalmConfig abstractBalmConfig = (AbstractBalmConfig)config;
                abstractBalmConfig.resetToLocalConfig();
            }
        });
    }
}

