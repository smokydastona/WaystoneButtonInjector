/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.event.PlayerLoginEvent
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 */
package net.blay09.mods.waystones.handler;

import java.util.List;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneSyncManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class LoginHandler {
    public static void onPlayerLogin(PlayerLoginEvent event) {
        ServerPlayer player = event.getPlayer();
        List<IWaystone> globalWaystones = WaystoneManager.get(player.f_8924_).getGlobalWaystones();
        for (IWaystone waystone : globalWaystones) {
            if (PlayerWaystoneManager.isWaystoneActivated((Player)player, waystone)) continue;
            PlayerWaystoneManager.activateWaystone((Player)player, waystone);
        }
        WaystoneSyncManager.sendActivatedWaystones((Player)player);
        WaystoneSyncManager.sendWarpPlates(player);
        WaystoneSyncManager.sendWaystoneCooldowns((Player)player);
    }
}

