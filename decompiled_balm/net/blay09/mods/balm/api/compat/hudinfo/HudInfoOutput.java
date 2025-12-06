/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 */
package net.blay09.mods.balm.api.compat.hudinfo;

import net.minecraft.network.chat.Component;

public interface HudInfoOutput {
    public void text(Component var1);

    public void progress(float var1);

    default public void progress(int progress, int maxProgress) {
        this.progress((float)progress / (float)maxProgress);
    }
}

