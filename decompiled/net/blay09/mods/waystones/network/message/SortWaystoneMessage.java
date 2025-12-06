/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 */
package net.blay09.mods.waystones.network.message;

import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class SortWaystoneMessage {
    private final int index;
    private final int otherIndex;

    public SortWaystoneMessage(int index, int otherIndex) {
        this.index = index;
        this.otherIndex = otherIndex;
    }

    public static void encode(SortWaystoneMessage message, FriendlyByteBuf buf) {
        buf.m_130130_(message.index);
        buf.m_130130_(message.otherIndex);
    }

    public static SortWaystoneMessage decode(FriendlyByteBuf buf) {
        int index = buf.m_130242_();
        int otherIndex = buf.m_130242_();
        return new SortWaystoneMessage(index, otherIndex);
    }

    public static void handle(ServerPlayer player, SortWaystoneMessage message) {
        if (player == null) {
            return;
        }
        PlayerWaystoneManager.swapWaystoneSorting((Player)player, message.index, message.otherIndex);
    }
}

