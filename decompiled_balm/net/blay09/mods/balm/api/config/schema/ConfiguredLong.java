/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

public interface ConfiguredLong
extends ConfiguredProperty<Long> {
    default public long get(LoadedConfig config) {
        return (Long)this.getRaw(config);
    }

    default public long get() {
        return this.get(Balm.getConfig().getActiveConfig(this.parentSchema()));
    }

    default public void set(MutableLoadedConfig config, long value) {
        this.setRaw(config, value);
    }

    default public void set(long value) {
        this.set(Balm.getConfig().getLocalConfig(this.parentSchema()), value);
    }
}

