/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 */
package net.blay09.mods.waystones.client.gui.widget;

import java.util.List;
import net.minecraft.network.chat.Component;

public interface ITooltipProvider {
    public boolean shouldShowTooltip();

    public List<Component> getTooltipComponents();
}

