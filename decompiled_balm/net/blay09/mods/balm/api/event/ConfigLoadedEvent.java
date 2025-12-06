/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.event;

import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.event.BalmEvent;

public class ConfigLoadedEvent
extends BalmEvent {
    private final BalmConfigSchema schema;

    public ConfigLoadedEvent(BalmConfigSchema schema) {
        this.schema = schema;
    }

    public BalmConfigSchema getSchema() {
        return this.schema;
    }
}

