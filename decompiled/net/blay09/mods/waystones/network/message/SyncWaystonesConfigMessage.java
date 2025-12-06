/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.network.SyncConfigMessage
 */
package net.blay09.mods.waystones.network.message;

import net.blay09.mods.balm.api.network.SyncConfigMessage;
import net.blay09.mods.waystones.config.WaystonesConfigData;

public class SyncWaystonesConfigMessage
extends SyncConfigMessage<WaystonesConfigData> {
    public SyncWaystonesConfigMessage(WaystonesConfigData data) {
        super((Object)data);
    }
}

