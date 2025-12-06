/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ArrayListMultimap
 *  com.google.common.collect.Multimap
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.balm.common.config;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.reflection.LoadedReflectionConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategory;
import net.blay09.mods.balm.api.network.ConfigReflection;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractBalmConfig
implements BalmConfig {
    private final Map<ResourceLocation, BalmConfigSchema> schemas = new ConcurrentHashMap<ResourceLocation, BalmConfigSchema>();
    private final Map<ResourceLocation, MutableLoadedConfig> localConfigs = new ConcurrentHashMap<ResourceLocation, MutableLoadedConfig>();
    private final Map<ResourceLocation, LoadedConfig> activeConfigs = new ConcurrentHashMap<ResourceLocation, LoadedConfig>();
    private final Map<ResourceLocation, Object> activeReflectionConfigs = new ConcurrentHashMap<ResourceLocation, Object>();
    private final Multimap<ResourceLocation, Consumer<MutableLoadedConfig>> configLoadHandlers = ArrayListMultimap.create();

    @Override
    public void registerConfig(BalmConfigSchema schema) {
        this.schemas.put(schema.identifier(), schema);
    }

    @Override
    public BalmConfigSchema getSchema(ResourceLocation identifier) {
        return this.schemas.get(identifier);
    }

    @Override
    public MutableLoadedConfig getLocalConfig(ResourceLocation identifier) {
        return this.localConfigs.get(identifier);
    }

    @Override
    public <T> void updateLocalConfig(Class<T> configDataClass, Consumer<T> updater) {
        BalmConfigSchema schema = this.getSchema(configDataClass);
        MutableLoadedConfig localConfig = this.getLocalConfig(schema);
        LoadedReflectionConfig<T> reflectionConfig = ConfigReflection.of(configDataClass, localConfig);
        updater.accept(reflectionConfig.data());
        this.saveLocalConfig(schema, reflectionConfig);
    }

    @Override
    public LoadedConfig getActiveConfig(ResourceLocation identifier) {
        return this.activeConfigs.get(identifier);
    }

    @Override
    public Collection<BalmConfigSchema> getSchemasByNamespace(String namespace) {
        return this.schemas.values().stream().filter(schema -> schema.identifier().m_135827_().equals(namespace)).toList();
    }

    @Override
    public Collection<BalmConfigSchema> getSchemas() {
        return this.schemas.values();
    }

    @Override
    public <T> T getActiveConfig(Class<T> configDataClass) {
        ResourceLocation identifier = ConfigReflection.getIdentifier(configDataClass);
        return (T)this.activeReflectionConfigs.computeIfAbsent(identifier, it -> BalmConfig.super.getActiveConfig(configDataClass));
    }

    @Override
    public void saveLocalConfig(BalmConfigSchema schema, MutableLoadedConfig config) {
        this.activeReflectionConfigs.remove(schema.identifier());
        this.localConfigs.put(schema.identifier(), config);
        this.updateActiveFromLocal(schema, config);
    }

    protected void updateActiveFromLocal(BalmConfigSchema schema, MutableLoadedConfig config) {
        MutableLoadedConfig newConfig = config.copy();
        if (Balm.getProxy().isConnected() && !Balm.getProxy().isLocalServer()) {
            LoadedConfig activeConfig = this.activeConfigs.get(schema.identifier());
            for (ConfiguredProperty<?> rootProperty : schema.rootProperties()) {
                if (!rootProperty.synced()) continue;
                newConfig.setRaw(rootProperty, activeConfig.getRaw(rootProperty));
            }
            for (ConfigCategory category : schema.categories()) {
                for (ConfiguredProperty<?> property : category.properties()) {
                    if (!property.synced()) continue;
                    newConfig.setRaw(property, activeConfig.getRaw(property));
                }
            }
        }
        this.setActiveConfig(schema, newConfig);
    }

    protected void setLocalConfig(BalmConfigSchema schema, MutableLoadedConfig config) {
        this.localConfigs.put(schema.identifier(), config);
    }

    public void setActiveConfig(BalmConfigSchema schema, LoadedConfig config) {
        this.activeReflectionConfigs.remove(schema.identifier());
        this.activeConfigs.put(schema.identifier(), config);
    }

    public void resetToLocalConfig() {
        this.activeReflectionConfigs.clear();
        this.activeConfigs.putAll(this.localConfigs);
    }

    @Override
    public void onConfigAvailable(BalmConfigSchema schema, Consumer<MutableLoadedConfig> handler) {
        MutableLoadedConfig loaded = this.getLocalConfig(schema);
        if (loaded != null) {
            handler.accept(loaded);
        } else {
            this.configLoadHandlers.put((Object)schema.identifier(), handler);
        }
    }

    protected void fireConfigLoadHandlers(BalmConfigSchema schema, MutableLoadedConfig config) {
        this.configLoadHandlers.get((Object)schema.identifier()).forEach(handler -> handler.accept(config));
    }
}

