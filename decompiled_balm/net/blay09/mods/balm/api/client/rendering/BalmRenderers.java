/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.color.block.BlockColor
 *  net.minecraft.client.color.item.ItemColor
 *  net.minecraft.client.model.geom.ModelLayerLocation
 *  net.minecraft.client.model.geom.builders.LayerDefinition
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.client.particle.SpriteSet
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
 */
package net.blay09.mods.balm.api.client.rendering;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
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

public interface BalmRenderers {
    default public ModelLayerLocation registerModel(ResourceLocation location, Supplier<LayerDefinition> layerDefinition) {
        return this.registerModel(location, "main", layerDefinition);
    }

    public ModelLayerLocation registerModel(ResourceLocation var1, String var2, Supplier<LayerDefinition> var3);

    default public <T extends Entity> void registerEntityRenderer(ResourceLocation identifier, Supplier<EntityType<T>> type, EntityRendererProvider<? super T> provider) {
        this.registerEntityRenderer(type, provider);
    }

    default public <T extends BlockEntity> void registerBlockEntityRenderer(ResourceLocation identifier, Supplier<BlockEntityType<T>> type, BlockEntityRendererProvider<? super T> provider) {
        this.registerBlockEntityRenderer(type, provider);
    }

    default public void registerBlockColorHandler(ResourceLocation identifier, BlockColor color, Supplier<Block[]> blocks) {
        this.registerBlockColorHandler(color, blocks);
    }

    public void registerItemColorHandler(ItemColor var1, Supplier<ItemLike[]> var2);

    public void setBlockRenderType(Supplier<Block> var1, RenderType var2);

    default public <T extends ParticleOptions> void registerParticleProvider(ResourceLocation identifier, Supplier<ParticleType<T>> particleType, Function<SpriteSet, ParticleProvider<T>> factory) {
        this.registerParticleProvider(particleType, factory);
    }

    default public <T extends ParticleOptions> void registerParticleProvider(ResourceLocation identifier, Supplier<ParticleType<T>> particleType, ParticleProvider<T> provider) {
        this.registerParticleProvider(particleType, provider);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    public <T extends Entity> void registerEntityRenderer(Supplier<EntityType<T>> var1, EntityRendererProvider<? super T> var2);

    @Deprecated(forRemoval=true, since="1.21.5")
    public <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> var1, BlockEntityRendererProvider<? super T> var2);

    @Deprecated(forRemoval=true, since="1.21.5")
    public void registerBlockColorHandler(BlockColor var1, Supplier<Block[]> var2);

    @Deprecated(forRemoval=true, since="1.21.5")
    public <T extends ParticleOptions> void registerParticleProvider(Supplier<ParticleType<T>> var1, Function<SpriteSet, ParticleProvider<T>> var2);

    @Deprecated(forRemoval=true, since="1.21.5")
    public <T extends ParticleOptions> void registerParticleProvider(Supplier<ParticleType<T>> var1, ParticleProvider<T> var2);

    public BalmRenderers scoped(String var1);
}

