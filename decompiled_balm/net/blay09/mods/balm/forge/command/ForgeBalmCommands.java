/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraftforge.common.MinecraftForge
 */
package net.blay09.mods.balm.forge.command;

import com.mojang.brigadier.CommandDispatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.common.MinecraftForge;

public class ForgeBalmCommands
implements BalmCommands {
    private final List<Consumer<CommandDispatcher<CommandSourceStack>>> commands = new ArrayList<Consumer<CommandDispatcher<CommandSourceStack>>>();

    public ForgeBalmCommands() {
        MinecraftForge.EVENT_BUS.addListener(event -> this.commands.forEach(it -> it.accept(event.getDispatcher())));
    }

    @Override
    public void register(Consumer<CommandDispatcher<CommandSourceStack>> initializer) {
        this.commands.add(initializer);
    }
}

