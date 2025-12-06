/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.api.event;

import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.event.BalmEvent;
import org.jetbrains.annotations.Nullable;

public class ConfigReloadedEvent
extends BalmEvent {
    private final BalmConfigSchema schema;

    public ConfigReloadedEvent() {
        this.schema = null;
    }

    public ConfigReloadedEvent(BalmConfigSchema schema) {
        this.schema = schema;
    }

    @Nullable
    public BalmConfigSchema getSchema() {
        return this.schema;
    }
}

