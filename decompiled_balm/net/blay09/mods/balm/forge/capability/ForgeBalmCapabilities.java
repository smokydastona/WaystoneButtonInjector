/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ArrayListMultimap
 *  com.google.common.collect.Multimap
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraftforge.common.capabilities.Capability
 *  net.minecraftforge.common.capabilities.CapabilityManager
 *  net.minecraftforge.common.capabilities.CapabilityToken
 *  net.minecraftforge.common.capabilities.ICapabilityProvider
 *  net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent
 *  net.minecraftforge.common.util.LazyOptional
 *  net.minecraftforge.event.AttachCapabilitiesEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.forge.capability;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.capability.CapabilityType;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

public record ForgeBalmCapabilities(NamespaceResolver namespaceResolver) implements BalmCapabilities
{
    private static final Map<ResourceLocation, Capability<?>> backingTypes = new ConcurrentHashMap();
    private static final Map<ResourceLocation, CapabilityType<?, ?, ?>> types = new ConcurrentHashMap();
    private static final List<BlockEntityProviderRegistration<?, ?>> blockEntityProviders = new CopyOnWriteArrayList();
    private static final List<BlockEntityFallbackProviderRegistration<?, ?>> fallbackBlockEntityProviders = new CopyOnWriteArrayList();
    private static Multimap<BlockEntityType<?>, BlockEntityProviderRegistration<?, ?>> flattenedBlockEntityProviders;

    @Override
    public <TApi, TContext> TApi getCapability(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, TContext context, CapabilityType<Block, TApi, TContext> type) {
        if (blockEntity != null) {
            Capability capability = (Capability)type.backingType();
            if (context == null) {
                return blockEntity.getCapability(capability).resolve().orElse(null);
            }
            if (context instanceof Direction) {
                Direction direction = (Direction)context;
                return blockEntity.getCapability(capability, direction).resolve().orElse(null);
            }
        }
        return null;
    }

    public <TApi> void preRegisterType(ResourceLocation identifier, CapabilityToken<TApi> capabilityToken) {
        this.preRegisterType(identifier, CapabilityManager.get(capabilityToken));
    }

    public <TApi> void preRegisterType(ResourceLocation identifier, Capability<TApi> capability) {
        backingTypes.put(identifier, capability);
    }

    @Override
    public <TScope, TApi, TContext> CapabilityType<TScope, TApi, TContext> registerType(ResourceLocation identifier, Class<TScope> scopeClass, Class<TApi> apiClass, Class<TContext> contextClass) {
        this.getActiveRegistrations().apiClasses.add(apiClass);
        Capability<?> backingType = backingTypes.get(identifier);
        if (backingType == null) {
            throw new IllegalStateException("You must additionally call ForgeBalmCapabilities.preRegisterType() on Forge first, as Balm cannot create a capability token dynamically.");
        }
        CapabilityType<TScope, TApi, TContext> type = new CapabilityType<TScope, TApi, TContext>(identifier, scopeClass, apiClass, contextClass, backingType);
        types.put(identifier, type);
        return type;
    }

    @Override
    public <TScope, TApi, TContext> CapabilityType<TScope, TApi, TContext> getType(ResourceLocation identifier, Class<TScope> scopeClass, Class<TApi> apiClass, Class<TContext> contextClass) {
        CapabilityType<Object, Object, Object> type = types.get(identifier);
        if (type == null) {
            Capability<?> backingType = backingTypes.get(identifier);
            if (backingType == null) {
                throw new IllegalStateException("You must call ForgeBalmCapabilities.preRegisterType() on Forge first, as Balm cannot create a capability token dynamically.");
            }
            type = new CapabilityType<TScope, TApi, TContext>(identifier, scopeClass, apiClass, contextClass, backingType);
            types.put(identifier, type);
        }
        if (type.scopeClass() != scopeClass) {
            throw new IllegalArgumentException("Incompatible scope for capability " + String.valueOf(identifier) + ", expected " + String.valueOf(type.scopeClass()) + " but got " + String.valueOf(scopeClass));
        }
        if (type.apiClass() != apiClass) {
            throw new IllegalArgumentException("Incompatible API for capability " + String.valueOf(identifier) + ", expected " + String.valueOf(type.apiClass()) + " but got " + String.valueOf(apiClass));
        }
        if (type.contextClass() != contextClass) {
            throw new IllegalArgumentException("Incompatible context for capability " + String.valueOf(identifier) + ", expected " + String.valueOf(type.contextClass()) + " but got " + String.valueOf(contextClass));
        }
        return type;
    }

    @Override
    public <TApi, TContext> void registerProvider(ResourceLocation identifier, CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider, final Supplier<List<BlockEntityType<?>>> blockEntityTypes) {
        blockEntityProviders.add(new BlockEntityProviderRegistration<TApi, TContext>(identifier, type, provider, new Supplier<Set<BlockEntityType<?>>>(){
            private Set<BlockEntityType<?>> set;

            @Override
            public Set<BlockEntityType<?>> get() {
                if (this.set == null) {
                    this.set = Set.copyOf((Collection)blockEntityTypes.get());
                }
                return this.set;
            }
        }));
        flattenedBlockEntityProviders = null;
    }

    @Override
    public <TApi, TContext> void registerFallbackBlockEntityProvider(ResourceLocation identifier, CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider) {
        fallbackBlockEntityProviders.add(new BlockEntityFallbackProviderRegistration<TApi, TContext>(identifier, type, provider));
    }

    @SubscribeEvent
    public void attachBlockEntityCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        if (flattenedBlockEntityProviders == null) {
            flattenedBlockEntityProviders = ArrayListMultimap.create();
            for (BlockEntityProviderRegistration<?, ?> blockEntityProvider : blockEntityProviders) {
                Set<BlockEntityType<?>> blockEntityTypes = blockEntityProvider.blockEntityTypes.get();
                for (BlockEntityType<?> blockEntityType : blockEntityTypes) {
                    flattenedBlockEntityProviders.put(blockEntityType, blockEntityProvider);
                }
            }
        }
        BlockEntity blockEntity = (BlockEntity)event.getObject();
        int i = 0;
        for (BlockEntityProviderRegistration blockEntityProviderRegistration : flattenedBlockEntityProviders.get((Object)blockEntity.m_58903_())) {
            event.addCapability(blockEntityProviderRegistration.identifier().m_266382_("_" + i++), (ICapabilityProvider)new BlockEntityCapabilityProvider(blockEntity, blockEntityProviderRegistration.type(), blockEntityProviderRegistration.provider()));
        }
        i = 0;
        for (BlockEntityFallbackProviderRegistration blockEntityFallbackProviderRegistration : fallbackBlockEntityProviders) {
            event.addCapability(blockEntityFallbackProviderRegistration.identifier().m_266382_("_" + i++), (ICapabilityProvider)new BlockEntityCapabilityProvider(blockEntity, blockEntityFallbackProviderRegistration.type(), blockEntityFallbackProviderRegistration.provider()));
        }
    }

    public <TApi> void addExistingType(ResourceLocation identifier, Class<TApi> apiClass, Capability<TApi> capability) {
        types.put(identifier, new CapabilityType<Block, TApi, Direction>(identifier, Block.class, apiClass, Direction.class, capability));
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(this.namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    public static class Registrations {
        private final List<Class<?>> apiClasses = new ArrayList();

        @SubscribeEvent
        public void registerCapabilities(RegisterCapabilitiesEvent event) {
            for (Class<?> apiClass : this.apiClasses) {
                event.register(apiClass);
            }
        }
    }

    record BlockEntityProviderRegistration<TApi, TContext>(ResourceLocation identifier, CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider, Supplier<Set<BlockEntityType<?>>> blockEntityTypes) {
    }

    record BlockEntityFallbackProviderRegistration<TApi, TContext>(ResourceLocation identifier, CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider) {
    }

    record BlockEntityCapabilityProvider(BlockEntity blockEntity, CapabilityType<Block, ?, ?> type, BiFunction<BlockEntity, ?, ?> provider) implements ICapabilityProvider
    {
        public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
            Object result;
            Capability capability = (Capability)this.type.backingType();
            if (cap == capability && (result = side != null && this.type.contextClass() == Direction.class ? this.provider.apply(this.blockEntity, side) : this.provider.apply(this.blockEntity, null)) != null) {
                return LazyOptional.of(() -> result);
            }
            return LazyOptional.empty();
        }
    }
}

