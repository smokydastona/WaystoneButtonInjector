/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.balm.api.config.schema;

import java.util.Collection;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategory;
import net.blay09.mods.balm.api.config.schema.impl.ConfigSchemaImpl;
import net.minecraft.resources.ResourceLocation;

public interface BalmConfigSchema {
    public static ConfigSchemaImpl create(ResourceLocation identifier) {
        return new ConfigSchemaImpl(identifier);
    }

    public ResourceLocation identifier();

    public LoadedConfig defaults();

    public Collection<ConfiguredProperty<?>> rootProperties();

    public Collection<ConfigCategory> categories();

    public ConfiguredProperty<?> findProperty(String var1, String var2);

    public ConfiguredProperty<?> findRootProperty(String var1);
}

