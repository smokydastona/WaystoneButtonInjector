/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.Table
 *  com.google.common.collect.Tables
 *  net.minecraftforge.eventbus.api.IEventBus
 */
package net.blay09.mods.balm.forge;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModBusEventRegisters {
    private static final Map<String, IEventBus> modEventBuses = new ConcurrentHashMap<String, IEventBus>();
    private static final Table<String, Class<?>, Object> registrations = Tables.synchronizedTable((Table)HashBasedTable.create());

    public static <T> T getRegistrations(String namespace, Class<T> clazz) {
        Object existing = registrations.get((Object)namespace, clazz);
        if (existing != null) {
            return (T)existing;
        }
        try {
            T instance = clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
            registrations.put((Object)namespace, clazz, instance);
            IEventBus modEventBus = modEventBuses.get(namespace);
            if (modEventBus != null) {
                modEventBus.register(instance);
            }
            return instance;
        }
        catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void register(String modId, IEventBus modEventBus) {
        modEventBuses.put(modId, modEventBus);
        Table<String, Class<?>, Object> table = registrations;
        synchronized (table) {
            for (Object registrations : ModBusEventRegisters.getByModId(modId)) {
                modEventBus.register(registrations);
            }
        }
    }

    private static Collection<Object> getByModId(String modId) {
        return registrations.row((Object)modId).values();
    }
}

