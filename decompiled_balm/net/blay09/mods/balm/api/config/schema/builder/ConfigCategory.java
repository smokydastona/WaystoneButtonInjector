/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.schema.builder;

import java.util.List;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

public interface ConfigCategory {
    public BalmConfigSchema parentSchema();

    public String name();

    public String comment();

    public List<ConfiguredProperty<?>> properties();
}

