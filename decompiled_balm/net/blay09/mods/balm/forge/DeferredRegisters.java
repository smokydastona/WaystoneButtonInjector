/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.Table
 *  com.google.common.collect.Tables
 *  net.minecraft.core.Registry
 *  net.minecraft.resources.ResourceKey
 *  net.minecraftforge.eventbus.api.IEventBus
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.IForgeRegistry
 */
package net.blay09.mods.balm.forge;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import java.util.Collection;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

public class DeferredRegisters {
    private static final Table<ResourceKey<?>, String, DeferredRegister<?>> deferredRegisters = Tables.synchronizedTable((Table)HashBasedTable.create());

    public static <T> DeferredRegister<T> get(IForgeRegistry<T> registry, String modId) {
        return DeferredRegisters.get(registry.getRegistryKey(), modId);
    }

    public static <T> DeferredRegister<T> get(ResourceKey<Registry<T>> registry, String modId) {
        DeferredRegister register = (DeferredRegister)deferredRegisters.get(registry, (Object)modId);
        if (register == null) {
            register = DeferredRegister.create(registry, (String)modId);
            deferredRegisters.put(registry, (Object)modId, (Object)register);
        }
        return register;
    }

    public static Collection<DeferredRegister<?>> getByModId(String modId) {
        return deferredRegisters.column((Object)modId).values();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void register(String modId, IEventBus modEventBus) {
        Table<ResourceKey<?>, String, DeferredRegister<?>> table = deferredRegisters;
        synchronized (table) {
            for (DeferredRegister<?> deferredRegister : DeferredRegisters.getByModId(modId)) {
                deferredRegister.register(modEventBus);
            }
        }
    }
}

