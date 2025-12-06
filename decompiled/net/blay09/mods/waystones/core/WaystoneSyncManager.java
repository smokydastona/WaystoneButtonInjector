/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.core;

import java.util.List;
import java.util.stream.Collectors;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.blay09.mods.waystones.network.message.KnownWaystonesMessage;
import net.blay09.mods.waystones.network.message.PlayerWaystoneCooldownsMessage;
import net.blay09.mods.waystones.network.message.UpdateWaystoneMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class WaystoneSyncManager {
    public static void sendWaystoneUpdateToAll(@Nullable MinecraftServer server, IWaystone waystone) {
        if (server == null) {
            return;
        }
        List players = server.m_6846_().m_11314_();
        for (ServerPlayer player : players) {
            WaystoneSyncManager.sendWaystoneUpdate((Player)player, waystone);
            WaystoneSyncManager.sendActivatedWaystones((Player)player);
        }
    }

    public static void sendActivatedWaystones(Player player) {
        List<IWaystone> waystones = PlayerWaystoneManager.getWaystones(player);
        Balm.getNetworking().sendTo(player, (Object)new KnownWaystonesMessage(WaystoneTypes.WAYSTONE, waystones));
    }

    public static void sendWarpPlates(ServerPlayer player) {
        List<IWaystone> warpPlates = WaystoneManager.get(player.f_8924_).getWaystonesByType(WaystoneTypes.WARP_PLATE).collect(Collectors.toList());
        Balm.getNetworking().sendTo((Player)player, (Object)new KnownWaystonesMessage(WaystoneTypes.WARP_PLATE, warpPlates));
    }

    public static void sendWaystoneUpdate(Player player, IWaystone waystone) {
        if (!waystone.getWaystoneType().equals((Object)WaystoneTypes.WAYSTONE) || PlayerWaystoneManager.isWaystoneActivated(player, waystone)) {
            Balm.getNetworking().sendTo(player, (Object)new UpdateWaystoneMessage(waystone));
        }
    }

    public static void sendWaystoneCooldowns(Player player) {
        long inventoryButtonCooldownUntil = PlayerWaystoneManager.getInventoryButtonCooldownUntil(player);
        long warpStoneCooldownUntil = PlayerWaystoneManager.getWarpStoneCooldownUntil(player);
        Balm.getNetworking().sendTo(player, (Object)new PlayerWaystoneCooldownsMessage(inventoryButtonCooldownUntil, warpStoneCooldownUntil));
    }
}

