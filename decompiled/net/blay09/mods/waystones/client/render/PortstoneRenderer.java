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
 *  net.minecraft.core.Direction
 *  net.minecraft.resources.ResourceLocation
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
import net.blay09.mods.waystones.block.PortstoneBlock;
import net.blay09.mods.waystones.block.entity.PortstoneBlockEntity;
import net.blay09.mods.waystones.client.ModRenderers;
import net.blay09.mods.waystones.client.render.PortstoneModel;
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
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;

public class PortstoneRenderer
implements BlockEntityRenderer<PortstoneBlockEntity> {
    private static final Material MATERIAL = new Material(TextureAtlas.f_118259_, new ResourceLocation("minecraft", "waystone_overlays/portstone"));
    private static ItemStack warpStoneItem;
    private final PortstoneModel model;

    public PortstoneRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new PortstoneModel(context.m_173582_(ModRenderers.portstoneModel));
    }

    public void render(PortstoneBlockEntity tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        Level level = tileEntity.m_58904_();
        BlockState state = tileEntity.m_58900_();
        if (level == null || state.m_61143_((Property)PortstoneBlock.HALF) != DoubleBlockHalf.LOWER) {
            return;
        }
        Direction facing = (Direction)state.m_61143_((Property)PortstoneBlock.FACING);
        if (warpStoneItem == null) {
            warpStoneItem = new ItemStack((ItemLike)ModItems.warpStone);
            warpStoneItem.m_41663_(Enchantments.f_44986_, 1);
        }
        poseStack.m_85836_();
        poseStack.m_252880_(0.5f, 0.0f, 0.5f);
        poseStack.m_252781_(Axis.f_252392_.m_252977_(facing.m_122435_()));
        poseStack.m_252781_(Axis.f_252495_.m_252977_(180.0f));
        poseStack.m_252880_(0.0f, -2.0f, 0.0f);
        float scale = 1.01f;
        poseStack.m_85841_(0.5f, 0.5f, 0.5f);
        poseStack.m_85841_(scale, scale, scale);
        VertexConsumer vertexBuilder = MATERIAL.m_119194_(buffer, RenderType::m_110452_);
        int light = WaystonesConfig.getActive().client.disableTextGlow ? combinedLightIn : 0xF000F0;
        int overlay = WaystonesConfig.getActive().client.disableTextGlow ? combinedOverlayIn : OverlayTexture.f_118083_;
        long gameTime = level.m_46467_();
        float min = 0.7f;
        float color = (float)Math.max((double)min, (double)min + Math.abs(Math.sin((float)gameTime / 32.0f)) * (double)(1.0f - min));
        this.model.m_7695_(poseStack, vertexBuilder, light, overlay, color, color, color, 1.0f);
        poseStack.m_85849_();
        poseStack.m_85836_();
        poseStack.m_252880_(0.5f, 1.0f, 0.5f);
        poseStack.m_252781_(Axis.f_252392_.m_252977_(facing.m_122435_()));
        poseStack.m_252880_(0.0f, 0.0f, 0.15f);
        poseStack.m_252781_(Axis.f_252495_.m_252977_(25.0f));
        poseStack.m_85841_(0.5f, 0.5f, 0.5f);
        poseStack.m_252880_(0.03125f, 0.0f, 0.0f);
        Minecraft.m_91087_().m_91291_().m_269128_(warpStoneItem, ItemDisplayContext.FIXED, combinedLightIn, combinedOverlayIn, poseStack, buffer, level, 0);
        poseStack.m_85849_();
    }
}

