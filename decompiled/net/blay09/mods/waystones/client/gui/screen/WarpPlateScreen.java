/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 */
package net.blay09.mods.waystones.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.waystones.block.WarpPlateBlock;
import net.blay09.mods.waystones.menu.WarpPlateContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class WarpPlateScreen
extends AbstractContainerScreen<WarpPlateContainer> {
    private static final ResourceLocation WARP_PLATE_GUI_TEXTURES = new ResourceLocation("waystones", "textures/gui/menu/warp_plate.png");

    public WarpPlateScreen(WarpPlateContainer menu, Inventory playerInventory, Component title) {
        super((AbstractContainerMenu)menu, playerInventory, title);
        this.f_97727_ = 196;
        this.f_97731_ = 93;
    }

    protected void m_7286_(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        guiGraphics.m_280246_(1.0f, 1.0f, 1.0f, 1.0f);
        guiGraphics.m_280218_(WARP_PLATE_GUI_TEXTURES, this.f_97735_, this.f_97736_, 0, 0, this.f_97726_, this.f_97727_);
        guiGraphics.m_280218_(WARP_PLATE_GUI_TEXTURES, this.f_97735_ + 86, this.f_97736_ + 34, 176, 4, 4, (int)(10.0f * ((WarpPlateContainer)this.f_97732_).getAttunementProgress()));
        guiGraphics.m_280218_(WARP_PLATE_GUI_TEXTURES, this.f_97735_ + 107 - (int)(10.0f * ((WarpPlateContainer)this.f_97732_).getAttunementProgress()), this.f_97736_ + 51, 176, 0, (int)(10.0f * ((WarpPlateContainer)this.f_97732_).getAttunementProgress()), 4);
        guiGraphics.m_280218_(WARP_PLATE_GUI_TEXTURES, this.f_97735_ + 86, this.f_97736_ + 72 - (int)(10.0f * ((WarpPlateContainer)this.f_97732_).getAttunementProgress()), 176, 4, 4, (int)(10.0f * ((WarpPlateContainer)this.f_97732_).getAttunementProgress()));
        guiGraphics.m_280218_(WARP_PLATE_GUI_TEXTURES, this.f_97735_ + 69, this.f_97736_ + 51, 176, 0, (int)(10.0f * ((WarpPlateContainer)this.f_97732_).getAttunementProgress()), 4);
    }

    protected void m_280003_(GuiGraphics guiGraphics, int x, int y) {
        super.m_280003_(guiGraphics, x, y);
        Component galacticName = WarpPlateBlock.getGalacticName(((WarpPlateContainer)this.f_97732_).getWaystone());
        int width = this.f_96547_.m_92852_((FormattedText)galacticName);
        guiGraphics.m_280430_(this.f_96547_, galacticName, this.f_97726_ - width - 5, 5, -1);
    }

    public void m_88315_(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(guiGraphics, mouseX, mouseY);
    }
}

