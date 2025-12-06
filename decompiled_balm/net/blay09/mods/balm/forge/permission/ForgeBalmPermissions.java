/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.server.permission.PermissionAPI
 *  net.minecraftforge.server.permission.events.PermissionGatherEvent$Nodes
 *  net.minecraftforge.server.permission.nodes.PermissionDynamicContext
 *  net.minecraftforge.server.permission.nodes.PermissionDynamicContextKey
 *  net.minecraftforge.server.permission.nodes.PermissionNode
 *  net.minecraftforge.server.permission.nodes.PermissionTypes
 */
package net.blay09.mods.balm.forge.permission;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.blay09.mods.balm.api.permission.PermissionContext;
import net.blay09.mods.balm.common.permission.CommonBalmPermissions;
import net.blay09.mods.balm.common.permission.OfflinePermissionContext;
import net.blay09.mods.balm.common.permission.PlayerPermissionContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionDynamicContext;
import net.minecraftforge.server.permission.nodes.PermissionDynamicContextKey;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

public class ForgeBalmPermissions
extends CommonBalmPermissions {
    private final Map<ResourceLocation, PermissionNode<?>> nodes = new HashMap();

    public ForgeBalmPermissions() {
        MinecraftForge.EVENT_BUS.addListener(this::registerNodes);
    }

    private void registerNodes(PermissionGatherEvent.Nodes event) {
        event.addNodes(this.nodes.values());
    }

    @Override
    public void registerPermission(ResourceLocation permission, Function<PermissionContext, Boolean> defaultResolver) {
        super.registerPermission(permission, defaultResolver);
        this.nodes.put(permission, new PermissionNode(permission, PermissionTypes.BOOLEAN, (serverPlayer, uuid, permissionDynamicContexts) -> (Boolean)defaultResolver.apply((PermissionContext)((Object)(serverPlayer != null ? new PlayerPermissionContext(serverPlayer) : new OfflinePermissionContext(uuid)))), new PermissionDynamicContextKey[0]));
    }

    @Override
    public boolean hasPermission(ServerPlayer player, ResourceLocation permission) {
        PermissionNode<?> node = this.nodes.get(permission);
        if (node == null) {
            return false;
        }
        return (Boolean)PermissionAPI.getPermission((ServerPlayer)player, node, (PermissionDynamicContext[])new PermissionDynamicContext[0]);
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, ResourceLocation permission) {
        PermissionNode<?> node = this.nodes.get(permission);
        if (node == null) {
            return false;
        }
        ServerPlayer player = source.m_230896_();
        return player != null ? ((Boolean)PermissionAPI.getPermission((ServerPlayer)player, node, (PermissionDynamicContext[])new PermissionDynamicContext[0])).booleanValue() : super.hasPermission(source, permission);
    }
}

