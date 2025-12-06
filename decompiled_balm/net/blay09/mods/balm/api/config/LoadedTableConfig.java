/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.Table
 *  com.google.gson.Gson
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.serialization.DynamicOps
 *  com.mojang.serialization.JsonOps
 */
package net.blay09.mods.balm.api.config;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import java.util.ArrayList;
import java.util.List;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.PropertyAwareConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategory;

public record LoadedTableConfig(Table<String, String, Object> table) implements MutableLoadedConfig,
PropertyAwareConfig
{
    private static final Gson GSON = new Gson();

    public LoadedTableConfig() {
        this((Table<String, String, Object>)HashBasedTable.create());
    }

    public static Pair<LoadedTableConfig, List<Throwable>> of(BalmConfigSchema schema, Table<String, String, Object> table) {
        HashBasedTable validatedTable = HashBasedTable.create();
        ArrayList<Throwable> errors = new ArrayList<Throwable>();
        for (ConfiguredProperty<?> rootProperty : schema.rootProperties()) {
            try {
                Object value = LoadedTableConfig.validate(rootProperty, table);
                validatedTable.put((Object)rootProperty.category(), (Object)rootProperty.name(), value);
            }
            catch (Throwable e) {
                validatedTable.put((Object)rootProperty.category(), (Object)rootProperty.name(), rootProperty.defaultValue());
                errors.add(e);
            }
        }
        for (ConfigCategory category : schema.categories()) {
            for (ConfiguredProperty<?> property : category.properties()) {
                try {
                    Object value = LoadedTableConfig.validate(property, table);
                    validatedTable.put((Object)property.category(), (Object)property.name(), value);
                }
                catch (Throwable e) {
                    validatedTable.put((Object)property.category(), (Object)property.name(), property.defaultValue());
                    errors.add(e);
                }
            }
        }
        return Pair.of((Object)new LoadedTableConfig((Table<String, String, Object>)validatedTable), errors);
    }

    private static <T> T validate(ConfiguredProperty<T> property, Table<String, String, Object> table) {
        Object value = table.get((Object)property.category(), (Object)property.name());
        return (T)((Pair)property.codec().decode((DynamicOps)JsonOps.INSTANCE, (Object)GSON.toJsonTree(value)).getOrThrow(false, error -> {})).getFirst();
    }

    @Override
    public <T> void setRaw(ConfiguredProperty<T> property, T value) {
        if (!property.type().isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException("Invalid type for property " + property.name() + " in category " + property.category() + ": " + value.getClass().getName() + ", expected " + property.type().getName());
        }
        this.table.put((Object)property.category(), (Object)property.name(), value);
    }

    @Override
    public MutableLoadedConfig copy() {
        return new LoadedTableConfig((Table<String, String, Object>)HashBasedTable.create(this.table));
    }

    @Override
    public MutableLoadedConfig mutable(BalmConfigSchema schema) {
        return this;
    }

    @Override
    public <T> T getRaw(ConfiguredProperty<T> property) {
        Object value = this.table.get((Object)property.category(), (Object)property.name());
        if (value == null) {
            return property.defaultValue();
        }
        if (!property.type().isAssignableFrom(value.getClass())) {
            return property.defaultValue();
        }
        return (T)value;
    }

    @Override
    public boolean hasProperty(ConfiguredProperty<?> property) {
        return this.table.contains((Object)property.category(), (Object)property.name());
    }
}

