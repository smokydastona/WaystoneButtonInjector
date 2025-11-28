package com.example.waystoneinjector.network;

import com.example.waystoneinjector.config.WaystoneConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class WaystoneButtonHandler {
    public static void handle(ServerPlayer player, WaystoneButtonPacket pkt) {
        if (player == null) return;
        
        List<String> commands = WaystoneConfig.getEnabledCommands();
        if (pkt.getButtonId() < 0 || pkt.getButtonId() >= commands.size()) return;
        
        String command = commands.get(pkt.getButtonId());
        MinecraftServer server = player.getServer();
        if (server != null) {
            server.execute(() -> {
                // Create command source with OP level 2 permissions, positioned at player
                CommandSourceStack css = new CommandSourceStack(
                    player,                          // source entity
                    player.position(),               // position
                    player.getRotationVector(),      // rotation
                    player.serverLevel(),            // level
                    2,                               // permission level (2 = OP)
                    player.getName().getString(),    // name
                    player.getDisplayName(),         // display name
                    server,                          // server
                    player                           // entity
                );
                server.getCommands().performPrefixedCommand(css, command);
            });
        }
    }
}
