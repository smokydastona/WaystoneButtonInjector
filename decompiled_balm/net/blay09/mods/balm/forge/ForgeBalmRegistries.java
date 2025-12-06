/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Registry
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraftforge.common.ForgeMod
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.RegistryObject
 */
package net.blay09.mods.balm.forge;

import java.util.function.Function;
import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ForgeBalmRegistries
implements BalmRegistries {
    @Override
    public void enableMilkFluid() {
        ForgeMod.enableMilkFluid();
    }

    @Override
    public Fluid getMilkFluid() {
        return (Fluid)ForgeMod.MILK.get();
    }

    @Override
    public <T> DeferredObject<T> register(Registry<T> registry, Function<ResourceLocation, T> supplier, ResourceLocation identifier) {
        DeferredRegister register = DeferredRegisters.get(registry.m_123023_(), identifier.m_135827_());
        RegistryObject registryObject = register.register(identifier.m_135815_(), () -> supplier.apply(identifier));
        return new DeferredObject(identifier, registryObject, () -> ((RegistryObject)registryObject).isPresent());
    }
}

