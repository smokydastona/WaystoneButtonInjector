/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.Container
 *  net.minecraft.world.level.block.Block
 */
package net.blay09.mods.balm.common;

import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.capability.CapabilityType;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.Block;

public class CommonCapabilities {
    public static CapabilityType<Block, Container, Direction> CONTAINER;
    public static CapabilityType<Block, FluidTank, Void> FLUID_TANK;
    public static CapabilityType<Block, EnergyStorage, Direction> ENERGY_STORAGE;

    public static void initialize(BalmCapabilities capabilities) {
        CONTAINER = capabilities.registerType(CommonCapabilities.id("container"), Block.class, Container.class, Direction.class);
        FLUID_TANK = capabilities.registerType(CommonCapabilities.id("fluid_tank"), Block.class, FluidTank.class, Void.class);
        ENERGY_STORAGE = capabilities.registerType(CommonCapabilities.id("energy_storage"), Block.class, EnergyStorage.class, Direction.class);
        capabilities.registerFallbackBlockEntityProvider(CommonCapabilities.id("container"), CONTAINER, (blockEntity, direction) -> {
            if (blockEntity instanceof BalmContainerProvider) {
                BalmContainerProvider provider = (BalmContainerProvider)blockEntity;
                if (direction != null) {
                    return provider.getContainer((Direction)direction);
                }
                return provider.getContainer();
            }
            return null;
        });
        capabilities.registerFallbackBlockEntityProvider(CommonCapabilities.id("fluid_tank"), FLUID_TANK, (blockEntity, direction) -> {
            if (blockEntity instanceof BalmFluidTankProvider) {
                BalmFluidTankProvider provider = (BalmFluidTankProvider)blockEntity;
                return provider.getFluidTank();
            }
            return null;
        });
        capabilities.registerFallbackBlockEntityProvider(CommonCapabilities.id("energy_storage"), ENERGY_STORAGE, (blockEntity, direction) -> {
            if (blockEntity instanceof BalmEnergyStorageProvider) {
                BalmEnergyStorageProvider provider = (BalmEnergyStorageProvider)blockEntity;
                if (direction != null) {
                    return provider.getEnergyStorage((Direction)direction);
                }
                return provider.getEnergyStorage();
            }
            return null;
        });
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation("balm", path);
    }
}

