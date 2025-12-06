/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.Container
 *  net.minecraftforge.common.capabilities.CapabilityToken
 */
package net.blay09.mods.balm.forge.capability;

import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.forge.capability.ForgeBalmCapabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ForgeCommonCapabilities
implements BalmModule {
    private static ResourceLocation id(String path) {
        return new ResourceLocation("balm", path);
    }

    @Override
    public ResourceLocation getId() {
        return ForgeCommonCapabilities.id("forge_common_capabilities");
    }

    @Override
    public void registerCapabilities(BalmCapabilities capabilities) {
        ForgeBalmCapabilities forgeCapabilities = (ForgeBalmCapabilities)capabilities;
        forgeCapabilities.preRegisterType(ForgeCommonCapabilities.id("container"), new CapabilityToken<Container>(){});
        forgeCapabilities.preRegisterType(ForgeCommonCapabilities.id("fluid_tank"), new CapabilityToken<FluidTank>(){});
        forgeCapabilities.preRegisterType(ForgeCommonCapabilities.id("energy_storage"), new CapabilityToken<EnergyStorage>(){});
    }
}

