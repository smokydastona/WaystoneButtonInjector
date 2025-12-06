/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.waystones.client.gui.widget;

import com.google.common.collect.Lists;
import java.util.List;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.client.gui.widget.ITooltipProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class RemoveWaystoneButton
extends Button
implements ITooltipProvider {
    private static final ResourceLocation BEACON = new ResourceLocation("textures/gui/container/beacon.png");
    private final List<Component> tooltip;
    private final List<Component> activeTooltip;
    private final int visibleRegionStart;
    private final int visibleRegionHeight;
    private static boolean shiftGuard;

    public RemoveWaystoneButton(int x, int y, int visibleRegionStart, int visibleRegionHeight, IWaystone waystone, Button.OnPress pressable) {
        super(x, y, 13, 13, (Component)Component.m_237119_(), pressable, Button.f_252438_);
        this.visibleRegionStart = visibleRegionStart;
        this.visibleRegionHeight = visibleRegionHeight;
        this.tooltip = Lists.newArrayList((Object[])new Component[]{Component.m_237115_((String)"gui.waystones.waystone_selection.hold_shift_to_delete")});
        this.activeTooltip = Lists.newArrayList((Object[])new Component[]{Component.m_237115_((String)"gui.waystones.waystone_selection.click_to_delete")});
        if (waystone.isGlobal()) {
            MutableComponent component = Component.m_237115_((String)"gui.waystones.waystone_selection.deleting_global_for_all");
            component.m_130940_(ChatFormatting.DARK_RED);
            this.tooltip.add((Component)component);
            this.activeTooltip.add((Component)component);
        }
    }

    public boolean m_6375_(double mouseX, double mouseY, int button) {
        if (super.m_6375_(mouseX, mouseY, button)) {
            shiftGuard = true;
            return true;
        }
        return false;
    }

    public void m_87963_(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        boolean shiftDown = Screen.m_96638_();
        if (!shiftDown) {
            shiftGuard = false;
        }
        boolean bl = this.f_93623_ = !shiftGuard && shiftDown;
        if (mouseY >= this.visibleRegionStart && mouseY < this.visibleRegionStart + this.visibleRegionHeight) {
            if (this.f_93622_ && this.f_93623_) {
                guiGraphics.m_280246_(1.0f, 1.0f, 1.0f, 1.0f);
            } else {
                guiGraphics.m_280246_(0.5f, 0.5f, 0.5f, 0.5f);
            }
            guiGraphics.m_280218_(BEACON, this.m_252754_(), this.m_252907_(), 114, 223, 13, 13);
        }
    }

    @Override
    public boolean shouldShowTooltip() {
        return this.f_93622_;
    }

    @Override
    public List<Component> getTooltipComponents() {
        return this.f_93623_ ? this.activeTooltip : this.tooltip;
    }
}

