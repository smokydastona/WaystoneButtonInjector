/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.InteractionHand
 */
package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.world.InteractionHand;

public class UseItemInputEvent
extends BalmEvent {
    private final InteractionHand hand;

    public UseItemInputEvent(InteractionHand hand) {
        this.hand = hand;
    }

    public InteractionHand getHand() {
        return this.hand;
    }
}

