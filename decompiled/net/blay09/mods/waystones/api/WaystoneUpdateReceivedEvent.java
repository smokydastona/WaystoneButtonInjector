/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.event.BalmEvent
 */
package net.blay09.mods.waystones.api;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.blay09.mods.waystones.api.IWaystone;

public class WaystoneUpdateReceivedEvent
extends BalmEvent {
    private final IWaystone waystone;

    public WaystoneUpdateReceivedEvent(IWaystone waystone) {
        this.waystone = waystone;
    }

    public IWaystone getWaystone() {
        return this.waystone;
    }
}

