/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.renderer.texture.TextureAtlas
 *  net.minecraft.client.resources.model.Material
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.DyeColor
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.block.state.properties.Property
 */
package net.blay09.mods.waystones.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.blay09.mods.waystones.block.SharestoneBlock;
import net.blay09.mods.waystones.block.entity.SharestoneBlockEntity;
import net.blay09.mods.waystones.client.ModRenderers;
import net.blay09.mods.waystones.client.render.SharestoneModel;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;

public class SharestoneRenderer
implements BlockEntityRenderer<SharestoneBlockEntity> {
    private static final Material MATERIAL = new Material(TextureAtlas.f_118259_, new ResourceLocation("minecraft", "waystone_overlays/sharestone_color"));
    private static ItemStack warpStoneItem;
    private final SharestoneModel model;

    public SharestoneRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new SharestoneModel(context.m_173582_(ModRenderers.sharestoneModel));
    }

    public void render(SharestoneBlockEntity tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        Level level = tileEntity.m_58904_();
        BlockState state = tileEntity.m_58900_();
        if (level == null || state.m_61143_((Property)SharestoneBlock.HALF) != DoubleBlockHalf.LOWER) {
            return;
        }
        long gameTime = level.m_46467_();
        DyeColor color = ((SharestoneBlock)state.m_60734_()).getColor();
        if (color != null) {
            poseStack.m_85836_();
            poseStack.m_252880_(0.5f, 0.0f, 0.5f);
            poseStack.m_252781_(Axis.f_252495_.m_252977_(180.0f));
            poseStack.m_252880_(0.0f, -2.0f, 0.0f);
            float scale = 1.01f;
            poseStack.m_85841_(0.5f, 0.5f, 0.5f);
            poseStack.m_85841_(scale, scale, scale);
            VertexConsumer vertexBuilder = MATERIAL.m_119194_(buffer, RenderType::m_110452_);
            int light = WaystonesConfig.getActive().client.disableTextGlow ? combinedLightIn : 0xF000F0;
            int overlay = WaystonesConfig.getActive().client.disableTextGlow ? combinedOverlayIn : OverlayTexture.f_118083_;
            float[] colors = color.m_41068_();
            this.model.m_7695_(poseStack, vertexBuilder, light, overlay, colors[0], colors[1], colors[2], 1.0f);
            poseStack.m_85849_();
        }
        if (warpStoneItem == null) {
            warpStoneItem = new ItemStack((ItemLike)ModItems.warpStone);
            warpStoneItem.m_41663_(Enchantments.f_44986_, 1);
        }
        float angle = (float)gameTime / 2.0f % 360.0f;
        float offsetY = (float)Math.sin((float)gameTime / 8.0f) * 0.025f;
        poseStack.m_85836_();
        poseStack.m_252880_(0.5f, 1.0f + offsetY, 0.5f);
        poseStack.m_252781_(Axis.f_252392_.m_252977_(angle));
        poseStack.m_85841_(0.5f, 0.5f, 0.5f);
        Minecraft.m_91087_().m_91291_().m_269128_(warpStoneItem, ItemDisplayContext.FIXED, combinedLightIn, combinedOverlayIn, poseStack, buffer, level, 0);
        poseStack.m_85849_();
    }
}

