/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 */
package net.blay09.mods.waystones.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Objects;
import java.util.function.Supplier;
import net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class WaystoneInventoryButton
extends Button {
    private static final ResourceLocation INVENTORY_BUTTON_TEXTURE = new ResourceLocation("waystones", "textures/gui/inventory_button.png");
    private final AbstractContainerScreen<?> parentScreen;
    private final ItemStack iconItem;
    private final ItemStack iconItemHovered;
    private final Supplier<Boolean> visiblePredicate;
    private final Supplier<Integer> xPosition;
    private final Supplier<Integer> yPosition;

    public WaystoneInventoryButton(AbstractContainerScreen<?> parentScreen, Button.OnPress pressable, Supplier<Boolean> visiblePredicate, Supplier<Integer> xPosition, Supplier<Integer> yPosition) {
        super(0, 0, 16, 16, (Component)Component.m_237119_(), pressable, Button.f_252438_);
        this.parentScreen = parentScreen;
        this.visiblePredicate = visiblePredicate;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.iconItem = new ItemStack((ItemLike)ModItems.boundScroll);
        this.iconItemHovered = new ItemStack((ItemLike)ModItems.warpScroll);
    }

    public void m_88315_(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.f_93624_ = this.visiblePredicate.get();
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
    }

    public void m_87963_(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.f_93624_) {
            this.m_252865_(((AbstractContainerScreenAccessor)this.parentScreen).getLeftPos() + this.xPosition.get());
            this.m_253211_(((AbstractContainerScreenAccessor)this.parentScreen).getTopPos() + this.yPosition.get());
            this.f_93622_ = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            LocalPlayer player = Minecraft.m_91087_().f_91074_;
            if (PlayerWaystoneManager.canUseInventoryButton((Player)Objects.requireNonNull(player))) {
                ItemStack icon = this.f_93622_ ? this.iconItemHovered : this.iconItem;
                guiGraphics.m_280480_(icon, this.m_252754_(), this.m_252907_());
                guiGraphics.m_280370_(Minecraft.m_91087_().f_91062_, icon, this.m_252754_(), this.m_252907_());
            } else {
                RenderSystem.enableBlend();
                guiGraphics.m_280246_(1.0f, 1.0f, 1.0f, 0.5f);
                guiGraphics.m_280163_(INVENTORY_BUTTON_TEXTURE, this.m_252754_(), this.m_252907_(), 0.0f, 0.0f, 16, 16, 16, 16);
                RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                RenderSystem.disableBlend();
            }
        }
    }
}

