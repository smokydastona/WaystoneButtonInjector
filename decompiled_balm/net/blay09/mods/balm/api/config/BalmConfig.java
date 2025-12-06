/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.Table
 *  net.minecraft.ResourceLocationException
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 */
package net.blay09.mods.balm.api.config;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.BalmConfigProperty;
import net.blay09.mods.balm.api.config.BalmConfigPropertyImpl;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.reflection.LoadedReflectionConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.network.ConfigReflection;
import net.blay09.mods.balm.api.network.SyncConfigMessage;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface BalmConfig {
    @Deprecated(forRemoval=true, since="1.21.5")
    private static BalmConfigProperty<?> createConfigProperty(BalmConfigData configData, Field categoryField, Field propertyField, BalmConfigData defaultConfig) {
        return new BalmConfigPropertyImpl(configData, categoryField, propertyField, defaultConfig);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    private static boolean isPropertyType(Class<?> type) {
        return type.isPrimitive() || type == String.class || type == Integer.class || type == Boolean.class || type == Float.class || type == Double.class || type == List.class || type == Set.class || type == ResourceLocationException.class || Enum.class.isAssignableFrom(type);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    private static <T> T createConfigDataInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        }
        catch (IllegalAccessException | InstantiationException e) {
            throw new IllegalArgumentException("Config class or sub-class missing a public no-arg constructor.", e);
        }
    }

    public void registerConfig(BalmConfigSchema var1);

    public BalmConfigSchema getSchema(ResourceLocation var1);

    public MutableLoadedConfig getLocalConfig(ResourceLocation var1);

    public LoadedConfig getActiveConfig(ResourceLocation var1);

    public File getConfigDir();

    default public File getConfigFile(BalmConfigSchema schema) {
        ResourceLocation identifier = schema.identifier();
        return new File(this.getConfigDir(), identifier.m_135827_() + "-" + identifier.m_135815_() + ".toml");
    }

    default public MutableLoadedConfig getLocalConfig(BalmConfigSchema schema) {
        return this.getLocalConfig(schema.identifier());
    }

    public <T> void updateLocalConfig(Class<T> var1, Consumer<T> var2);

    default public LoadedConfig getActiveConfig(BalmConfigSchema schema) {
        return this.getActiveConfig(schema.identifier());
    }

    default public BalmConfigSchema registerConfig(Class<?> configDataClass) {
        BalmConfigSchema schema = ConfigReflection.schemaOf(configDataClass);
        this.registerConfig(schema);
        return schema;
    }

    default public BalmConfigSchema getSchema(Class<?> configDataClass) {
        return this.getSchema(ConfigReflection.getIdentifier(configDataClass));
    }

    default public <T> T getActiveConfig(Class<T> configDataClass) {
        LoadedConfig loadedConfig = this.getActiveConfig(this.getSchema(configDataClass));
        return ConfigReflection.of(configDataClass, loadedConfig).data();
    }

    public Collection<BalmConfigSchema> getSchemasByNamespace(String var1);

    public Collection<BalmConfigSchema> getSchemas();

    default public void saveLocalConfig(BalmConfigSchema schema) {
        this.saveLocalConfig(schema, this.getLocalConfig(schema));
    }

    public void saveLocalConfig(BalmConfigSchema var1, MutableLoadedConfig var2);

    public void onConfigAvailable(BalmConfigSchema var1, Consumer<MutableLoadedConfig> var2);

    default public <T> void onConfigAvailable(Class<T> configDataClass, Consumer<T> handler) {
        this.onConfigAvailable(this.getSchema(configDataClass), (MutableLoadedConfig config) -> handler.accept(this.getActiveConfig(configDataClass)));
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public <T extends BalmConfigData> T initializeBackingConfig(Class<T> clazz) {
        this.registerConfig(clazz);
        return this.getBackingConfig(clazz);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public <T extends BalmConfigData> T getBackingConfig(Class<T> clazz) {
        BalmConfigSchema schema = this.getSchema(clazz);
        MutableLoadedConfig localConfig = this.getLocalConfig(schema);
        LoadedReflectionConfig<T> reflectionConfig = ConfigReflection.of(clazz, localConfig);
        return (T)((BalmConfigData)reflectionConfig.data());
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public <T extends BalmConfigData> void saveBackingConfig(Class<T> clazz) {
        this.saveLocalConfig(this.getSchema(clazz));
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public <T extends BalmConfigData> T getActive(Class<T> clazz) {
        return (T)((BalmConfigData)this.getActiveConfig(clazz));
    }

    @Deprecated
    default public <T extends BalmConfigData> void handleSync(Player player, SyncConfigMessage<T> message) {
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public <T extends BalmConfigData> void registerConfig(Class<T> clazz, Function<T, SyncConfigMessage<T>> syncMessageFactory) {
        this.registerConfig(clazz);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public <T extends BalmConfigData> void updateConfig(Class<T> clazz, Consumer<T> consumer) {
        this.updateLocalConfig(clazz, consumer);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public <T extends BalmConfigData> void resetToBackingConfig(Class<T> clazz) {
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public void resetToBackingConfigs() {
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public File getConfigFile(String configName) {
        return new File(this.getConfigDir(), configName + "-common.toml");
    }

    @Deprecated
    default public <T extends BalmConfigData> Table<String, String, BalmConfigProperty<?>> getConfigProperties(Class<T> clazz) {
        T backingConfig = this.getBackingConfig(clazz);
        BalmConfigData defaultConfig = (BalmConfigData)BalmConfig.createConfigDataInstance(clazz);
        HashBasedTable properties = HashBasedTable.create();
        for (Field rootField : ConfigReflection.getAllFields(clazz)) {
            String category = "";
            Class<?> fieldType = rootField.getType();
            if (BalmConfig.isPropertyType(fieldType)) {
                String property = rootField.getName();
                properties.put((Object)category, (Object)property, BalmConfig.createConfigProperty(backingConfig, null, rootField, defaultConfig));
                continue;
            }
            category = rootField.getName();
            for (Field propertyField : ConfigReflection.getAllFields(fieldType)) {
                String property = propertyField.getName();
                properties.put((Object)category, (Object)property, BalmConfig.createConfigProperty(backingConfig, rootField, propertyField, defaultConfig));
            }
        }
        return properties;
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public <T extends BalmConfigData> String getConfigName(Class<T> clazz) {
        return ConfigReflection.getIdentifier(clazz).m_135827_();
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public List<? extends BalmConfigData> getConfigsByMod(String modId) {
        return this.getSchemasByNamespace(modId).stream().map(this::getActiveConfig).filter(it -> it instanceof BalmConfigData).map(it -> (BalmConfigData)((Object)it)).toList();
    }
}

