/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategoryBuilder;
import net.blay09.mods.balm.api.config.schema.builder.PropertyHolderBuilder;

public interface ConfigSchemaBuilder
extends PropertyHolderBuilder,
BalmConfigSchema {
    public ConfigCategoryBuilder category(String var1);
}

