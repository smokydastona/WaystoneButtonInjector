package com.example.waystoneinjector.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ExecuteClientCommandPacket {
    private final String command;

    public ExecuteClientCommandPacket(String command) {
        this.command = command;
    }

    public static void encode(ExecuteClientCommandPacket pkt, FriendlyByteBuf buf) {
        buf.writeUtf(pkt.command);
    }

    public static ExecuteClientCommandPacket decode(FriendlyByteBuf buf) {
        return new ExecuteClientCommandPacket(buf.readUtf());
    }

    public static void handle(ExecuteClientCommandPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Execute on client thread to ensure we're on the correct side
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null) {
                    System.out.println("[WaystoneInjector] Client processing command: " + pkt.command);
                    
                    // Parse the redirect command to get the server address
                    String serverAddress = parseRedirectAddress(pkt.command);
                    if (serverAddress != null && !serverAddress.isEmpty()) {
                        System.out.println("[WaystoneInjector] Connecting to server: " + serverAddress);
                        // Directly disconnect and connect to new server
                        // This bypasses ALL permission checks since it's pure client-side
                        connectToServer(mc, serverAddress);
                    } else {
                        System.err.println("[WaystoneInjector] Could not parse server address from: " + pkt.command);
                    }
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
    
    private static String parseRedirectAddress(String command) {
        // Parse "redirect server.address.com" or "redirect @s server.address.com"
        String[] parts = command.trim().split("\\s+");
        if (parts.length >= 2 && parts[0].equalsIgnoreCase("redirect")) {
            // If second part is @s, use third part, otherwise use second part
            if (parts[1].equals("@s") && parts.length >= 3) {
                return parts[2];
            } else {
                return parts[1];
            }
        }
        return null;
    }
    
    private static void connectToServer(Minecraft mc, String serverAddress) {
        // Parse address and port
        String[] parts = serverAddress.split(":");
        String address = parts[0];
        int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 25565;
        
        // Schedule the connection for next tick to ensure clean disconnect
        mc.execute(() -> {
            // Disconnect from current world
            if (mc.level != null) {
                mc.level.disconnect();
            }
            mc.clearLevel();
            
            // Create server data for the connection
            net.minecraft.client.multiplayer.ServerData serverData = 
                new net.minecraft.client.multiplayer.ServerData("", serverAddress, false);
            
            // Connect to the new server using a title screen as parent
            net.minecraft.client.gui.screens.Screen parentScreen = 
                mc.screen != null ? mc.screen : new net.minecraft.client.gui.screens.TitleScreen();
            
            net.minecraft.client.gui.screens.ConnectScreen.startConnecting(
                parentScreen,
                mc,
                new net.minecraft.client.multiplayer.resolver.ServerAddress(address, port),
                serverData,
                false
            );
        });
    }
}
