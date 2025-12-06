/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.datafixers.util.Pair
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.Container
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.common;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntityBase;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BalmBlockEntity
extends BalmBlockEntityBase
implements BalmProviderHolder {
    @Deprecated(forRemoval=true, since="1.21.5")
    private final Map<Class<?>, BalmProvider<?>> providers = new HashMap();
    @Deprecated(forRemoval=true, since="1.21.5")
    private final Map<Pair<Direction, Class<?>>, BalmProvider<?>> sidedProviders = new HashMap();
    @Deprecated(forRemoval=true, since="1.21.5")
    private boolean providersInitialized;

    public BalmBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public CompoundTag m_5995_() {
        return this.createUpdateTag();
    }

    @Nullable
    public Packet<ClientGamePacketListener> m_58483_() {
        return this.createUpdatePacket();
    }

    @Override
    @Deprecated(forRemoval=true, since="1.21.5")
    public <T> T getProvider(Class<T> clazz) {
        BalmProvider<?> found;
        if (!this.providersInitialized) {
            ArrayList<Object> providers = new ArrayList<Object>();
            this.buildProviders(providers);
            for (Object e : providers) {
                BalmProviderHolder providerHolder = (BalmProviderHolder)e;
                for (BalmProvider<?> balmProvider : providerHolder.getProviders()) {
                    this.providers.put(balmProvider.getProviderClass(), balmProvider);
                }
                for (Pair pair : providerHolder.getSidedProviders()) {
                    Direction direction = (Direction)pair.getFirst();
                    BalmProvider provider = (BalmProvider)pair.getSecond();
                    this.sidedProviders.put(Pair.of((Object)direction, provider.getProviderClass()), provider);
                }
            }
            this.providersInitialized = true;
        }
        return (found = this.providers.get(clazz)) != null ? (T)found.getInstance() : null;
    }

    @Override
    @Deprecated(forRemoval=true, since="1.21.5")
    public void buildProviders(List<Object> providers) {
        providers.add(this);
        BalmBlockEntity balmBlockEntity = this;
        if (balmBlockEntity instanceof BalmContainerProvider) {
            final BalmContainerProvider containerProvider = (BalmContainerProvider)((Object)balmBlockEntity);
            providers.add(new BalmProviderHolder(){

                @Override
                public List<BalmProvider<?>> getProviders() {
                    Container container = containerProvider.getContainer();
                    if (container != null) {
                        return Lists.newArrayList((Object[])new BalmProvider[]{new BalmProvider<Container>(Container.class, container)});
                    }
                    return Collections.emptyList();
                }

                @Override
                public List<Pair<Direction, BalmProvider<?>>> getSidedProviders() {
                    ArrayList providers = new ArrayList();
                    for (Direction direction : Direction.values()) {
                        Container container = containerProvider.getContainer(direction);
                        if (container == null) continue;
                        providers.add(Pair.of((Object)direction, new BalmProvider<Container>(Container.class, container)));
                    }
                    return providers;
                }
            });
        }
        if ((balmBlockEntity = this) instanceof BalmFluidTankProvider) {
            final BalmFluidTankProvider fluidTankProvider = (BalmFluidTankProvider)((Object)balmBlockEntity);
            providers.add(new BalmProviderHolder(){

                @Override
                public List<BalmProvider<?>> getProviders() {
                    FluidTank fluidTank = fluidTankProvider.getFluidTank();
                    if (fluidTank != null) {
                        return Lists.newArrayList((Object[])new BalmProvider[]{new BalmProvider<FluidTank>(FluidTank.class, fluidTank)});
                    }
                    return Collections.emptyList();
                }

                @Override
                public List<Pair<Direction, BalmProvider<?>>> getSidedProviders() {
                    ArrayList providers = new ArrayList();
                    for (Direction direction : Direction.values()) {
                        FluidTank fluidTank = fluidTankProvider.getFluidTank(direction);
                        if (fluidTank == null) continue;
                        providers.add(Pair.of((Object)direction, new BalmProvider<FluidTank>(FluidTank.class, fluidTank)));
                    }
                    return providers;
                }
            });
        }
        if ((balmBlockEntity = this) instanceof BalmEnergyStorageProvider) {
            final BalmEnergyStorageProvider energyStorageProvider = (BalmEnergyStorageProvider)((Object)balmBlockEntity);
            providers.add(new BalmProviderHolder(){

                @Override
                public List<BalmProvider<?>> getProviders() {
                    EnergyStorage energyStorage = energyStorageProvider.getEnergyStorage();
                    if (energyStorage != null) {
                        return Lists.newArrayList((Object[])new BalmProvider[]{new BalmProvider<EnergyStorage>(EnergyStorage.class, energyStorage)});
                    }
                    return Collections.emptyList();
                }

                @Override
                public List<Pair<Direction, BalmProvider<?>>> getSidedProviders() {
                    ArrayList providers = new ArrayList();
                    for (Direction direction : Direction.values()) {
                        EnergyStorage energyStorage = energyStorageProvider.getEnergyStorage(direction);
                        if (energyStorage == null) continue;
                        providers.add(Pair.of((Object)direction, new BalmProvider<EnergyStorage>(EnergyStorage.class, energyStorage)));
                    }
                    return providers;
                }
            });
        }
    }

    public void sync() {
        if (this.m_58904_() != null && !this.m_58904_().f_46443_) {
            ((ServerLevel)this.m_58904_()).m_7726_().m_8450_(this.m_58899_());
        }
    }

    public Packet<ClientGamePacketListener> createUpdatePacket() {
        return ClientboundBlockEntityDataPacket.m_195642_((BlockEntity)this, this::createUpdateTag);
    }

    public CompoundTag createUpdateTag() {
        return this.createUpdateTag(this);
    }

    private CompoundTag createUpdateTag(BlockEntity blockEntity) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BalmBlockEntity) {
            BalmBlockEntity balmBlockEntity = (BalmBlockEntity)blockEntity;
            balmBlockEntity.writeUpdateTag(tag);
        }
        return tag;
    }

    protected void writeUpdateTag(CompoundTag tag) {
    }
}

