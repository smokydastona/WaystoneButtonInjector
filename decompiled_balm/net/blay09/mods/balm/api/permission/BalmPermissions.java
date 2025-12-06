/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 */
package net.blay09.mods.balm.api.permission;

import java.util.function.Function;
import net.blay09.mods.balm.api.permission.PermissionContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public interface BalmPermissions {
    public void registerPermission(ResourceLocation var1, Function<PermissionContext, Boolean> var2);

    public boolean hasPermission(ServerPlayer var1, ResourceLocation var2);

    public boolean hasPermission(CommandSourceStack var1, ResourceLocation var2);
}

