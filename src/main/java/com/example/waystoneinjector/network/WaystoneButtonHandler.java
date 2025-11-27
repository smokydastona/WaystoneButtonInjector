package com.example.waystoneinjector.network;

import net.minecraft.server.level.ServerPlayer;

public class WaystoneButtonHandler {
    public static void handle(ServerPlayer player, WaystoneButtonPacket pkt) {
        // Commands are now executed client-side, this handler is no longer used
        // Keeping it for backwards compatibility with the packet system
    }
}
