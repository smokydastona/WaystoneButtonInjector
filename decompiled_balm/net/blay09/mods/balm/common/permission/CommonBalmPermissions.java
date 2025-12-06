/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 */
package net.blay09.mods.balm.common.permission;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.permission.PermissionContext;
import net.blay09.mods.balm.common.permission.CommandPermissionContext;
import net.blay09.mods.balm.common.permission.PlayerPermissionContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class CommonBalmPermissions
implements BalmPermissions {
    private final Map<ResourceLocation, Function<PermissionContext, Boolean>> resolvers = new HashMap<ResourceLocation, Function<PermissionContext, Boolean>>();

    @Override
    public void registerPermission(ResourceLocation permission, Function<PermissionContext, Boolean> defaultResolver) {
        this.resolvers.put(permission, defaultResolver);
    }

    @Override
    public boolean hasPermission(ServerPlayer player, ResourceLocation permission) {
        Function<PermissionContext, Boolean> node = this.resolvers.get(permission);
        if (node == null) {
            return false;
        }
        return node.apply(new PlayerPermissionContext(player));
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, ResourceLocation permission) {
        Function<PermissionContext, Boolean> node = this.resolvers.get(permission);
        if (node == null) {
            return false;
        }
        return node.apply(new CommandPermissionContext(source));
    }
}

