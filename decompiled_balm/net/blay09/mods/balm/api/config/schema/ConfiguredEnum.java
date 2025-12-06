/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

public interface ConfiguredEnum<T extends Enum<T>>
extends ConfiguredProperty<T> {
    default public T get(LoadedConfig config) {
        return (T)((Enum)this.getRaw(config));
    }

    default public T get() {
        return this.get(Balm.getConfig().getActiveConfig(this.parentSchema()));
    }

    default public void set(MutableLoadedConfig config, T value) {
        this.setRaw(config, value);
    }

    default public void set(T value) {
        this.set(Balm.getConfig().getLocalConfig(this.parentSchema()), value);
    }
}

