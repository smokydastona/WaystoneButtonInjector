/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 */
package net.blay09.mods.waystones.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class WaystoneButton
extends Button {
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");
    private final int xpLevelCost;
    private final IWaystone waystone;

    public WaystoneButton(int x, int y, IWaystone waystone, int xpLevelCost, Button.OnPress pressable) {
        super(x, y, 200, 20, WaystoneButton.getWaystoneNameComponent(waystone), pressable, Button.f_252438_);
        LocalPlayer player = Minecraft.m_91087_().f_91074_;
        this.xpLevelCost = xpLevelCost;
        this.waystone = waystone;
        if (player == null || !PlayerWaystoneManager.mayTeleportToWaystone((Player)player, waystone)) {
            this.f_93623_ = false;
        } else if (player.f_36078_ < xpLevelCost && !player.m_150110_().f_35937_) {
            this.f_93623_ = false;
        }
    }

    private static Component getWaystoneNameComponent(IWaystone waystone) {
        String effectiveName = waystone.getName();
        if (effectiveName.isEmpty()) {
            effectiveName = I18n.m_118938_((String)"gui.waystones.waystone_selection.unnamed_waystone", (Object[])new Object[0]);
        }
        MutableComponent textComponent = Component.m_237113_((String)effectiveName);
        if (waystone.isGlobal()) {
            textComponent.m_130940_(ChatFormatting.YELLOW);
        }
        return textComponent;
    }

    public void m_87963_(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.m_87963_(guiGraphics, mouseX, mouseY, partialTicks);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft mc = Minecraft.m_91087_();
        if (this.waystone.getDimension() == mc.f_91074_.m_9236_().m_46472_() && this.m_142518_()) {
            int distance = (int)mc.f_91074_.m_20182_().m_82554_(this.waystone.getPos().m_252807_());
            String distanceStr = distance < 10000 && (mc.f_91062_.m_92852_((FormattedText)this.m_6035_()) < 120 || distance < 1000) ? distance + "m" : String.format("%.1f", Float.valueOf((float)distance / 1000.0f)).replace(",0", "").replace(".0", "") + "km";
            int xOffset = this.m_5711_() - mc.f_91062_.m_92895_(distanceStr);
            guiGraphics.m_280488_(mc.f_91062_, distanceStr, this.m_252754_() + xOffset - 4, this.m_252907_() + 6, 0xFFFFFF);
        }
        if (this.xpLevelCost > 0) {
            boolean canAfford = Objects.requireNonNull(mc.f_91074_).f_36078_ >= this.xpLevelCost || mc.f_91074_.m_150110_().f_35937_;
            guiGraphics.m_280218_(ENCHANTMENT_TABLE_GUI_TEXTURE, this.m_252754_() + 2, this.m_252907_() + 2, (Math.min(this.xpLevelCost, 3) - 1) * 16, 223 + (!canAfford ? 16 : 0), 16, 16);
            if (this.xpLevelCost > 3) {
                guiGraphics.m_280488_(mc.f_91062_, "+", this.m_252754_() + 17, this.m_252907_() + 6, 0xC8FF8F);
            }
            if (this.f_93622_ && mouseX <= this.m_252754_() + 16) {
                ArrayList<MutableComponent> tooltip = new ArrayList<MutableComponent>();
                MutableComponent levelRequirementText = Component.m_237110_((String)"gui.waystones.waystone_selection.level_requirement", (Object[])new Object[]{this.xpLevelCost});
                levelRequirementText.m_130940_(canAfford ? ChatFormatting.GREEN : ChatFormatting.RED);
                tooltip.add(levelRequirementText);
                Font font = mc.f_91062_;
                Optional optional = Optional.empty();
                Objects.requireNonNull(mc.f_91062_);
                guiGraphics.m_280677_(font, tooltip, optional, mouseX, mouseY + 9);
            }
        }
    }
}

