/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.Table
 *  com.mojang.datafixers.util.Pair
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.Container
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraftforge.common.capabilities.Capability
 *  net.minecraftforge.common.capabilities.ForgeCapabilities
 *  net.minecraftforge.common.util.LazyOptional
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.api.block.entity;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.blay09.mods.balm.forge.container.BalmInvWrapper;
import net.blay09.mods.balm.forge.energy.ForgeEnergyStorage;
import net.blay09.mods.balm.forge.fluid.ForgeFluidTank;
import net.blay09.mods.balm.forge.provider.ForgeBalmProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BalmBlockEntityBase
extends BlockEntity {
    private final Map<Capability<?>, LazyOptional<?>> capabilities = new HashMap();
    private final Table<Capability<?>, Direction, LazyOptional<?>> sidedCapabilities = HashBasedTable.create();
    private boolean capabilitiesInitialized;

    public BalmBlockEntityBase(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    private void addCapabilities(BalmProvider<?> provider, Map<Capability<?>, LazyOptional<?>> capabilities) {
        ForgeBalmProviders forgeProviders = (ForgeBalmProviders)Balm.getProviders();
        Capability<?> capability = forgeProviders.getCapability(provider.getProviderClass());
        capabilities.put(capability, LazyOptional.of(provider::getInstance));
        if (provider.getProviderClass() == Container.class) {
            capabilities.put(ForgeCapabilities.ITEM_HANDLER, LazyOptional.of(() -> new BalmInvWrapper((Container)provider.getInstance())));
        } else if (provider.getProviderClass() == FluidTank.class) {
            capabilities.put(ForgeCapabilities.FLUID_HANDLER, LazyOptional.of(() -> new ForgeFluidTank((FluidTank)provider.getInstance())));
        } else if (provider.getProviderClass() == EnergyStorage.class) {
            capabilities.put(ForgeCapabilities.ENERGY, LazyOptional.of(() -> new ForgeEnergyStorage((EnergyStorage)provider.getInstance())));
        }
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    public <T> T getProvider(Class<T> clazz) {
        ForgeBalmProviders forgeProviders = (ForgeBalmProviders)Balm.getProviders();
        Capability<T> capability = forgeProviders.getCapability(clazz);
        return this.getCapability(capability).resolve().orElse(null);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (!this.capabilitiesInitialized) {
            ArrayList<Object> providers = new ArrayList<Object>();
            this.buildProviders(providers);
            for (Object e : providers) {
                BalmProviderHolder providerHolder = (BalmProviderHolder)e;
                for (BalmProvider<?> balmProvider : providerHolder.getProviders()) {
                    this.addCapabilities(balmProvider, this.capabilities);
                }
                for (Pair pair : providerHolder.getSidedProviders()) {
                    Direction direction = (Direction)pair.getFirst();
                    BalmProvider provider = (BalmProvider)pair.getSecond();
                    Map sidedCapabilities = this.sidedCapabilities.column((Object)direction);
                    this.addCapabilities(provider, sidedCapabilities);
                }
            }
            this.capabilitiesInitialized = true;
        }
        LazyOptional<?> result = null;
        if (side != null) {
            result = (LazyOptional<?>)this.sidedCapabilities.get(cap, (Object)side);
        }
        if (result == null) {
            result = this.capabilities.get(cap);
        }
        return result != null ? result.cast() : super.getCapability(cap, side);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    protected abstract void buildProviders(List<Object> var1);
}

