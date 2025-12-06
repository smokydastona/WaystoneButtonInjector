/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 */
package net.blay09.mods.balm.common.permission;

import java.util.Optional;
import java.util.UUID;
import net.blay09.mods.balm.api.permission.PermissionContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public record CommandPermissionContext(CommandSourceStack source) implements PermissionContext
{
    @Override
    public Optional<ServerPlayer> getPlayer() {
        return Optional.ofNullable(this.source.m_230896_());
    }

    @Override
    public Optional<UUID> getPlayerUUID() {
        return Optional.ofNullable(this.source.m_230896_()).map(Entity::m_20148_);
    }

    @Override
    public Optional<CommandSourceStack> getCommandSource() {
        return Optional.of(this.source);
    }
}

