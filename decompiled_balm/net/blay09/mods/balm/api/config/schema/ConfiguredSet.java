/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.schema;

import java.util.Set;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.NestedTypeHolder;

public interface ConfiguredSet<T>
extends ConfiguredProperty<Set<T>>,
NestedTypeHolder<T> {
    default public Set<T> get(LoadedConfig config) {
        return (Set)this.getRaw(config);
    }

    default public Set<T> get() {
        return this.get(Balm.getConfig().getActiveConfig(this.parentSchema()));
    }

    default public void set(MutableLoadedConfig config, Set<T> value) {
        this.setRaw(config, value);
    }

    default public void set(Set<T> value) {
        this.set(Balm.getConfig().getLocalConfig(this.parentSchema()), value);
    }
}

