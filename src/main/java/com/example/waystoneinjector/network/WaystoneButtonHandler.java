package com.example.waystoneinjector.network;

import com.example.waystoneinjector.config.WaystoneConfig;
import net.minecraft.server.level.ServerPlayer;

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
        System.out.println("[WaystoneInjector] Button pressed: " + pkt.getButtonId() + " by player: " + player.getName().getString());
        System.out.println("[WaystoneInjector] Sending command to client: " + command);
        
        // Send command to client for client-side execution
        // This bypasses the need for OP permissions since the client executes it
        Networking.CHANNEL.sendTo(new ExecuteClientCommandPacket(command), 
            player.connection.connection, 
            net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT);
    }
}
