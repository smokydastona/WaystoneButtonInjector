package com.example.waystoneinjector.network;

import com.example.waystoneinjector.config.WaystoneConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class WaystoneButtonHandler {
    public static void handle(ServerPlayer player, WaystoneButtonPacket pkt) {
        if (player == null) return;

        // Get commands from config
        List<? extends String> commands = WaystoneConfig.BUTTON_COMMANDS.get();
        int buttonId = pkt.getButtonId();
        
        if (buttonId < 0 || buttonId >= commands.size()) return;
        
        String cmd = commands.get(buttonId);

        MinecraftServer server = player.getServer();
        if (server == null) return;

        // Run command as the player
        server.execute(() -> {
            try {
                server.getCommands().performPrefixedCommand(
                    player.createCommandSourceStack(), 
                    cmd
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
