/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.Level
 */
package net.blay09.mods.balm.api.event;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityAddedEvent
extends BalmEvent {
    private final Entity entity;
    private final Level level;

    public EntityAddedEvent(Entity entity, Level level) {
        this.entity = entity;
        this.level = level;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Level getLevel() {
        return this.level;
    }
}

