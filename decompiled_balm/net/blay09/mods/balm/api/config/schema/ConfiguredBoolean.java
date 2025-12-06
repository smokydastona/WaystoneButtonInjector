/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

public interface ConfiguredBoolean
extends ConfiguredProperty<Boolean> {
    default public boolean get(LoadedConfig config) {
        return (Boolean)this.getRaw(config);
    }

    default public boolean get() {
        return this.get(Balm.getConfig().getActiveConfig(this.parentSchema()));
    }

    default public void set(MutableLoadedConfig config, boolean value) {
        this.setRaw(config, value);
    }

    default public void set(boolean value) {
        this.set(Balm.getConfig().getLocalConfig(this.parentSchema()), value);
    }
}

