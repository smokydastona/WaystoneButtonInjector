/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.event;

import java.util.function.Consumer;
import net.blay09.mods.balm.api.event.EventPriority;
import net.blay09.mods.balm.api.event.TickPhase;
import net.blay09.mods.balm.api.event.TickType;

public interface BalmEvents {
    default public <T> void onEvent(Class<T> eventClass, Consumer<T> handler) {
        this.onEvent(eventClass, handler, EventPriority.Normal);
    }

    public <T> void onEvent(Class<T> var1, Consumer<T> var2, EventPriority var3);

    public <T> void fireEvent(T var1);

    public <T> void onTickEvent(TickType<T> var1, TickPhase var2, T var3);
}

