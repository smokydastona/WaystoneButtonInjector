/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.balm.api.command;

import com.mojang.brigadier.CommandDispatcher;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.blay09.mods.balm.api.Balm;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;

public interface BalmCommands {
    public static void registerPermission(ResourceLocation permission, int permissionLevel) {
        Balm.getPermissions().registerPermission(permission, context -> context.getCommandSource().map(it -> it.m_6761_(permissionLevel)).orElse(false));
    }

    public static Predicate<CommandSourceStack> requirePermission(ResourceLocation permission) {
        return source -> Balm.getPermissions().hasPermission((CommandSourceStack)source, permission);
    }

    public static Predicate<CommandSourceStack> requireAnyPermission(ResourceLocation ... permissions) {
        return source -> Arrays.stream(permissions).anyMatch(it -> Balm.getPermissions().hasPermission((CommandSourceStack)source, (ResourceLocation)it));
    }

    public static Predicate<CommandSourceStack> requireAllPermissions(ResourceLocation ... permissions) {
        return source -> Arrays.stream(permissions).allMatch(it -> Balm.getPermissions().hasPermission((CommandSourceStack)source, (ResourceLocation)it));
    }

    public void register(Consumer<CommandDispatcher<CommandSourceStack>> var1);
}

