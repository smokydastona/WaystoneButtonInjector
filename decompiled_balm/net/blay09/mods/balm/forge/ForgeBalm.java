/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.Container
 *  net.minecraftforge.common.capabilities.CapabilityToken
 *  net.minecraftforge.common.capabilities.ForgeCapabilities
 *  net.minecraftforge.energy.IEnergyStorage
 *  net.minecraftforge.eventbus.api.IEventBus
 *  net.minecraftforge.fluids.capability.IFluidHandler
 *  net.minecraftforge.fluids.capability.IFluidHandlerItem
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent
 *  net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
 *  net.minecraftforge.items.IItemHandler
 */
package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.blay09.mods.balm.forge.ForgeBalmRuntime;
import net.blay09.mods.balm.forge.ForgeLoadContext;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.blay09.mods.balm.forge.capability.ForgeBalmCapabilities;
import net.blay09.mods.balm.forge.capability.ForgeCommonCapabilities;
import net.blay09.mods.balm.forge.client.ForgeBalmClient;
import net.blay09.mods.balm.forge.compat.hudinfo.TheOneProbeModCompat;
import net.blay09.mods.balm.forge.provider.ForgeBalmProviders;
import net.blay09.mods.balm.forge.world.ForgeBalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;

@Mod(value="balm")
public class ForgeBalm {
    public ForgeBalm() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BalmLoadContexts.register("balm", new ForgeLoadContext(modEventBus));
        Balm.registerModule(new ForgeCommonCapabilities());
        ((ForgeBalmRuntime)Balm.getRuntime()).initializeRuntime();
        DeferredRegisters.register("balm", modEventBus);
        ModBusEventRegisters.register("balm", modEventBus);
        ForgeBalmWorldGen.initializeBalmBiomeModifiers(modEventBus);
        modEventBus.addListener(ForgeBalmClient::onInitializeClient);
        modEventBus.addListener(this::enqueueIMC);
        ForgeBalmProviders providers = (ForgeBalmProviders)Balm.getProviders();
        providers.register(IItemHandler.class, new CapabilityToken<IItemHandler>(){});
        providers.register(IFluidHandler.class, new CapabilityToken<IFluidHandler>(){});
        providers.register(IFluidHandlerItem.class, new CapabilityToken<IFluidHandlerItem>(){});
        providers.register(IEnergyStorage.class, new CapabilityToken<IEnergyStorage>(){});
        providers.register(Container.class, new CapabilityToken<Container>(){});
        providers.register(FluidTank.class, new CapabilityToken<FluidTank>(){});
        providers.register(EnergyStorage.class, new CapabilityToken<EnergyStorage>(){});
        ForgeBalmCapabilities capabilities = (ForgeBalmCapabilities)Balm.getCapabilities();
        capabilities.addExistingType(new ResourceLocation("forge", "item_handler"), IItemHandler.class, ForgeCapabilities.ITEM_HANDLER);
        capabilities.addExistingType(new ResourceLocation("forge", "fluid_handler"), IFluidHandler.class, ForgeCapabilities.FLUID_HANDLER);
        capabilities.addExistingType(new ResourceLocation("forge", "energy_storage"), IEnergyStorage.class, ForgeCapabilities.ENERGY);
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
        if (Balm.isModLoaded("theoneprobe")) {
            TheOneProbeModCompat.register();
        }
    }
}

