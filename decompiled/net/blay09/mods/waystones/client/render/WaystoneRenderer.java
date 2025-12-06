/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.renderer.texture.TextureAtlas
 *  net.minecraft.client.resources.model.Material
 *  net.minecraft.core.Direction
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.block.state.properties.Property
 */
package net.blay09.mods.waystones.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Objects;
import net.blay09.mods.waystones.api.WaystoneStyle;
import net.blay09.mods.waystones.api.WaystoneStyles;
import net.blay09.mods.waystones.block.WaystoneBlock;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntity;
import net.blay09.mods.waystones.client.ModRenderers;
import net.blay09.mods.waystones.client.render.SharestoneModel;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;

public class WaystoneRenderer
implements BlockEntityRenderer<WaystoneBlockEntity> {
    private static final Material MATERIAL = new Material(TextureAtlas.f_118259_, new ResourceLocation("minecraft", "waystone_overlays/waystone_active"));
    private final SharestoneModel model;

    public WaystoneRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new SharestoneModel(context.m_173582_(ModRenderers.waystoneModel));
    }

    public void render(WaystoneBlockEntity tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        BlockState state = tileEntity.m_58900_();
        if (state.m_61143_((Property)WaystoneBlock.HALF) != DoubleBlockHalf.LOWER) {
            return;
        }
        float angle = ((Direction)state.m_61143_((Property)WaystoneBlock.FACING)).m_122435_();
        matrixStack.m_85836_();
        matrixStack.m_252880_(0.5f, 0.0f, 0.5f);
        matrixStack.m_252781_(Axis.f_252436_.m_252977_(angle));
        matrixStack.m_252781_(Axis.f_252495_.m_252977_(180.0f));
        matrixStack.m_85841_(0.5f, 0.5f, 0.5f);
        LocalPlayer player = Minecraft.m_91087_().f_91074_;
        boolean isActivated = PlayerWaystoneManager.isWaystoneActivated((Player)Objects.requireNonNull(player), tileEntity.getWaystone());
        if (isActivated) {
            matrixStack.m_85841_(1.05f, 1.05f, 1.05f);
            VertexConsumer vertexBuilder = MATERIAL.m_119194_(buffer, RenderType::m_110452_);
            int light = WaystonesConfig.getActive().client.disableTextGlow ? combinedLightIn : 0xF000F0;
            int overlay = WaystonesConfig.getActive().client.disableTextGlow ? combinedOverlayIn : OverlayTexture.f_118083_;
            WaystoneStyle style = WaystoneStyles.getStyle(state.m_60734_());
            float red = 1.0f;
            float green = 1.0f;
            float blue = 1.0f;
            float alpha = 1.0f;
            if (style != null) {
                int color = style.getRuneColor();
                red = (float)(color >> 16 & 0xFF) / 255.0f;
                green = (float)(color >> 8 & 0xFF) / 255.0f;
                blue = (float)(color & 0xFF) / 255.0f;
                alpha = (float)(color >> 24 & 0xFF) / 255.0f;
            }
            this.model.m_7695_(matrixStack, vertexBuilder, light, overlay, red, green, blue, alpha);
        }
        matrixStack.m_85849_();
    }
}

