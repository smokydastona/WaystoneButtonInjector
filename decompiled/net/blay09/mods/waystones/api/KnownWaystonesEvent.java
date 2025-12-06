/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.event.BalmEvent
 */
package net.blay09.mods.waystones.api;

import java.util.List;
import net.blay09.mods.balm.api.event.BalmEvent;
import net.blay09.mods.waystones.api.IWaystone;

public class KnownWaystonesEvent
extends BalmEvent {
    private final List<IWaystone> waystones;

    public KnownWaystonesEvent(List<IWaystone> waystones) {
        this.waystones = waystones;
    }

    public List<IWaystone> getWaystones() {
        return this.waystones;
    }
}

