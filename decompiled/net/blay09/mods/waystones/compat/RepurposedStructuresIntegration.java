/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraftforge.eventbus.api.IEventBus
 *  net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.RegistryObject
 */
package net.blay09.mods.waystones.compat;

import java.util.function.Supplier;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RepurposedStructuresIntegration {
    public static final DeferredRegister<Supplier<Boolean>> RS_CONDITIONS_REGISTRY = DeferredRegister.createOptional((ResourceLocation)new ResourceLocation("repurposed_structures", "json_conditions"), (String)"waystones");
    public static final RegistryObject<Supplier<Boolean>> WAYSTONE_CONFIG_CONDITION = RS_CONDITIONS_REGISTRY.register("config", () -> () -> WaystonesConfig.getActive().worldGen.spawnInVillages || WaystonesConfig.getActive().worldGen.forceSpawnInVillages);

    public RepurposedStructuresIntegration() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        RS_CONDITIONS_REGISTRY.register(modEventBus);
    }
}

