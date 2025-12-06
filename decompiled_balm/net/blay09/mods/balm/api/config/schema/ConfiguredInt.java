/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

public interface ConfiguredInt
extends ConfiguredProperty<Integer> {
    default public int get(LoadedConfig config) {
        return (Integer)this.getRaw(config);
    }

    default public int get() {
        return this.get(Balm.getConfig().getActiveConfig(this.parentSchema()));
    }

    default public void set(MutableLoadedConfig config, int value) {
        this.setRaw(config, value);
    }

    default public void set(int value) {
        this.set(Balm.getConfig().getLocalConfig(this.parentSchema()), value);
    }
}

