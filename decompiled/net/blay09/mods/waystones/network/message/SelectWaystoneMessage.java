/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 */
package net.blay09.mods.waystones.network.message;

import java.util.UUID;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WaystoneProxy;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class SelectWaystoneMessage {
    private final UUID waystoneUid;

    public SelectWaystoneMessage(UUID waystoneUid) {
        this.waystoneUid = waystoneUid;
    }

    public static void encode(SelectWaystoneMessage message, FriendlyByteBuf buf) {
        buf.m_130077_(message.waystoneUid);
    }

    public static SelectWaystoneMessage decode(FriendlyByteBuf buf) {
        UUID waystoneUid = buf.m_130259_();
        return new SelectWaystoneMessage(waystoneUid);
    }

    public static void handle(ServerPlayer player, SelectWaystoneMessage message) {
        if (!(player.f_36096_ instanceof WaystoneSelectionMenu)) {
            return;
        }
        WaystoneProxy waystone = new WaystoneProxy(player.f_8924_, message.waystoneUid);
        WaystoneSelectionMenu container = (WaystoneSelectionMenu)player.f_36096_;
        PlayerWaystoneManager.tryTeleportToWaystone((Entity)player, waystone, container.getWarpMode(), container.getWaystoneFrom());
        player.m_6915_();
    }
}

