/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.StringRepresentable
 */
package net.blay09.mods.waystones.api;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;

public enum WaystoneOrigin implements StringRepresentable
{
    UNKNOWN,
    WILDERNESS,
    DUNGEON,
    VILLAGE,
    PLAYER;


    public String m_7912_() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}

