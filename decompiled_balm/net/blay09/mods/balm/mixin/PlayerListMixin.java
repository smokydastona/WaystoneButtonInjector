/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Connection
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.server.players.PlayerList
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.PlayerConnectedEvent;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={PlayerList.class})
public class PlayerListMixin {
    @Inject(method={"placeNewPlayer"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/protocol/game/ClientboundPlayerAbilitiesPacket;<init>(Lnet/minecraft/world/entity/player/Abilities;)V")})
    private void handlePlayerConnection(Connection connection, ServerPlayer player, CallbackInfo callbackInfo) {
        PlayerConnectedEvent event = new PlayerConnectedEvent(player);
        Balm.getEvents().fireEvent(event);
    }
}

