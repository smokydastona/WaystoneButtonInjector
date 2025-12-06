/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.Table
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.eventbus.api.Event
 *  net.minecraftforge.eventbus.api.EventPriority
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.forge.event;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.event.TickPhase;
import net.blay09.mods.balm.api.event.TickType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.Nullable;

public class ForgeBalmEvents
implements BalmEvents {
    private final Table<Class<?>, net.blay09.mods.balm.api.event.EventPriority, Consumer<net.blay09.mods.balm.api.event.EventPriority>> eventInitializers = HashBasedTable.create();
    private final Map<Class<?>, Consumer<?>> eventDispatchers = new HashMap();
    private final Table<Class<?>, net.blay09.mods.balm.api.event.EventPriority, List<Consumer<?>>> eventHandlers = HashBasedTable.create();
    private final Table<TickType<?>, TickPhase, Consumer<?>> tickEventInitializers = HashBasedTable.create();

    public static EventPriority toForge(net.blay09.mods.balm.api.event.EventPriority priority) {
        return switch (priority) {
            default -> throw new IncompatibleClassChangeError();
            case net.blay09.mods.balm.api.event.EventPriority.Lowest -> EventPriority.LOWEST;
            case net.blay09.mods.balm.api.event.EventPriority.Low -> EventPriority.LOW;
            case net.blay09.mods.balm.api.event.EventPriority.Normal -> EventPriority.NORMAL;
            case net.blay09.mods.balm.api.event.EventPriority.High -> EventPriority.HIGH;
            case net.blay09.mods.balm.api.event.EventPriority.Highest -> EventPriority.HIGHEST;
        };
    }

    public void registerEvent(Class<?> eventClass, Consumer<net.blay09.mods.balm.api.event.EventPriority> initializer) {
        this.registerEvent(eventClass, initializer, null);
    }

    public void registerEvent(Class<?> eventClass, Consumer<net.blay09.mods.balm.api.event.EventPriority> initializer, @Nullable Consumer<?> dispatcher) {
        for (net.blay09.mods.balm.api.event.EventPriority priority : net.blay09.mods.balm.api.event.EventPriority.values()) {
            this.eventInitializers.put(eventClass, (Object)priority, initializer);
        }
        if (dispatcher != null) {
            this.eventDispatchers.put(eventClass, dispatcher);
        }
    }

    public <T> void fireEventHandlers(net.blay09.mods.balm.api.event.EventPriority priority, T event) {
        List handlers = (List)this.eventHandlers.get(event.getClass(), (Object)priority);
        if (handlers != null) {
            handlers.forEach(handler -> this.fireEventHandler((Consumer)handler, event));
        }
    }

    private <T> void fireEventHandler(Consumer<T> handler, Object event) {
        handler.accept(event);
    }

    @Override
    public <T> void onEvent(Class<T> eventClass, Consumer<T> handler, net.blay09.mods.balm.api.event.EventPriority priority) {
        ArrayList<Consumer<T>> consumers;
        Consumer initializer = (Consumer)this.eventInitializers.remove(eventClass, (Object)priority);
        if (initializer != null) {
            initializer.accept(priority);
        }
        if ((consumers = (ArrayList<Consumer<T>>)this.eventHandlers.get(eventClass, (Object)priority)) == null) {
            consumers = new ArrayList<Consumer<T>>();
            this.eventHandlers.put(eventClass, (Object)priority, consumers);
        }
        consumers.add(handler);
    }

    @Override
    public <T> void fireEvent(T event) {
        Consumer<?> handler = this.eventDispatchers.get(event.getClass());
        if (handler != null) {
            handler.accept(event);
        } else {
            for (net.blay09.mods.balm.api.event.EventPriority priority : net.blay09.mods.balm.api.event.EventPriority.values) {
                this.fireEventHandlers(priority, event);
            }
        }
        if (event instanceof Event) {
            Event forgeEvent = (Event)event;
            MinecraftForge.EVENT_BUS.post(forgeEvent);
        }
    }

    @Override
    public <T> void onTickEvent(TickType<T> type, TickPhase phase, T handler) {
        Consumer initializer = (Consumer)this.tickEventInitializers.get(type, (Object)phase);
        initializer.accept(handler);
    }

    public <T> void registerTickEvent(TickType<?> type, TickPhase phase, Consumer<T> initializer) {
        this.tickEventInitializers.put(type, (Object)phase, initializer);
    }
}

