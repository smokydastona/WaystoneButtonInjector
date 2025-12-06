/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config;

import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

public interface LoadedConfig {
    public <T> T getRaw(ConfiguredProperty<T> var1);

    public MutableLoadedConfig mutable(BalmConfigSchema var1);
}

