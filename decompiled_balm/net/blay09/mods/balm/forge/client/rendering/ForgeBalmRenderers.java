/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 *  net.minecraft.client.color.block.BlockColor
 *  net.minecraft.client.color.item.ItemColor
 *  net.minecraft.client.model.geom.ModelLayerLocation
 *  net.minecraft.client.model.geom.builders.LayerDefinition
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.client.renderer.ItemBlockRenderTypes
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
 *  net.minecraft.client.renderer.entity.EntityRendererProvider
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraftforge.client.event.EntityRenderersEvent$RegisterLayerDefinitions
 *  net.minecraftforge.client.event.EntityRenderersEvent$RegisterRenderers
 *  net.minecraftforge.client.event.RegisterColorHandlersEvent$Block
 *  net.minecraftforge.client.event.RegisterColorHandlersEvent$Item
 *  net.minecraftforge.client.event.RegisterParticleProvidersEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
 */
package net.blay09.mods.balm.forge.client.rendering;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public record ForgeBalmRenderers(NamespaceResolver namespaceResolver) implements BalmRenderers
{
    @Override
    public ModelLayerLocation registerModel(ResourceLocation location, String layer, Supplier<LayerDefinition> layerDefinition) {
        ModelLayerLocation modelLayerLocation = new ModelLayerLocation(location, layer);
        this.getActiveRegistrations().layerDefinitions.put(modelLayerLocation, layerDefinition);
        return modelLayerLocation;
    }

    @Override
    public <T extends Entity> void registerEntityRenderer(Supplier<EntityType<T>> type, EntityRendererProvider<? super T> provider) {
        this.getActiveRegistrations().entityRenderers.add(Pair.of(type::get, provider));
    }

    @Override
    public <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> type, BlockEntityRendererProvider<? super T> provider) {
        this.getActiveRegistrations().blockEntityRenderers.add(Pair.of(type::get, provider));
    }

    @Override
    public void registerBlockColorHandler(BlockColor color, Supplier<Block[]> blocks) {
        this.getActiveRegistrations().blockColors.add(new ColorRegistration(color, blocks));
    }

    @Override
    public void registerItemColorHandler(ItemColor color, Supplier<ItemLike[]> items) {
        this.getActiveRegistrations().itemColors.add(new ColorRegistration(color, items));
    }

    @Override
    public void setBlockRenderType(Supplier<Block> block, RenderType renderType) {
        this.getActiveRegistrations().blockRenderTypes.add(new BlockRenderTypeRegistration(block, renderType));
    }

    @Override
    public <T extends ParticleOptions> void registerParticleProvider(Supplier<ParticleType<T>> particleType, Function<SpriteSet, ParticleProvider<T>> factory) {
        this.getActiveRegistrations().particleProviderFactories.add(new ParticleProviderFactoryRegistration<T>(particleType, factory));
    }

    @Override
    public <T extends ParticleOptions> void registerParticleProvider(Supplier<ParticleType<T>> particleType, ParticleProvider<T> provider) {
        this.getActiveRegistrations().particleProviders.add(new ParticleProviderRegistration<T>(particleType, provider));
    }

    @Override
    public BalmRenderers scoped(String modId) {
        return new ForgeBalmRenderers(new StaticNamespaceResolver(modId));
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(this.namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    public static class Registrations {
        public final Map<ModelLayerLocation, Supplier<LayerDefinition>> layerDefinitions = new HashMap<ModelLayerLocation, Supplier<LayerDefinition>>();
        public final List<Pair<Supplier<BlockEntityType<?>>, BlockEntityRendererProvider<BlockEntity>>> blockEntityRenderers = new ArrayList();
        public final List<Pair<Supplier<EntityType<?>>, EntityRendererProvider<Entity>>> entityRenderers = new ArrayList();
        public final List<ColorRegistration<BlockColor, Block>> blockColors = new ArrayList<ColorRegistration<BlockColor, Block>>();
        public final List<ColorRegistration<ItemColor, ItemLike>> itemColors = new ArrayList<ColorRegistration<ItemColor, ItemLike>>();
        public final List<ParticleProviderFactoryRegistration<?>> particleProviderFactories = new ArrayList();
        public final List<ParticleProviderRegistration<?>> particleProviders = new ArrayList();
        public final List<BlockRenderTypeRegistration> blockRenderTypes = new ArrayList<BlockRenderTypeRegistration>();

        @SubscribeEvent
        public void setupClient(FMLClientSetupEvent event) {
            event.enqueueWork(() -> this.blockRenderTypes.forEach(blockRenderType -> ItemBlockRenderTypes.setRenderLayer((Block)blockRenderType.blockSupplier.get(), (RenderType)blockRenderType.renderType())));
        }

        @SubscribeEvent
        public void initRenderers(EntityRenderersEvent.RegisterRenderers event) {
            for (Pair<Supplier<BlockEntityType<?>>, BlockEntityRendererProvider<BlockEntity>> pair : this.blockEntityRenderers) {
                event.registerBlockEntityRenderer((BlockEntityType)((Supplier)pair.getFirst()).get(), (BlockEntityRendererProvider)pair.getSecond());
            }
            for (Pair<Supplier<BlockEntityType<?>>, BlockEntityRendererProvider<BlockEntity>> pair : this.entityRenderers) {
                event.registerEntityRenderer((EntityType)((Supplier)pair.getFirst()).get(), (EntityRendererProvider)pair.getSecond());
            }
        }

        @SubscribeEvent
        public void initLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            for (Map.Entry<ModelLayerLocation, Supplier<LayerDefinition>> entry : this.layerDefinitions.entrySet()) {
                event.registerLayerDefinition(entry.getKey(), entry.getValue());
            }
        }

        @SubscribeEvent
        public void initBlockColors(RegisterColorHandlersEvent.Block event) {
            for (ColorRegistration<BlockColor, Block> blockColor : this.blockColors) {
                event.register(blockColor.color(), blockColor.objects().get());
            }
        }

        @SubscribeEvent
        public void initItemColors(RegisterColorHandlersEvent.Item event) {
            for (ColorRegistration<ItemColor, ItemLike> itemColor : this.itemColors) {
                event.register(itemColor.color(), itemColor.objects().get());
            }
        }

        @SubscribeEvent
        public void initParticleProviders(RegisterParticleProvidersEvent event) {
            for (ParticleProviderFactoryRegistration<?> particleProviderFactoryRegistration : this.particleProviderFactories) {
                this.registerParticleProviderFactory(event, particleProviderFactoryRegistration);
            }
            for (ParticleProviderRegistration particleProviderRegistration : this.particleProviders) {
                this.registerParticleProvider(event, particleProviderRegistration);
            }
        }

        private <T extends ParticleOptions> void registerParticleProviderFactory(RegisterParticleProvidersEvent event, ParticleProviderFactoryRegistration<T> registration) {
            event.registerSpriteSet(registration.particleType.get(), spriteSet -> registration.value().apply(spriteSet));
        }

        private <T extends ParticleOptions> void registerParticleProvider(RegisterParticleProvidersEvent event, ParticleProviderRegistration<T> registration) {
            event.registerSpriteSet(registration.particleType.get(), spriteSet -> registration.value());
        }
    }

    private record ColorRegistration<THandler, TObject>(THandler color, Supplier<TObject[]> objects) {
    }

    private record BlockRenderTypeRegistration(Supplier<Block> blockSupplier, RenderType renderType) {
    }

    private record ParticleProviderFactoryRegistration<T extends ParticleOptions>(Supplier<ParticleType<T>> particleType, Function<SpriteSet, ParticleProvider<T>> value) {
    }

    private record ParticleProviderRegistration<T extends ParticleOptions>(Supplier<ParticleType<T>> particleType, ParticleProvider<T> value) {
    }
}

