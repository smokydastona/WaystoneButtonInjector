/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

public interface ConfiguredFloat
extends ConfiguredProperty<Float> {
    default public float get(LoadedConfig config) {
        return ((Float)this.getRaw(config)).floatValue();
    }

    default public float get() {
        return this.get(Balm.getConfig().getActiveConfig(this.parentSchema()));
    }

    default public void set(MutableLoadedConfig config, float value) {
        this.setRaw(config, Float.valueOf(value));
    }

    default public void set(float value) {
        this.set(Balm.getConfig().getLocalConfig(this.parentSchema()), value);
    }
}

