/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Table
 *  net.minecraftforge.common.ForgeConfigSpec$ConfigValue
 *  net.minecraftforge.fml.config.ModConfig
 */
package net.blay09.mods.balm.forge.config;

import com.google.common.collect.Table;
import net.blay09.mods.balm.api.config.LoadedTableConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.PropertyAwareConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.forge.config.ForgeBalmConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public record LoadedForgeConfig(BalmConfigSchema schema, ModConfig modConfig, Table<String, String, ForgeConfigSpec.ConfigValue<?>> properties) implements MutableLoadedConfig,
PropertyAwareConfig
{
    @Override
    public <T> void setRaw(ConfiguredProperty<T> property, T value) {
        ForgeConfigSpec.ConfigValue backingProperty = (ForgeConfigSpec.ConfigValue)this.properties.get((Object)property.category(), (Object)property.name());
        if (backingProperty != null) {
            Object mappedValue = ForgeBalmConfig.mapConfigValueToNeoForge(value);
            backingProperty.set(mappedValue);
        }
    }

    @Override
    public MutableLoadedConfig copy() {
        LoadedTableConfig newConfig = new LoadedTableConfig();
        newConfig.applyFrom(this.schema, this);
        return newConfig;
    }

    @Override
    public <T> T getRaw(ConfiguredProperty<T> property) {
        ForgeConfigSpec.ConfigValue backingProperty = (ForgeConfigSpec.ConfigValue)this.properties.get((Object)property.category(), (Object)property.name());
        if (backingProperty != null) {
            Object value = backingProperty.get();
            return (T)ForgeBalmConfig.mapConfigValueFromNeoForge(property, value);
        }
        return property.defaultValue();
    }

    @Override
    public MutableLoadedConfig mutable(BalmConfigSchema schema) {
        return this;
    }

    @Override
    public boolean hasProperty(ConfiguredProperty<?> property) {
        return this.properties.contains((Object)property.category(), (Object)property.name());
    }
}

