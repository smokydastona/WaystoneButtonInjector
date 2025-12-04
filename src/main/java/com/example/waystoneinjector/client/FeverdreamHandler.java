package com.example.waystoneinjector.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class FeverdreamHandler {
    
    public static void handleRedirect(String serverName) {
        System.out.println("[WaystoneInjector] Received Feverdream redirect packet for server: " + serverName);
        
        // Execute on client side only
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                // Use the same redirect logic as button clicks
                // The serverName from Feverdream is just the server identifier
                // We'll construct a redirect command for it
                String command = "redirect " + serverName;
                System.out.println("[WaystoneInjector] Triggering redirect with command: " + command);
                
                // Parse and connect using the same method as ClientEvents
                String serverAddress = parseRedirectAddress(command);
                if (serverAddress != null && !serverAddress.isEmpty()) {
                    System.out.println("[WaystoneInjector] Connecting to server: " + serverAddress);
                    connectToServer(serverAddress);
                } else {
                    System.err.println("[WaystoneInjector] Could not parse server address from: " + command);
                }
            }
        });
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
    
    private static void connectToServer(String serverAddress) {
        Minecraft mc = Minecraft.getInstance();
        
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
            
            // Failsafe: If connection takes longer than 60 seconds, return to main menu
            // This prevents players from getting stuck on timeout
            new Thread(() -> {
                try {
                    Thread.sleep(60000); // Wait 60 seconds
                    
                    // Check if we're still on a connecting screen (not in-game and not at title screen)
                    mc.execute(() -> {
                        if (mc.level == null && mc.screen != null && 
                            mc.screen.getClass().getName().contains("ConnectScreen")) {
                            System.out.println("[WaystoneInjector] Connection timeout - returning to main menu");
                            mc.setScreen(new net.minecraft.client.gui.screens.TitleScreen());
                        }
                    });
                } catch (InterruptedException e) {
                    // Thread was interrupted, ignore
                }
            }, "WaystoneInjector-ConnectionTimeout").start();
        });
    }
}
