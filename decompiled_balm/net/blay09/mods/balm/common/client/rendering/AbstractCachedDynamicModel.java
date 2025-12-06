/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.math.Transformation
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.block.model.BakedQuad
 *  net.minecraft.client.renderer.block.model.ItemOverrides
 *  net.minecraft.client.renderer.block.model.ItemTransforms
 *  net.minecraft.client.renderer.texture.MissingTextureAtlasSprite
 *  net.minecraft.client.renderer.texture.TextureAtlas
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.resources.model.BakedModel
 *  net.minecraft.client.resources.model.BlockModelRotation
 *  net.minecraft.client.resources.model.Material
 *  net.minecraft.client.resources.model.ModelBakery
 *  net.minecraft.client.resources.model.ModelState
 *  net.minecraft.client.resources.model.UnbakedModel
 *  net.minecraft.core.Direction
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.state.BlockState
 *  org.apache.commons.lang3.tuple.Pair
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Matrix4f
 */
package net.blay09.mods.balm.common.client.rendering;

import com.mojang.math.Transformation;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public abstract class AbstractCachedDynamicModel
implements BakedModel {
    private final Map<String, BakedModel> cache = new HashMap<String, BakedModel>();
    private final Map<ResourceLocation, BakedModel> baseModelCache = new HashMap<ResourceLocation, BakedModel>();
    private final ModelBakery modelBakery;
    private final Function<BlockState, ResourceLocation> baseModelFunction;
    private final List<Pair<Predicate<BlockState>, BakedModel>> parts;
    private final Function<BlockState, Map<String, String>> textureMapFunction;
    private final BiConsumer<BlockState, Matrix4f> transformFunction;
    private final ResourceLocation location;
    private TextureAtlasSprite particleTexture;

    public AbstractCachedDynamicModel(ModelBakery modelBakery, Function<BlockState, ResourceLocation> baseModelFunction, @Nullable List<Pair<Predicate<BlockState>, BakedModel>> parts, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction, List<RenderType> renderTypes, ResourceLocation location) {
        this.modelBakery = modelBakery;
        this.baseModelFunction = baseModelFunction;
        this.parts = parts;
        this.textureMapFunction = textureMapFunction;
        this.transformFunction = transformFunction;
        this.location = location;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<BakedQuad> m_213637_(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        if (state != null) {
            BakedModel bakedModel;
            Matrix4f transform = BlockModelRotation.X0_Y0.m_6189_().m_252783_();
            String stateString = state.toString();
            Map<String, BakedModel> map = this.cache;
            synchronized (map) {
                bakedModel = this.cache.get(stateString);
                if (bakedModel == null) {
                    if (this.transformFunction != null) {
                        this.transformFunction.accept(state, transform);
                    }
                    BalmModels models = BalmClient.getModels();
                    ModelState modelTransform = models.getModelState(new Transformation(transform));
                    ResourceLocation baseModelLocation = this.baseModelFunction.apply(state);
                    if (this.textureMapFunction != null && !this.baseModelCache.containsKey(baseModelLocation)) {
                        UnbakedModel baseModel = models.getUnbakedModelOrMissing(baseModelLocation);
                        BakedModel bakedBaseModel = baseModel.m_7611_(models.createBaker(baseModelLocation, this::getSprite), Material::m_119204_, modelTransform, baseModelLocation);
                        this.baseModelCache.put(baseModelLocation, bakedBaseModel);
                    }
                    UnbakedModel retexturedBaseModel = this.textureMapFunction != null ? models.retexture(this.modelBakery, baseModelLocation, this.textureMapFunction.apply(state)) : models.getUnbakedModelOrMissing(baseModelLocation);
                    bakedModel = retexturedBaseModel.m_7611_(models.createBaker(this.location, this::getSprite), Material::m_119204_, modelTransform, this.location);
                    this.cache.put(stateString, bakedModel);
                    if (this.particleTexture == null && bakedModel != null) {
                        this.particleTexture = bakedModel.m_6160_();
                    }
                }
            }
            return bakedModel != null ? bakedModel.m_213637_(state, side, rand) : Collections.emptyList();
        }
        return Collections.emptyList();
    }

    public boolean m_7541_() {
        return true;
    }

    public boolean m_7539_() {
        return true;
    }

    public boolean m_7547_() {
        return false;
    }

    public boolean m_7521_() {
        return false;
    }

    public TextureAtlasSprite m_6160_() {
        return this.particleTexture != null ? this.particleTexture : new Material(TextureAtlas.f_118259_, MissingTextureAtlasSprite.m_118071_()).m_119204_();
    }

    public ItemTransforms m_7442_() {
        return ItemTransforms.f_111786_;
    }

    public ItemOverrides m_7343_() {
        return ItemOverrides.f_111734_;
    }

    public abstract List<RenderType> getBlockRenderTypes(BlockState var1, RandomSource var2);

    public abstract List<RenderType> getItemRenderTypes(ItemStack var1, boolean var2);

    private TextureAtlasSprite getSprite(ResourceLocation modelLocation, Material material) {
        return material.m_119204_();
    }
}

