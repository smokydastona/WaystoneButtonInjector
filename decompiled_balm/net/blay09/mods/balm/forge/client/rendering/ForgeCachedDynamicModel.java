/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.resources.model.BakedModel
 *  net.minecraft.client.resources.model.ModelBakery
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraftforge.client.ChunkRenderTypeSet
 *  net.minecraftforge.client.model.data.ModelData
 *  org.apache.commons.lang3.tuple.Pair
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Matrix4f
 */
package net.blay09.mods.balm.forge.client.rendering;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.blay09.mods.balm.common.client.rendering.AbstractCachedDynamicModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class ForgeCachedDynamicModel
extends AbstractCachedDynamicModel {
    private final List<RenderType> renderTypes;
    private ChunkRenderTypeSet cachedChunkRenderTypeSet;

    public ForgeCachedDynamicModel(ModelBakery modelBakery, Function<BlockState, ResourceLocation> baseModelFunction, @Nullable List<Pair<Predicate<BlockState>, BakedModel>> parts, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction, List<RenderType> renderTypes, ResourceLocation location) {
        super(modelBakery, baseModelFunction, parts, textureMapFunction, transformFunction, renderTypes, location);
        this.renderTypes = renderTypes;
    }

    @Override
    public List<RenderType> getItemRenderTypes(ItemStack itemStack, boolean fabulous) {
        return this.renderTypes;
    }

    public List<RenderType> getRenderTypes(ItemStack itemStack, boolean fabulous) {
        List<RenderType> result = this.getItemRenderTypes(itemStack, fabulous);
        if (result.isEmpty()) {
            return super.getRenderTypes(itemStack, fabulous);
        }
        return result;
    }

    @Override
    public List<RenderType> getBlockRenderTypes(BlockState state, RandomSource rand) {
        return this.renderTypes;
    }

    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        if (this.cachedChunkRenderTypeSet == null) {
            List<RenderType> result = this.getBlockRenderTypes(state, rand);
            this.cachedChunkRenderTypeSet = !result.isEmpty() ? ChunkRenderTypeSet.of(result) : super.getRenderTypes(state, rand, data);
        }
        return this.cachedChunkRenderTypeSet;
    }
}

