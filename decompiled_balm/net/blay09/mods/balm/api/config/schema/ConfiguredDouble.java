/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

public interface ConfiguredDouble
extends ConfiguredProperty<Double> {
    default public double get(LoadedConfig config) {
        return (Double)this.getRaw(config);
    }

    default public double get() {
        return this.get(Balm.getConfig().getActiveConfig(this.parentSchema()));
    }

    default public void set(MutableLoadedConfig config, double value) {
        this.setRaw(config, value);
    }

    default public void set(double value) {
        this.set(Balm.getConfig().getLocalConfig(this.parentSchema()), value);
    }
}

