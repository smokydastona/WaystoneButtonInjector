/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.compat;

import net.blay09.mods.balm.api.compat.hudinfo.BalmModSupportHudInfo;
import net.blay09.mods.balm.api.compat.trinkets.BalmModSupportTrinkets;

public interface BalmModSupport {
    public BalmModSupportTrinkets trinkets();

    public BalmModSupportHudInfo hudInfo();
}

