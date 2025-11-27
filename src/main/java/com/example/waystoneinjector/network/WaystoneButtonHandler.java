package com.example.waystoneinjector.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;

public class WaystoneButtonHandler {
    public static void handle(ServerPlayer player, WaystoneButtonPacket pkt) {
        if (player == null) return;

        // Button 0 = Chaos Town, Button 1 = The Undergrown
        String address = pkt.getButtonId() == 0 ? "chaostowntest.modrinth.gg" : "51.222.244.61:10020";
        String cmd = "redirect @s " + address;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        // Run command as console
        server.execute(() -> {
            try {
                server.getCommands().performPrefixedCommand(
                    server.createCommandSourceStack(), 
                    cmd
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
