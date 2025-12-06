/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.waystones.handler;

import net.blay09.mods.waystones.api.WaystoneActivatedEvent;
import net.blay09.mods.waystones.stats.ModStats;

public class WaystoneActivationStatHandler {
    public static void onWaystoneActivated(WaystoneActivatedEvent event) {
        event.getPlayer().m_36220_(ModStats.waystoneActivated);
    }
}

