/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.compat.hudinfo;

import net.blay09.mods.balm.api.compat.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.api.compat.hudinfo.HudInfoOutput;

@FunctionalInterface
public interface BlockInfoProvider {
    public void apply(BlockInfoContext var1, HudInfoOutput var2);
}

