/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.reflection;

import java.lang.reflect.Field;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedTableConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.PropertyAwareConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

public record LoadedReflectionConfig<ConfigData>(ConfigData data) implements MutableLoadedConfig,
PropertyAwareConfig
{
    @Override
    public <T> void setRaw(ConfiguredProperty<T> property, T value) {
        try {
            Object holder = this.locatePropertyHolder(property);
            Field field = holder.getClass().getField(property.name());
            field.set(holder, value);
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("Failed to set config property '" + (String)(property.category().isEmpty() ? "" : property.category() + ".") + property.name() + "'", e);
        }
    }

    @Override
    public <T> T getRaw(ConfiguredProperty<T> property) {
        try {
            Object holder = this.locatePropertyHolder(property);
            Field field = holder.getClass().getField(property.name());
            Object value = field.get(holder);
            return (T)value;
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("Failed to get config property '" + (String)(property.category().isEmpty() ? "" : property.category() + ".") + property.name() + "'", e);
        }
    }

    @Override
    public MutableLoadedConfig copy() {
        LoadedTableConfig newConfig = new LoadedTableConfig();
        newConfig.applyFrom(Balm.getConfig().getSchema(this.data.getClass()), this);
        return newConfig;
    }

    @Override
    public MutableLoadedConfig mutable(BalmConfigSchema schema) {
        return this;
    }

    private Object locatePropertyHolder(ConfiguredProperty<?> property) throws NoSuchFieldException, IllegalAccessException {
        String category = property.category();
        if (category != null && !category.isEmpty()) {
            Field categoryField = this.data.getClass().getField(category);
            return categoryField.get(this.data);
        }
        return this.data;
    }

    @Override
    public boolean hasProperty(ConfiguredProperty<?> property) {
        try {
            Object holder = this.locatePropertyHolder(property);
            holder.getClass().getField(property.name());
            return true;
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            return false;
        }
    }
}

