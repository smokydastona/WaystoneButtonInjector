/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.blay09.mods.balm.api.menu.BalmMenuProvider
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 */
package net.blay09.mods.waystones.network.message;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.config.InventoryButtonMode;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class InventoryButtonMessage {
    public static void encode(InventoryButtonMessage message, FriendlyByteBuf buf) {
    }

    public static InventoryButtonMessage decode(FriendlyByteBuf buf) {
        return new InventoryButtonMessage();
    }

    public static void handle(ServerPlayer player, InventoryButtonMessage message) {
        InventoryButtonMode inventoryButtonMode = WaystonesConfig.getActive().getInventoryButtonMode();
        if (!inventoryButtonMode.isEnabled()) {
            return;
        }
        if (player == null) {
            return;
        }
        if (player.m_150110_().f_35937_) {
            PlayerWaystoneManager.setInventoryButtonCooldownUntil((Player)player, 0L);
        }
        if (!PlayerWaystoneManager.canUseInventoryButton((Player)player)) {
            return;
        }
        IWaystone waystone = PlayerWaystoneManager.getInventoryButtonWaystone((Player)player);
        if (waystone != null) {
            PlayerWaystoneManager.tryTeleportToWaystone((Entity)player, waystone, WarpMode.INVENTORY_BUTTON, null);
        } else if (inventoryButtonMode.isReturnToAny()) {
            BalmMenuProvider containerProvider = new BalmMenuProvider(){

                public Component m_5446_() {
                    return Component.m_237115_((String)"container.waystones.waystone_selection");
                }

                public AbstractContainerMenu m_7208_(int i, Inventory playerInventory, Player playerEntity) {
                    return WaystoneSelectionMenu.createWaystoneSelection(i, playerEntity, WarpMode.INVENTORY_BUTTON, null);
                }

                public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                    buf.writeByte(WarpMode.INVENTORY_BUTTON.ordinal());
                }
            };
            Balm.getNetworking().openGui((Player)player, (MenuProvider)containerProvider);
        }
    }
}

