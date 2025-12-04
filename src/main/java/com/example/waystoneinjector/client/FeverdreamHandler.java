package com.example.waystoneinjector.client;

import com.example.waystoneinjector.config.WaystoneConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FeverdreamHandler {
    
    // Track death counts per player (client-side)
    private static final Map<UUID, Integer> playerDeathCounts = new HashMap<>();
    
    public static void handleRedirect(String serverName) {
        System.out.println("[WaystoneInjector] Received Feverdream redirect packet for server: " + serverName);
        
        // Execute on client side only
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                String processedServerName = serverName;
                
                // Check if this is a sleep-based redirect (will be prefixed with "sleep:")
                if (processedServerName.startsWith("sleep:")) {
                    if (!WaystoneConfig.isFeverdreamSleepTpEnabled()) {
                        System.out.println("[WaystoneInjector] Sleep TP is disabled in config - ignoring sleep redirect");
                        return;
                    }
                    // Remove the "sleep:" prefix and continue with redirect
                    processedServerName = processedServerName.substring(6);
                    System.out.println("[WaystoneInjector] Sleep-based redirect detected");
                }
                
                // Check if this is a death-based redirect (will be prefixed with "death:")
                if (processedServerName.startsWith("death:")) {
                    processedServerName = processedServerName.substring(6);
                    System.out.println("[WaystoneInjector] Death-based redirect detected");
                    
                    // Check if death redirects are enabled
                    if (!WaystoneConfig.isFeverdreamDeathRedirectEnabled()) {
                        System.out.println("[WaystoneInjector] Death redirects are disabled in config - ignoring death redirect");
                        return;
                    }
                    
                    // Track death count
                    UUID playerId = mc.player.getUUID();
                    int currentDeaths = playerDeathCounts.getOrDefault(playerId, 0);
                    currentDeaths++;
                    playerDeathCounts.put(playerId, currentDeaths);
                    
                    int requiredDeaths = WaystoneConfig.getFeverdreamDeathCount();
                    System.out.println("[WaystoneInjector] Death count: " + currentDeaths + "/" + requiredDeaths);
                    
                    if (currentDeaths < requiredDeaths) {
                        System.out.println("[WaystoneInjector] Not enough deaths yet - redirect cancelled");
                        return;
                    }
                    
                    // Reset death count after triggering redirect
                    playerDeathCounts.remove(playerId);
                    System.out.println("[WaystoneInjector] Death threshold reached - proceeding with redirect");
                }
                
                final String finalServerName = processedServerName;
                
                // Try to parse as button ID first (for secret buttons 6-11)
                try {
                    int buttonId = Integer.parseInt(finalServerName);
                    if (buttonId >= 6 && buttonId <= 11) {
                        // Secret button - map to visible button (6->0, 7->1, 8->2, 9->3, 10->4, 11->5)
                        int visibleButtonIndex = buttonId - 6;
                        handleSecretButton(visibleButtonIndex);
                        return;
                    }
                } catch (NumberFormatException e) {
                    // Not a number, treat as server name
                }
                
                // Use the same redirect logic as button clicks
                // The serverName from Feverdream is just the server identifier
                // We'll construct a redirect command for it
                String command = "redirect " + finalServerName;
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
    
    // Reset death count (can be called when needed)
    public static void resetDeathCount() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                playerDeathCounts.remove(mc.player.getUUID());
                System.out.println("[WaystoneInjector] Death count reset");
            }
        });
    }
    
    private static void handleSecretButton(int buttonIndex) {
        System.out.println("[WaystoneInjector] Secret button triggered: " + buttonIndex);
        
        // Get the command from the visible button configuration
        List<String> commands = WaystoneConfig.getEnabledCommands();
        
        if (buttonIndex >= 0 && buttonIndex < commands.size()) {
            String command = commands.get(buttonIndex);
            System.out.println("[WaystoneInjector] Executing command from button " + buttonIndex + ": " + command);
            
            // Parse and connect
            String serverAddress = parseRedirectAddress(command);
            if (serverAddress != null && !serverAddress.isEmpty()) {
                System.out.println("[WaystoneInjector] Connecting to server: " + serverAddress);
                connectToServer(serverAddress);
            } else {
                System.err.println("[WaystoneInjector] Could not parse server address from: " + command);
            }
        } else {
            System.err.println("[WaystoneInjector] Invalid secret button index: " + buttonIndex + " (enabled buttons: " + commands.size() + ")");
        }
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
