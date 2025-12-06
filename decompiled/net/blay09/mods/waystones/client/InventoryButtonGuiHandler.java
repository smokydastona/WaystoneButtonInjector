/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.blay09.mods.balm.api.client.BalmClient
 *  net.blay09.mods.balm.api.event.client.screen.ScreenDrawEvent$Post
 *  net.blay09.mods.balm.api.event.client.screen.ScreenInitEvent$Post
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
 *  net.minecraft.client.gui.screens.inventory.InventoryScreen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 */
package net.blay09.mods.waystones.client;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.event.client.screen.ScreenDrawEvent;
import net.blay09.mods.balm.api.event.client.screen.ScreenInitEvent;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.client.gui.screen.InventoryButtonReturnConfirmScreen;
import net.blay09.mods.waystones.client.gui.widget.WaystoneInventoryButton;
import net.blay09.mods.waystones.config.InventoryButtonMode;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.network.message.InventoryButtonMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class InventoryButtonGuiHandler {
    private static WaystoneInventoryButton warpButton;

    public static void initialize() {
        Balm.getEvents().onEvent(ScreenInitEvent.Post.class, event -> {
            Screen screen = event.getScreen();
            if (!(screen instanceof InventoryScreen) && !(screen instanceof CreativeModeInventoryScreen)) {
                return;
            }
            Minecraft mc = Minecraft.m_91087_();
            if (screen != mc.f_91080_) {
                return;
            }
            InventoryButtonMode inventoryButtonMode = WaystonesConfig.getActive().getInventoryButtonMode();
            if (!inventoryButtonMode.isEnabled()) {
                return;
            }
            Supplier<Integer> xPosition = screen instanceof CreativeModeInventoryScreen ? () -> WaystonesConfig.getActive().inventoryButton.creativeWarpButtonX : () -> WaystonesConfig.getActive().inventoryButton.warpButtonX;
            Supplier<Integer> yPosition = screen instanceof CreativeModeInventoryScreen ? () -> WaystonesConfig.getActive().inventoryButton.creativeWarpButtonY : () -> WaystonesConfig.getActive().inventoryButton.warpButtonY;
            warpButton = new WaystoneInventoryButton((AbstractContainerScreen)screen, button -> {
                LocalPlayer player = mc.f_91074_;
                if (player.m_150110_().f_35937_) {
                    PlayerWaystoneManager.setInventoryButtonCooldownUntil((Player)player, 0L);
                }
                if (PlayerWaystoneManager.canUseInventoryButton((Player)player)) {
                    if (inventoryButtonMode.hasNamedTarget()) {
                        mc.m_91152_((Screen)new InventoryButtonReturnConfirmScreen(inventoryButtonMode.getNamedTarget()));
                    } else if (inventoryButtonMode.isReturnToNearest()) {
                        if (PlayerWaystoneManager.getNearestWaystone((Player)player) != null) {
                            mc.m_91152_((Screen)new InventoryButtonReturnConfirmScreen());
                        }
                    } else if (inventoryButtonMode.isReturnToAny()) {
                        Balm.getNetworking().sendToServer((Object)new InventoryButtonMessage());
                    }
                } else {
                    mc.m_91106_().m_120367_((SoundInstance)SimpleSoundInstance.m_263171_((Holder)SoundEvents.f_12490_, (float)0.5f));
                }
            }, () -> {
                if (screen instanceof CreativeModeInventoryScreen) {
                    CreativeModeInventoryScreen creativeModeInventoryScreen = (CreativeModeInventoryScreen)screen;
                    return creativeModeInventoryScreen.m_258017_();
                }
                return true;
            }, xPosition, yPosition);
            BalmClient.getScreens().addRenderableWidget(screen, (AbstractWidget)warpButton);
        });
        Balm.getEvents().onEvent(ScreenDrawEvent.Post.class, event -> {
            Screen screen = event.getScreen();
            GuiGraphics guiGraphics = event.getGuiGraphics();
            int mouseX = event.getMouseX();
            int mouseY = event.getMouseY();
            if ((screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) && warpButton != null && warpButton.m_198029_()) {
                InventoryButtonMode inventoryButtonMode = WaystonesConfig.getActive().getInventoryButtonMode();
                ArrayList<Object> tooltip = new ArrayList<Object>();
                LocalPlayer player = Minecraft.m_91087_().f_91074_;
                if (player == null) {
                    return;
                }
                long timeLeft = PlayerWaystoneManager.getInventoryButtonCooldownLeft((Player)player);
                IWaystone waystone = PlayerWaystoneManager.getInventoryButtonWaystone((Player)player);
                int xpLevelCost = waystone != null ? PlayerWaystoneManager.predictExperienceLevelCost((Entity)player, waystone, WarpMode.INVENTORY_BUTTON, null) : 0;
                int secondsLeft = (int)(timeLeft / 1000L);
                if (inventoryButtonMode.hasNamedTarget()) {
                    tooltip.add(InventoryButtonGuiHandler.formatTranslation(ChatFormatting.YELLOW, "gui.waystones.inventory.return_to_waystone", new Object[0]));
                    tooltip.add(InventoryButtonGuiHandler.formatTranslation(ChatFormatting.GRAY, "tooltip.waystones.bound_to", String.valueOf(ChatFormatting.DARK_AQUA) + inventoryButtonMode.getNamedTarget()));
                    if (secondsLeft > 0) {
                        tooltip.add(Component.m_237119_());
                    }
                } else if (inventoryButtonMode.isReturnToNearest()) {
                    tooltip.add(InventoryButtonGuiHandler.formatTranslation(ChatFormatting.YELLOW, "gui.waystones.inventory.return_to_nearest_waystone", new Object[0]));
                    IWaystone nearestWaystone = PlayerWaystoneManager.getNearestWaystone((Player)player);
                    if (nearestWaystone != null) {
                        tooltip.add(InventoryButtonGuiHandler.formatTranslation(ChatFormatting.GRAY, "tooltip.waystones.bound_to", String.valueOf(ChatFormatting.DARK_AQUA) + nearestWaystone.getName()));
                    } else {
                        tooltip.add(InventoryButtonGuiHandler.formatTranslation(ChatFormatting.RED, "gui.waystones.inventory.no_waystones_activated", new Object[0]));
                    }
                    if (secondsLeft > 0) {
                        tooltip.add(Component.m_237119_());
                    }
                } else if (inventoryButtonMode.isReturnToAny()) {
                    tooltip.add(InventoryButtonGuiHandler.formatTranslation(ChatFormatting.YELLOW, "gui.waystones.inventory.return_to_waystone", new Object[0]));
                    if (PlayerWaystoneManager.getWaystones((Player)player).isEmpty()) {
                        tooltip.add(InventoryButtonGuiHandler.formatTranslation(ChatFormatting.RED, "gui.waystones.inventory.no_waystones_activated", new Object[0]));
                    }
                }
                if (xpLevelCost > 0 && player.f_36078_ < xpLevelCost) {
                    tooltip.add(InventoryButtonGuiHandler.formatTranslation(ChatFormatting.RED, "tooltip.waystones.not_enough_xp", xpLevelCost));
                }
                if (secondsLeft > 0) {
                    tooltip.add(InventoryButtonGuiHandler.formatTranslation(ChatFormatting.GOLD, "tooltip.waystones.cooldown_left", secondsLeft));
                }
                guiGraphics.m_280677_(Minecraft.m_91087_().f_91062_, tooltip, Optional.empty(), mouseX, mouseY);
            }
        });
    }

    private static Component formatTranslation(ChatFormatting formatting, String key, Object ... args) {
        MutableComponent result = Component.m_237110_((String)key, (Object[])args);
        result.m_130940_(formatting);
        return result;
    }
}

