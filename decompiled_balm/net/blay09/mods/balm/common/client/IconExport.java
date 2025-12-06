/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.TextureTarget
 *  com.mojang.blaze3d.platform.Lighting
 *  com.mojang.blaze3d.platform.NativeImage
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexSorting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.flag.FeatureFlagSet
 *  net.minecraft.world.item.CreativeModeTab
 *  net.minecraft.world.item.CreativeModeTabs
 *  net.minecraft.world.item.ItemStack
 *  org.joml.Matrix4f
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.blay09.mods.balm.common.client;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IconExport {
    private static final Logger logger = LoggerFactory.getLogger(IconExport.class);

    public static void export(String filter) {
        Minecraft minecraft = Minecraft.m_91087_();
        minecraft.execute(() -> {
            TextureTarget renderTarget = null;
            try {
                renderTarget = new TextureTarget(64, 64, true, Minecraft.f_91002_);
                renderTarget.m_83931_(0.0f, 0.0f, 0.0f, 0.0f);
                CreativeModeTabs.m_269226_((FeatureFlagSet)minecraft.f_91074_.f_108617_.m_247016_(), (boolean)((Boolean)minecraft.f_91066_.m_257871_().m_231551_()), (HolderLookup.Provider)minecraft.f_91073_.m_9598_());
                int colonIndex = filter.indexOf(58);
                String filterModId = colonIndex != -1 ? filter.substring(0, colonIndex) : filter;
                String filterItemId = colonIndex != -1 ? filter.substring(colonIndex + 1) : null;
                File exportFolder = new File("exports/icons/" + filterModId);
                if (!exportFolder.exists() && !exportFolder.mkdirs()) {
                    throw new RuntimeException("Failed to create export folder: " + String.valueOf(exportFolder));
                }
                GuiGraphics guiGraphics = new GuiGraphics(minecraft, minecraft.m_91269_().m_110104_());
                for (CreativeModeTab creativeModeTab : CreativeModeTabs.m_257478_()) {
                    for (ItemStack itemStack : creativeModeTab.m_260957_()) {
                        ResourceLocation itemId = BuiltInRegistries.f_257033_.m_7981_((Object)itemStack.m_41720_());
                        if (!itemId.m_135827_().equals(filterModId) || filterItemId != null && !itemId.m_135815_().equals(filterItemId)) continue;
                        renderTarget.m_83954_(true);
                        RenderSystem.enableDepthTest();
                        renderTarget.m_83947_(false);
                        Matrix4f matrix = new Matrix4f().setOrtho(0.0f, 16.0f, 16.0f, 0.0f, 1000.0f, 21000.0f);
                        RenderSystem.setProjectionMatrix((Matrix4f)matrix, (VertexSorting)VertexSorting.f_276633_);
                        PoseStack modelViewStack = RenderSystem.getModelViewStack();
                        modelViewStack.m_85836_();
                        modelViewStack.m_252880_(0.0f, 0.0f, -11000.0f);
                        Lighting.m_84930_();
                        guiGraphics.m_280480_(itemStack, 0, 0);
                        guiGraphics.m_280262_();
                        modelViewStack.m_85849_();
                        renderTarget.m_83970_();
                        RenderSystem.disableDepthTest();
                        try (NativeImage nativeImage = new NativeImage(renderTarget.f_83915_, renderTarget.f_83916_, false);){
                            RenderSystem.bindTexture((int)renderTarget.m_83975_());
                            nativeImage.m_85045_(0, false);
                            nativeImage.m_85122_();
                            nativeImage.m_85056_(new File(exportFolder, itemId.m_135815_() + ".png"));
                        }
                    }
                }
            }
            catch (Exception e) {
                logger.error("Failed to export icons", (Throwable)e);
            }
            finally {
                if (renderTarget != null) {
                    renderTarget.m_83930_();
                }
            }
        });
    }
}

