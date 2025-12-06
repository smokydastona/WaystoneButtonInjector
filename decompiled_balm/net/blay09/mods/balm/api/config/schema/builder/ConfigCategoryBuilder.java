/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.schema.builder;

import java.util.function.Function;
import net.blay09.mods.balm.api.config.schema.builder.PropertyHolderBuilder;
import net.blay09.mods.balm.api.config.schema.impl.ConfigCategoryImpl;

public interface ConfigCategoryBuilder
extends PropertyHolderBuilder {
    public ConfigCategoryImpl comment(String var1);

    public <T> T via(Function<ConfigCategoryBuilder, T> var1);
}

