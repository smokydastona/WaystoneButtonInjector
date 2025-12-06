/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.ConfirmScreen
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.player.Player
 */
package net.blay09.mods.waystones.client.gui.screen;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.network.message.InventoryButtonMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class InventoryButtonReturnConfirmScreen
extends ConfirmScreen {
    private final String waystoneName;

    public InventoryButtonReturnConfirmScreen() {
        this("");
    }

    public InventoryButtonReturnConfirmScreen(String targetWaystone) {
        super(result -> {
            if (result) {
                Balm.getNetworking().sendToServer((Object)new InventoryButtonMessage());
            }
            Minecraft.m_91087_().m_91152_(null);
        }, (Component)Component.m_237115_((String)"gui.waystones.inventory.confirm_return"), (Component)Component.m_237119_());
        this.waystoneName = InventoryButtonReturnConfirmScreen.getWaystoneName(targetWaystone);
    }

    private static String getWaystoneName(String targetWaystone) {
        if (!targetWaystone.isEmpty()) {
            return String.valueOf(ChatFormatting.GRAY) + I18n.m_118938_((String)"gui.waystones.inventory.confirm_return_bound_to", (Object[])new Object[]{targetWaystone});
        }
        IWaystone nearestWaystone = PlayerWaystoneManager.getNearestWaystone((Player)Minecraft.m_91087_().f_91074_);
        if (nearestWaystone != null) {
            return String.valueOf(ChatFormatting.GRAY) + I18n.m_118938_((String)"gui.waystones.inventory.confirm_return_bound_to", (Object[])new Object[]{nearestWaystone.getName()});
        }
        return String.valueOf(ChatFormatting.GRAY) + I18n.m_118938_((String)"gui.waystones.inventory.confirm_return.noWaystoneActive", (Object[])new Object[0]);
    }

    public void m_88315_(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        Font font = Minecraft.m_91087_().f_91062_;
        guiGraphics.m_280137_(font, this.waystoneName, this.f_96543_ / 2, 100, 0xFFFFFF);
    }
}

