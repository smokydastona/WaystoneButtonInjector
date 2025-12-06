/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.level.block.entity.BlockEntity
 */
package net.blay09.mods.balm.api.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface BalmMenuProvider
extends MenuProvider {
    default public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        BalmMenuProvider balmMenuProvider = this;
        if (balmMenuProvider instanceof BlockEntity) {
            BlockEntity blockEntity = (BlockEntity)balmMenuProvider;
            buf.m_130064_(blockEntity.m_58899_());
        }
    }
}

