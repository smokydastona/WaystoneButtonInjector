/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 */
package net.blay09.mods.balm.api.event;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class PlayerAttackEvent
extends BalmEvent {
    private final Player player;
    private final Entity target;

    public PlayerAttackEvent(Player player, Entity target) {
        this.player = player;
        this.target = target;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Entity getTarget() {
        return this.target;
    }
}

