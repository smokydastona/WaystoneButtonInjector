/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.minecraft.ChatFormatting
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.Difficulty
 *  net.minecraft.world.level.GameRules
 *  net.minecraft.world.level.GameRules$BooleanValue
 */
package net.blay09.mods.balm.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.io.File;
import java.util.Collection;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.common.client.IconExport;
import net.blay09.mods.balm.common.config.ConfigJsonExport;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;

public class BalmCommand {
    private static final ResourceLocation PERMISSION_BALM_DEV = new ResourceLocation("balm", "command.balm.dev");
    private static final ResourceLocation PERMISSION_BALM_EXPORT_CONFIG = new ResourceLocation("balm", "command.balm.export.config");
    private static final ResourceLocation PERMISSION_BALM_EXPORT_ICONS = new ResourceLocation("balm", "command.balm.export.icons");
    private static int balmDevCounter;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        BalmCommands.registerPermission(PERMISSION_BALM_DEV, 2);
        BalmCommands.registerPermission(PERMISSION_BALM_EXPORT_CONFIG, 4);
        BalmCommands.registerPermission(PERMISSION_BALM_EXPORT_ICONS, 4);
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.m_82127_((String)"balm").then(((LiteralArgumentBuilder)Commands.m_82127_((String)"dev").requires(BalmCommands.requirePermission(PERMISSION_BALM_DEV))).executes(context -> {
            if (Balm.isDevelopmentEnvironment() || ++balmDevCounter >= 3) {
                CommandSourceStack source = (CommandSourceStack)context.getSource();
                MinecraftServer server = source.m_81377_();
                GameRules gameRules = server.m_129900_();
                ((GameRules.BooleanValue)gameRules.m_46170_(GameRules.f_46140_)).m_46246_(false, server);
                source.m_288197_(() -> Component.m_237113_((String)"Daylight cycle disabled"), true);
                ((GameRules.BooleanValue)gameRules.m_46170_(GameRules.f_46150_)).m_46246_(false, server);
                source.m_288197_(() -> Component.m_237113_((String)"Weather cycle disabled"), true);
                ((GameRules.BooleanValue)gameRules.m_46170_(GameRules.f_46133_)).m_46246_(true, server);
                source.m_288197_(() -> Component.m_237113_((String)"Keep Inventory enabled"), true);
                ((GameRules.BooleanValue)gameRules.m_46170_(GameRules.f_46155_)).m_46246_(false, server);
                source.m_288197_(() -> Component.m_237113_((String)"Insomnia disabled"), true);
                ((GameRules.BooleanValue)gameRules.m_46170_(GameRules.f_46132_)).m_46246_(false, server);
                source.m_288197_(() -> Component.m_237113_((String)"Mob Griefing disabled"), true);
                ((GameRules.BooleanValue)gameRules.m_46170_(GameRules.f_46125_)).m_46246_(false, server);
                source.m_288197_(() -> Component.m_237113_((String)"Trader Spawning disabled"), true);
                server.m_129827_(Difficulty.PEACEFUL, true);
                source.m_288197_(() -> Component.m_237113_((String)"Difficulty set to Peaceful"), true);
                server.m_129783_().m_8606_(99999, 0, false, false);
                source.m_288197_(() -> Component.m_237113_((String)"Weather cleared"), true);
                for (ServerLevel level : server.m_129785_()) {
                    level.m_8615_(1000L);
                }
                source.m_288197_(() -> Component.m_237113_((String)"Set the time to Daytime"), true);
            } else {
                ((CommandSourceStack)context.getSource()).m_288197_(() -> Component.m_237113_((String)"This command will change several game rules and your world's difficulty. You should only use it if you know what you're doing!").m_130940_(ChatFormatting.RED), true);
            }
            return 0;
        }))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.m_82127_((String)"export").requires(BalmCommands.requireAnyPermission(PERMISSION_BALM_EXPORT_CONFIG, PERMISSION_BALM_EXPORT_ICONS))).then(((LiteralArgumentBuilder)Commands.m_82127_((String)"config").requires(BalmCommands.requirePermission(PERMISSION_BALM_EXPORT_CONFIG))).then(Commands.m_82129_((String)"mod", (ArgumentType)StringArgumentType.string()).executes(context -> {
            String mod = (String)context.getArgument("mod", String.class);
            Collection<BalmConfigSchema> schemas = Balm.getConfig().getSchemasByNamespace(mod);
            try {
                ConfigJsonExport.exportToFile(schemas, new File("exports/config/" + mod + ".json"));
            }
            catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error exporting config data class: " + mod, e);
            }
            ((CommandSourceStack)context.getSource()).m_288197_(() -> Component.m_237113_((String)("Exported config schema for " + mod)), false);
            return 0;
        })))).then(((LiteralArgumentBuilder)Commands.m_82127_((String)"icons").requires(BalmCommands.requirePermission(PERMISSION_BALM_EXPORT_ICONS))).then(Commands.m_82129_((String)"filter", (ArgumentType)StringArgumentType.greedyString()).executes(context -> {
            String filter = (String)context.getArgument("filter", String.class);
            if (Balm.getProxy().isClient()) {
                try {
                    IconExport.export(filter);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error exporting icons for " + filter, e);
                }
                ((CommandSourceStack)context.getSource()).m_288197_(() -> Component.m_237113_((String)("Exported icons for " + filter)), false);
                return 1;
            }
            return 0;
        })))));
    }
}

