/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Either
 *  com.mojang.math.Transformation
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.block.model.BlockModel
 *  net.minecraft.client.renderer.block.model.BlockModel$GuiLight
 *  net.minecraft.client.renderer.block.model.ItemTransforms
 *  net.minecraft.client.renderer.texture.TextureAtlas
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.resources.model.BakedModel
 *  net.minecraft.client.resources.model.Material
 *  net.minecraft.client.resources.model.ModelBaker
 *  net.minecraft.client.resources.model.ModelBakery
 *  net.minecraft.client.resources.model.ModelState
 *  net.minecraft.client.resources.model.UnbakedModel
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Matrix4f
 */
package net.blay09.mods.balm.api.client.rendering;

import com.mojang.datafixers.util.Either;
import com.mojang.math.Transformation;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public interface BalmModels {
    public DeferredObject<BakedModel> loadModel(ResourceLocation var1);

    public DeferredObject<BakedModel> bakeModel(ResourceLocation var1, UnbakedModel var2);

    default public DeferredObject<BakedModel> loadDynamicModel(ResourceLocation identifier, @Nullable Function<BlockState, ResourceLocation> modelFunction, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction) {
        return this.loadDynamicModel(identifier, modelFunction, textureMapFunction, transformFunction, Collections.emptyList());
    }

    public DeferredObject<BakedModel> loadDynamicModel(ResourceLocation var1, @Nullable Function<BlockState, ResourceLocation> var2, @Nullable Function<BlockState, Map<String, String>> var3, @Nullable BiConsumer<BlockState, Matrix4f> var4, List<RenderType> var5);

    public DeferredObject<BakedModel> retexture(ResourceLocation var1, Map<String, String> var2);

    public void overrideModel(Supplier<Block> var1, Supplier<BakedModel> var2);

    public ModelState getModelState(Transformation var1);

    public UnbakedModel getUnbakedModelOrMissing(ResourceLocation var1);

    public UnbakedModel getUnbakedMissingModel();

    default public UnbakedModel retexture(ModelBakery bakery, ResourceLocation identifier, Map<String, String> textureMap) {
        HashMap<String, Either> replacedTexturesMapped = new HashMap<String, Either>();
        for (Map.Entry<String, String> entry : textureMap.entrySet()) {
            replacedTexturesMapped.put(entry.getKey(), Either.left((Object)new Material(TextureAtlas.f_118259_, new ResourceLocation(entry.getValue()))));
        }
        BlockModel blockModel = new BlockModel(identifier, Collections.emptyList(), replacedTexturesMapped, Boolean.valueOf(false), BlockModel.GuiLight.FRONT, ItemTransforms.f_111786_, Collections.emptyList());
        blockModel.m_5500_(arg_0 -> ((ModelBakery)bakery).m_119341_(arg_0));
        return blockModel;
    }

    public ModelBaker createBaker(ResourceLocation var1, BiFunction<ResourceLocation, Material, TextureAtlasSprite> var2);

    public BalmModels scoped(String var1);
}

