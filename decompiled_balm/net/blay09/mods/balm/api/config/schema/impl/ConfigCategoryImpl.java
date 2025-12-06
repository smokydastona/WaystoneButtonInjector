/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.schema.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategory;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategoryBuilder;
import net.blay09.mods.balm.api.config.schema.builder.ConfigPropertyBuilder;
import net.blay09.mods.balm.api.config.schema.impl.ConfigSchemaImpl;

public class ConfigCategoryImpl
implements ConfigCategoryBuilder,
ConfigCategory {
    private final List<ConfiguredProperty<?>> properties = new ArrayList();
    private final ConfigSchemaImpl schema;
    private final String name;
    private String comment = "";

    public ConfigCategoryImpl(ConfigSchemaImpl schema, String name) {
        this.schema = schema;
        this.name = name;
    }

    @Override
    public ConfigCategoryImpl comment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public BalmConfigSchema parentSchema() {
        return this.schema;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String comment() {
        return this.comment;
    }

    @Override
    public List<ConfiguredProperty<?>> properties() {
        return this.properties;
    }

    @Override
    public ConfigPropertyBuilder property(String name) {
        return new ConfigPropertyBuilder(this.schema, this.name, name);
    }

    @Override
    public <T> T via(Function<ConfigCategoryBuilder, T> initializer) {
        return initializer.apply(this);
    }

    public <T extends ConfiguredProperty<?>> void addProperty(T property) {
        this.properties.add(property);
    }
}

