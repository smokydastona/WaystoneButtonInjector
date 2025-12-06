/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.block.state.BlockState
 */
package net.blay09.mods.waystones.network.message;

import java.util.Objects;
import java.util.UUID;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.WaystoneBlock;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneProxy;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class RemoveWaystoneMessage {
    private final UUID waystoneUid;

    public RemoveWaystoneMessage(UUID waystoneUid) {
        this.waystoneUid = waystoneUid;
    }

    public static void encode(RemoveWaystoneMessage message, FriendlyByteBuf buf) {
        buf.m_130077_(message.waystoneUid);
    }

    public static RemoveWaystoneMessage decode(FriendlyByteBuf buf) {
        UUID waystoneUid = buf.m_130259_();
        return new RemoveWaystoneMessage(waystoneUid);
    }

    public static void handle(ServerPlayer player, RemoveWaystoneMessage message) {
        IWaystone backingWaystone;
        WaystoneProxy waystone = new WaystoneProxy(player.f_8924_, message.waystoneUid);
        PlayerWaystoneManager.deactivateWaystone((Player)player, waystone);
        if (waystone.isGlobal() && player.m_150110_().f_35937_ && (backingWaystone = waystone.getBackingWaystone()) instanceof Waystone) {
            BlockState state;
            ((Waystone)backingWaystone).setGlobal(false);
            ServerLevel targetWorld = Objects.requireNonNull(player.m_9236_().m_7654_()).m_129880_(backingWaystone.getDimension());
            BlockPos pos = backingWaystone.getPos();
            BlockState blockState = state = targetWorld != null ? targetWorld.m_8055_(pos) : null;
            if (targetWorld == null || !(state.m_60734_() instanceof WaystoneBlock)) {
                WaystoneManager.get(player.f_8924_).removeWaystone(backingWaystone);
                PlayerWaystoneManager.removeKnownWaystone(player.f_8924_, backingWaystone);
            }
        }
    }
}

