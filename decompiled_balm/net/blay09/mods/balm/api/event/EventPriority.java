/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.event;

public enum EventPriority {
    Lowest,
    Low,
    Normal,
    High,
    Highest;

    public static EventPriority[] values;

    static {
        values = EventPriority.values();
    }
}

