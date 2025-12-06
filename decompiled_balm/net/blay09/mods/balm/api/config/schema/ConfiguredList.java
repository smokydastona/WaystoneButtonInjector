/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.schema;

import java.util.List;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.NestedTypeHolder;

public interface ConfiguredList<T>
extends ConfiguredProperty<List<T>>,
NestedTypeHolder<T> {
    default public List<T> get(LoadedConfig config) {
        return (List)this.getRaw(config);
    }

    default public List<T> get() {
        return this.get(Balm.getConfig().getActiveConfig(this.parentSchema()));
    }

    default public void set(MutableLoadedConfig config, List<T> value) {
        this.setRaw(config, value);
    }

    default public void set(List<T> value) {
        this.set(Balm.getConfig().getLocalConfig(this.parentSchema()), value);
    }
}

