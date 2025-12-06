/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.server.level.ServerPlayer
 */
package net.blay09.mods.balm.common.permission;

import java.util.Optional;
import java.util.UUID;
import net.blay09.mods.balm.api.permission.PermissionContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public record PlayerPermissionContext(ServerPlayer player) implements PermissionContext
{
    @Override
    public Optional<ServerPlayer> getPlayer() {
        return Optional.of(this.player);
    }

    @Override
    public Optional<UUID> getPlayerUUID() {
        return Optional.of(this.player.m_20148_());
    }

    @Override
    public Optional<CommandSourceStack> getCommandSource() {
        return Optional.of(this.player.m_20203_());
    }
}

