/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.server.level.ServerPlayer
 */
package net.blay09.mods.balm.api.permission;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public interface PermissionContext {
    public Optional<ServerPlayer> getPlayer();

    public Optional<UUID> getPlayerUUID();

    public Optional<CommandSourceStack> getCommandSource();
}

