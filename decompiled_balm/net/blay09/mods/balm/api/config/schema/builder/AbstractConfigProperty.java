/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.config.schema.builder;

import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.builder.ConfigPropertyBuilder;
import net.blay09.mods.balm.api.config.schema.impl.ConfigSchemaImpl;

public abstract class AbstractConfigProperty<T>
implements ConfiguredProperty<T> {
    private final ConfigSchemaImpl schema;
    private final String category;
    private final String name;
    private final String comment;
    private final boolean synced;

    public AbstractConfigProperty(ConfigPropertyBuilder parent) {
        this.schema = parent.schema;
        this.category = parent.category;
        this.name = parent.name;
        this.comment = parent.comment;
        this.synced = parent.synced;
    }

    @Override
    public ConfigSchemaImpl parentSchema() {
        return this.schema;
    }

    @Override
    public String category() {
        return this.category;
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
    public boolean synced() {
        return this.synced;
    }
}

