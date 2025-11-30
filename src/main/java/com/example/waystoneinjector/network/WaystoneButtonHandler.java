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
        if (player == null) {
            System.err.println("[WaystoneInjector] Player is null in handler!");
            return;
        }
        
        List<String> commands = WaystoneConfig.getEnabledCommands();
        if (pkt.getButtonId() < 0 || pkt.getButtonId() >= commands.size()) {
            System.err.println("[WaystoneInjector] Invalid button ID: " + pkt.getButtonId());
            return;
        }
        
        String command = commands.get(pkt.getButtonId());
        System.out.println("[WaystoneInjector] Executing command as OP: " + command + " for player: " + player.getName().getString());
        
        MinecraftServer server = player.getServer();
        if (server != null) {
            server.execute(() -> {
                // First, send command to client for client-side execution
                Networking.CHANNEL.sendTo(new ExecuteClientCommandPacket(command), 
                    player.connection.connection, 
                    net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT);
                
                // Then execute on server with max OP permissions
                // Create command source with OP level 4 permissions (max level, same as command blocks)
                CommandSourceStack css = new CommandSourceStack(
                    player,                          // source entity
                    player.position(),               // position
                    player.getRotationVector(),      // rotation
                    player.serverLevel(),            // level
                    4,                               // permission level (4 = max OP, same as command blocks)
                    player.getName().getString(),    // name
                    player.getDisplayName(),         // display name
                    server,                          // server
                    player                           // entity
                ).withPermission(4);                 // Explicitly set permission level 4
                int result = server.getCommands().performPrefixedCommand(css, command);
                System.out.println("[WaystoneInjector] Command execution result: " + result);
            });
        } else {
            System.err.println("[WaystoneInjector] Server is null!");
        }
    }
}
