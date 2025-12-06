/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config;

import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.LoadedTableConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

public class DefaultedConfig
implements LoadedConfig {
    public static final DefaultedConfig INSTANCE = new DefaultedConfig();

    @Override
    public <T> T getRaw(ConfiguredProperty<T> property) {
        return property.defaultValue();
    }

    @Override
    public MutableLoadedConfig mutable(BalmConfigSchema schema) {
        LoadedTableConfig mutableConfig = new LoadedTableConfig();
        mutableConfig.applyFrom(schema, this);
        return mutableConfig;
    }
}

