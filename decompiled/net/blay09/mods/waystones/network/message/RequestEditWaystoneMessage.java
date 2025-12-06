/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.blay09.mods.balm.api.menu.BalmMenuProvider
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.MenuType
 */
package net.blay09.mods.waystones.network.message;

import java.util.UUID;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneEditPermissions;
import net.blay09.mods.waystones.core.WaystoneProxy;
import net.blay09.mods.waystones.menu.ModMenus;
import net.blay09.mods.waystones.menu.WaystoneSettingsMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class RequestEditWaystoneMessage {
    private final UUID waystoneUid;

    public RequestEditWaystoneMessage(UUID waystoneUid) {
        this.waystoneUid = waystoneUid;
    }

    public static void encode(RequestEditWaystoneMessage message, FriendlyByteBuf buf) {
        buf.m_130077_(message.waystoneUid);
    }

    public static RequestEditWaystoneMessage decode(FriendlyByteBuf buf) {
        UUID waystoneUid = buf.m_130259_();
        return new RequestEditWaystoneMessage(waystoneUid);
    }

    public static void handle(ServerPlayer player, RequestEditWaystoneMessage message) {
        final WaystoneProxy waystone = new WaystoneProxy(player.f_8924_, message.waystoneUid);
        WaystoneEditPermissions permissions = PlayerWaystoneManager.mayEditWaystone((Player)player, player.m_9236_(), waystone);
        if (permissions != WaystoneEditPermissions.ALLOW) {
            return;
        }
        BlockPos pos = waystone.getPos();
        if (player.m_20275_((double)((float)pos.m_123341_() + 0.5f), (double)((float)pos.m_123342_() + 0.5f), (double)((float)pos.m_123343_() + 0.5f)) > 64.0) {
            return;
        }
        BalmMenuProvider containerProvider = new BalmMenuProvider(){

            public Component m_5446_() {
                return Component.m_237115_((String)"container.waystones.waystone_settings");
            }

            public AbstractContainerMenu m_7208_(int i, Inventory playerInventory, Player playerEntity) {
                return new WaystoneSettingsMenu((MenuType<WaystoneSettingsMenu>)((MenuType)ModMenus.waystoneSettings.get()), waystone, i);
            }

            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                Waystone.write(buf, waystone);
            }
        };
        Balm.getNetworking().openGui((Player)player, (MenuProvider)containerProvider);
    }
}

