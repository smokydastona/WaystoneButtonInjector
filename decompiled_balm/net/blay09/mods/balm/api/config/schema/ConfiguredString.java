/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

public interface ConfiguredString
extends ConfiguredProperty<String> {
    default public String get(LoadedConfig config) {
        return (String)this.getRaw(config);
    }

    default public String get() {
        return this.get(Balm.getConfig().getActiveConfig(this.parentSchema()));
    }

    default public void set(MutableLoadedConfig config, String value) {
        this.setRaw(config, value);
    }

    default public void set(String value) {
        this.set(Balm.getConfig().getLocalConfig(this.parentSchema()), value);
    }
}

