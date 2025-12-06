/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.logging.LogUtils
 *  com.mojang.math.Transformation
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.block.BlockModelShaper
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.resources.model.BakedModel
 *  net.minecraft.client.resources.model.Material
 *  net.minecraft.client.resources.model.ModelBaker
 *  net.minecraft.client.resources.model.ModelBakery
 *  net.minecraft.client.resources.model.ModelResourceLocation
 *  net.minecraft.client.resources.model.ModelState
 *  net.minecraft.client.resources.model.UnbakedModel
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraftforge.client.event.ModelEvent$BakingCompleted
 *  net.minecraftforge.client.event.ModelEvent$ModifyBakingResult
 *  net.minecraftforge.client.event.ModelEvent$RegisterAdditional
 *  net.minecraftforge.client.model.SimpleModelState
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Matrix4f
 *  org.slf4j.Logger
 */
package net.blay09.mods.balm.forge.client.rendering;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.math.Transformation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.blay09.mods.balm.forge.client.rendering.ForgeCachedDynamicModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.slf4j.Logger;

public record ForgeBalmModels(NamespaceResolver namespaceResolver) implements BalmModels
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<DeferredModel> modelsToBake = Collections.synchronizedList(new ArrayList());
    private static BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction;
    private static ModelBakery modelBakery;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void onBakeModels(ModelBakery modelBakery, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
        ForgeBalmModels.modelBakery = modelBakery;
        ForgeBalmModels.spriteBiFunction = spriteBiFunction;
        List<DeferredModel> list = modelsToBake;
        synchronized (list) {
            for (DeferredModel deferredModel : modelsToBake) {
                deferredModel.resolveAndSet(modelBakery, modelBakery.m_119251_(), spriteBiFunction);
            }
        }
    }

    @Override
    public DeferredObject<BakedModel> loadModel(ResourceLocation identifier) {
        DeferredModel deferredModel = new DeferredModel(identifier){

            @Override
            public BakedModel resolve(ModelBakery bakery, Map<ResourceLocation, BakedModel> modelRegistry, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
                return modelRegistry.get(this.getIdentifier());
            }
        };
        this.getActiveRegistrations().additionalModels.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> bakeModel(final ResourceLocation identifier, final UnbakedModel model) {
        DeferredModel deferredModel = new DeferredModel(identifier){

            @Override
            public BakedModel resolve(ModelBakery bakery, Map<ResourceLocation, BakedModel> modelRegistry, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
                ModelBaker baker = ForgeBalmModels.this.createBaker(identifier, spriteBiFunction);
                return model.m_7611_(baker, baker.getModelTextureGetter(), ForgeBalmModels.this.getModelState(Transformation.m_121093_()), identifier);
            }
        };
        modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> retexture(final ResourceLocation identifier, final Map<String, String> textureMap) {
        DeferredModel deferredModel = new DeferredModel(identifier){

            @Override
            public BakedModel resolve(ModelBakery bakery, Map<ResourceLocation, BakedModel> modelRegistry, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
                UnbakedModel model = ForgeBalmModels.this.retexture(bakery, identifier, textureMap);
                ModelBaker baker = ForgeBalmModels.this.createBaker(identifier, spriteBiFunction);
                return model.m_7611_(baker, baker.getModelTextureGetter(), ForgeBalmModels.this.getModelState(Transformation.m_121093_()), identifier);
            }
        };
        modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> loadDynamicModel(final ResourceLocation identifier, @Nullable Function<BlockState, ResourceLocation> modelFunction, final @Nullable Function<BlockState, Map<String, String>> textureMapFunction, final @Nullable BiConsumer<BlockState, Matrix4f> transformFunction, final List<RenderType> renderTypes) {
        final Function<BlockState, ResourceLocation> effectiveModelFunction = modelFunction != null ? modelFunction : it -> identifier;
        DeferredModel deferredModel = new DeferredModel(identifier){

            @Override
            public BakedModel resolve(ModelBakery bakery, Map<ResourceLocation, BakedModel> modelRegistry, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
                return new ForgeCachedDynamicModel(bakery, effectiveModelFunction, null, textureMapFunction, transformFunction, renderTypes, identifier);
            }
        };
        modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public void overrideModel(Supplier<Block> block, Supplier<BakedModel> model) {
        this.getActiveRegistrations().overrides.add((Pair<Supplier<Block>, Supplier<BakedModel>>)Pair.of(block, model));
    }

    @Override
    public ModelState getModelState(Transformation transformation) {
        return new SimpleModelState(transformation);
    }

    @Override
    public UnbakedModel getUnbakedModelOrMissing(ResourceLocation location) {
        return modelBakery.m_119341_(location);
    }

    @Override
    public UnbakedModel getUnbakedMissingModel() {
        return modelBakery.m_119341_((ResourceLocation)ModelBakery.f_119230_);
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(this.namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    @Override
    public ModelBaker createBaker(ResourceLocation location, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
        try {
            Class<?> clazz = Class.forName("net.minecraft.client.resources.model.ModelBakery$ModelBakerImpl");
            Constructor<?> constructor = clazz.getDeclaredConstructor(ModelBakery.class, BiFunction.class, ResourceLocation.class);
            constructor.setAccessible(true);
            return (ModelBaker)constructor.newInstance(modelBakery, spriteBiFunction, location);
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Balm failed to create model baker", e);
        }
    }

    @Override
    public BalmModels scoped(String modId) {
        return new ForgeBalmModels(new StaticNamespaceResolver(modId));
    }

    private static abstract class DeferredModel
    extends DeferredObject<BakedModel> {
        public DeferredModel(ResourceLocation identifier) {
            super(identifier);
        }

        public void resolveAndSet(ModelBakery modelBakery, Map<ResourceLocation, BakedModel> modelRegistry, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
            try {
                this.set(this.resolve(modelBakery, modelRegistry, spriteBiFunction));
            }
            catch (Exception exception) {
                LOGGER.warn("Unable to bake model: '{}':", (Object)this.getIdentifier(), (Object)exception);
                this.set((BakedModel)modelBakery.m_119251_().get(ModelBakery.f_119230_));
            }
        }

        public abstract BakedModel resolve(ModelBakery var1, Map<ResourceLocation, BakedModel> var2, BiFunction<ResourceLocation, Material, TextureAtlasSprite> var3);
    }

    public static class Registrations {
        public final List<DeferredModel> additionalModels = new ArrayList<DeferredModel>();
        public final List<Pair<Supplier<Block>, Supplier<BakedModel>>> overrides = new ArrayList<Pair<Supplier<Block>, Supplier<BakedModel>>>();

        @SubscribeEvent
        public void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
            this.additionalModels.forEach(it -> event.register(it.getIdentifier()));
        }

        @SubscribeEvent
        public void onModelBakingCompleted(ModelEvent.ModifyBakingResult event) {
            for (Pair<Supplier<Block>, Supplier<BakedModel>> override : this.overrides) {
                Block block = (Block)((Supplier)override.getFirst()).get();
                BakedModel bakedModel = (BakedModel)((Supplier)override.getSecond()).get();
                block.m_49965_().m_61056_().forEach(state -> {
                    ModelResourceLocation modelLocation = BlockModelShaper.m_110895_((BlockState)state);
                    event.getModels().put(modelLocation, bakedModel);
                });
            }
        }

        @SubscribeEvent
        public void onModelBakingCompleted(ModelEvent.BakingCompleted event) {
            for (DeferredModel deferredModel : this.additionalModels) {
                deferredModel.resolveAndSet(event.getModelBakery(), event.getModels(), spriteBiFunction);
            }
        }
    }
}

