/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.client.rendering.BalmRenderers
 *  net.minecraft.client.model.geom.ModelLayerLocation
 *  net.minecraft.client.model.geom.builders.CubeDeformation
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.block.Block
 */
package net.blay09.mods.waystones.client;

import java.util.Objects;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.waystones.block.ModBlocks;
import net.blay09.mods.waystones.block.SharestoneBlock;
import net.blay09.mods.waystones.block.entity.ModBlockEntities;
import net.blay09.mods.waystones.client.render.PortstoneModel;
import net.blay09.mods.waystones.client.render.PortstoneRenderer;
import net.blay09.mods.waystones.client.render.SharestoneModel;
import net.blay09.mods.waystones.client.render.SharestoneRenderer;
import net.blay09.mods.waystones.client.render.WaystoneModel;
import net.blay09.mods.waystones.client.render.WaystoneRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModRenderers {
    public static ModelLayerLocation portstoneModel;
    public static ModelLayerLocation sharestoneModel;
    public static ModelLayerLocation waystoneModel;

    public static void initialize(BalmRenderers renderers) {
        portstoneModel = renderers.registerModel(new ResourceLocation("waystones", "portstone"), () -> PortstoneModel.createLayer(CubeDeformation.f_171458_));
        sharestoneModel = renderers.registerModel(new ResourceLocation("waystones", "sharestone"), () -> SharestoneModel.createLayer(CubeDeformation.f_171458_));
        waystoneModel = renderers.registerModel(new ResourceLocation("waystones", "waystone"), () -> WaystoneModel.createLayer(CubeDeformation.f_171458_));
        renderers.registerBlockEntityRenderer(() -> ModBlockEntities.waystone.get(), WaystoneRenderer::new);
        renderers.registerBlockEntityRenderer(() -> ModBlockEntities.sharestone.get(), SharestoneRenderer::new);
        renderers.registerBlockEntityRenderer(() -> ModBlockEntities.portstone.get(), PortstoneRenderer::new);
        renderers.registerBlockColorHandler((state, view, pos, tintIndex) -> Objects.requireNonNull(((SharestoneBlock)state.m_60734_()).getColor()).m_41071_(), () -> ModBlocks.scopedSharestones);
        renderers.registerItemColorHandler((stack, tintIndex) -> Objects.requireNonNull(((SharestoneBlock)Block.m_49814_((Item)stack.m_41720_())).getColor()).m_41071_(), () -> ModBlocks.scopedSharestones);
        renderers.setBlockRenderType(() -> ModBlocks.sharestone, RenderType.m_110463_());
    }
}

