/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.minecraft.resources.ResourceLocation;

public interface ConfiguredResourceLocation
extends ConfiguredProperty<ResourceLocation> {
    default public ResourceLocation get(LoadedConfig config) {
        return (ResourceLocation)this.getRaw(config);
    }

    default public ResourceLocation get() {
        return this.get(Balm.getConfig().getActiveConfig(this.parentSchema()));
    }

    default public void set(MutableLoadedConfig config, ResourceLocation value) {
        this.setRaw(config, value);
    }

    default public void set(ResourceLocation value) {
        this.set(Balm.getConfig().getLocalConfig(this.parentSchema()), value);
    }
}

