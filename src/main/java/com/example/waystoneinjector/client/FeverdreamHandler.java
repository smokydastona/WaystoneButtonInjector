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
            
            // Use multiplayer screen as parent so failed connections return to server list
            net.minecraft.client.gui.screens.Screen parentScreen = 
                new net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen(
                    new net.minecraft.client.gui.screens.TitleScreen());
            
            net.minecraft.client.gui.screens.ConnectScreen.startConnecting(
                parentScreen,
                mc,
                new net.minecraft.client.multiplayer.resolver.ServerAddress(address, port),
                serverData,
                false
            );
            
            // Failsafe: Monitor connection status and return to multiplayer screen on failure/timeout
            new Thread(() -> {
                try {
                    // Check every 2 seconds for up to 60 seconds
                    for (int i = 0; i < 30; i++) {
                        Thread.sleep(2000);
                        
                        final int iteration = i;
                        mc.execute(() -> {
                            // If we're in game, connection succeeded - stop monitoring
                            if (mc.level != null) {
                                return;
                            }
                            
                            // If screen is null or not a connecting screen, connection failed
                            if (mc.screen == null || 
                                !mc.screen.getClass().getName().contains("ConnectScreen")) {
                                System.out.println("[WaystoneInjector] Connection failed - ensuring multiplayer screen");
                                // Make sure we're on the multiplayer screen, not stuck on blank screen
                                if (mc.screen == null || mc.screen instanceof net.minecraft.client.gui.screens.TitleScreen) {
                                    mc.setScreen(new net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen(
                                        new net.minecraft.client.gui.screens.TitleScreen()));
                                }
                                return;
                            }
                            
                            // After 60 seconds (30 iterations), force return to multiplayer screen
                            if (iteration >= 29) {
                                System.out.println("[WaystoneInjector] Connection timeout - returning to multiplayer screen");
                                mc.setScreen(new net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen(
                                    new net.minecraft.client.gui.screens.TitleScreen()));
                            }
                        });
                        
                        // If we're in game, stop the monitoring thread
                        if (mc.level != null) {
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    // Thread was interrupted, ignore
                }
            }, "WaystoneInjector-ConnectionMonitor").start();
        });
    }
}
