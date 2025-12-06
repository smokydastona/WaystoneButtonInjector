/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 */
package net.blay09.mods.waystones.network.message;

import java.util.UUID;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneEditPermissions;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneProxy;
import net.blay09.mods.waystones.core.WaystoneSyncManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class EditWaystoneMessage {
    private final UUID waystoneUid;
    private final String name;
    private final boolean isGlobal;

    public EditWaystoneMessage(UUID waystoneUid, String name, boolean isGlobal) {
        this.waystoneUid = waystoneUid;
        this.name = name;
        this.isGlobal = isGlobal;
    }

    public static void encode(EditWaystoneMessage message, FriendlyByteBuf buf) {
        buf.m_130077_(message.waystoneUid);
        buf.m_130070_(message.name);
        buf.writeBoolean(message.isGlobal);
    }

    public static EditWaystoneMessage decode(FriendlyByteBuf buf) {
        UUID waystoneUid = buf.m_130259_();
        String name = buf.m_130136_(255);
        boolean isGlobal = buf.readBoolean();
        return new EditWaystoneMessage(waystoneUid, name, isGlobal);
    }

    public static void handle(ServerPlayer player, EditWaystoneMessage message) {
        WaystoneProxy waystone = new WaystoneProxy(player.f_8924_, message.waystoneUid);
        WaystoneEditPermissions permissions = PlayerWaystoneManager.mayEditWaystone((Player)player, player.m_9236_(), waystone);
        if (permissions != WaystoneEditPermissions.ALLOW) {
            return;
        }
        BlockPos pos = waystone.getPos();
        if (player.m_20275_((double)((float)pos.m_123341_() + 0.5f), (double)((float)pos.m_123342_() + 0.5f), (double)((float)pos.m_123343_() + 0.5f)) > 64.0) {
            return;
        }
        Waystone backingWaystone = (Waystone)waystone.getBackingWaystone();
        String legalName = EditWaystoneMessage.makeNameLegal(player.f_8924_, message.name);
        backingWaystone.setName(legalName);
        if (PlayerWaystoneManager.mayEditGlobalWaystones((Player)player)) {
            if (!backingWaystone.isGlobal() && message.isGlobal) {
                PlayerWaystoneManager.activeWaystoneForEveryone(player.f_8924_, backingWaystone);
            }
            backingWaystone.setGlobal(message.isGlobal);
        }
        WaystoneManager.get(player.f_8924_).m_77762_();
        WaystoneSyncManager.sendWaystoneUpdateToAll(player.f_8924_, backingWaystone);
        player.m_6915_();
    }

    private static String makeNameLegal(MinecraftServer server, String name) {
        String inventoryButtonMode = WaystonesConfig.getActive().inventoryButton.inventoryButton;
        if (inventoryButtonMode.equals(name) && WaystoneManager.get(server).findWaystoneByName(name).isPresent()) {
            return name + "*";
        }
        return name;
    }
}

