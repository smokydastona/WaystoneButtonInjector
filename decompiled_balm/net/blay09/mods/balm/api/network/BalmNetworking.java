/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 */
package net.blay09.mods.balm.api.network;

import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface BalmNetworking {
    public void openMenu(Player var1, MenuProvider var2);

    public void defineNetworkVersion(String var1, String var2);

    default public void allowClientAndServerOnly(String modId) {
        this.allowClientOnly(modId);
        this.allowServerOnly(modId);
    }

    public void allowClientOnly(String var1);

    public void allowServerOnly(String var1);

    public <T> void reply(T var1);

    public <T> void sendTo(Player var1, T var2);

    public <T> void sendToTracking(ServerLevel var1, BlockPos var2, T var3);

    public <T> void sendToTracking(Entity var1, T var2);

    public <T> void sendToAll(MinecraftServer var1, T var2);

    public <T> void sendToServer(T var1);

    public <T> void registerClientboundPacket(ResourceLocation var1, Class<T> var2, BiConsumer<T, FriendlyByteBuf> var3, Function<FriendlyByteBuf, T> var4, BiConsumer<Player, T> var5);

    public <T> void registerServerboundPacket(ResourceLocation var1, Class<T> var2, BiConsumer<T, FriendlyByteBuf> var3, Function<FriendlyByteBuf, T> var4, BiConsumer<ServerPlayer, T> var5);

    @Deprecated(forRemoval=true, since="1.21.5")
    default public void openGui(Player player, MenuProvider menuProvider) {
        this.openMenu(player, menuProvider);
    }
}

