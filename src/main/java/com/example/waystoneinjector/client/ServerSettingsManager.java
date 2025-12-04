package com.example.waystoneinjector.client;

import com.example.waystoneinjector.config.WaystoneConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages server settings to auto-enable resource packs for configured servers
 * Runs once on client load to ensure all button servers have resource packs enabled
 */
@OnlyIn(Dist.CLIENT)
public class ServerSettingsManager {
    
    private static boolean hasProcessedServers = false;
    private static final Set<String> configuredServers = new HashSet<>();
    
    /**
     * Called once on client tick to process server settings
     */
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (hasProcessedServers) return;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return; // Wait until world is loaded
        
        hasProcessedServers = true;
        System.out.println("[WaystoneInjector] Processing server settings for resource pack auto-accept...");
        
        // Collect all server addresses from button configs
        collectConfiguredServers();
        
        if (configuredServers.isEmpty()) {
            System.out.println("[WaystoneInjector] No servers configured in buttons, skipping server settings update");
            return;
        }
        
        // Update server list
        updateServerSettings(mc);
    }
    
    /**
     * Collect all server addresses from button configurations
     */
    private static void collectConfiguredServers() {
        // Button 1
        if (WaystoneConfig.BUTTON1_ENABLED.get()) {
            String command = WaystoneConfig.BUTTON1_COMMAND.get();
            String address = parseServerAddress(command);
            if (address != null) {
                configuredServers.add(normalizeAddress(address));
                System.out.println("[WaystoneInjector] Found button server: " + address);
            }
        }
        
        // Button 2
        if (WaystoneConfig.BUTTON2_ENABLED.get()) {
            String command = WaystoneConfig.BUTTON2_COMMAND.get();
            String address = parseServerAddress(command);
            if (address != null) {
                configuredServers.add(normalizeAddress(address));
                System.out.println("[WaystoneInjector] Found button server: " + address);
            }
        }
        
        // Button 3
        if (WaystoneConfig.BUTTON3_ENABLED.get()) {
            String command = WaystoneConfig.BUTTON3_COMMAND.get();
            String address = parseServerAddress(command);
            if (address != null) {
                configuredServers.add(normalizeAddress(address));
                System.out.println("[WaystoneInjector] Found button server: " + address);
            }
        }
        
        // Death redirects
        for (String deathServer : WaystoneConfig.getAllDeathRedirects()) {
            if (deathServer != null && !deathServer.isEmpty()) {
                configuredServers.add(normalizeAddress(deathServer));
                System.out.println("[WaystoneInjector] Found death redirect server: " + deathServer);
            }
        }
        
        // Sleep redirects
        for (String sleepServer : WaystoneConfig.getAllSleepRedirects()) {
            if (sleepServer != null && !sleepServer.isEmpty()) {
                configuredServers.add(normalizeAddress(sleepServer));
                System.out.println("[WaystoneInjector] Found sleep redirect server: " + sleepServer);
            }
        }
    }
    
    /**
     * Parse server address from redirect command
     */
    private static String parseServerAddress(String command) {
        if (command == null || command.isEmpty()) return null;
        
        String[] parts = command.trim().split("\\s+");
        if (parts.length >= 2 && parts[0].equalsIgnoreCase("redirect")) {
            if (parts[1].equals("@s") && parts.length >= 3) {
                return parts[2];
            } else {
                return parts[1];
            }
        }
        return null;
    }
    
    /**
     * Normalize server address (add default port if missing)
     */
    private static String normalizeAddress(String address) {
        if (address == null) return null;
        address = address.trim();
        if (!address.contains(":")) {
            address = address + ":25565"; // Default Minecraft port
        }
        return address.toLowerCase();
    }
    
    /**
     * Update server settings to enable resource packs
     */
    private static void updateServerSettings(Minecraft mc) {
        try {
            @SuppressWarnings("null")
            ServerList serverList = new ServerList(mc);
            serverList.load();
            
            int updatedCount = 0;
            int addedCount = 0;
            
            for (String configuredAddress : configuredServers) {
                boolean found = false;
                
                // Check if server already exists in the list
                for (int i = 0; i < serverList.size(); i++) {
                    ServerData serverData = serverList.get(i);
                    String existingAddress = normalizeAddress(serverData.ip);
                    
                    if (existingAddress.equals(configuredAddress)) {
                        found = true;
                        
                        // Update resource pack settings
                        if (serverData.getResourcePackStatus() != ServerData.ServerPackStatus.ENABLED) {
                            serverData.setResourcePackStatus(ServerData.ServerPackStatus.ENABLED);
                            updatedCount++;
                            System.out.println("[WaystoneInjector] ✓ Updated server " + configuredAddress + " to auto-accept resource packs");
                        } else {
                            System.out.println("[WaystoneInjector] ✓ Server " + configuredAddress + " already configured for resource packs");
                        }
                        break;
                    }
                }
                
                // If server not found, add it
                if (!found) {
                    String[] parts = configuredAddress.split(":");
                    String host = parts[0];
                    String displayName = host.substring(0, 1).toUpperCase() + host.substring(1);
                    
                    ServerData newServer = new ServerData(displayName, configuredAddress, false);
                    newServer.setResourcePackStatus(ServerData.ServerPackStatus.ENABLED);
                    serverList.add(newServer, false);
                    addedCount++;
                    System.out.println("[WaystoneInjector] ✓ Added new server " + configuredAddress + " with resource packs enabled");
                }
            }
            
            // Save the updated server list
            if (updatedCount > 0 || addedCount > 0) {
                serverList.save();
                System.out.println("[WaystoneInjector] ========================================");
                System.out.println("[WaystoneInjector] Server settings updated successfully!");
                System.out.println("[WaystoneInjector] - Updated: " + updatedCount + " servers");
                System.out.println("[WaystoneInjector] - Added: " + addedCount + " servers");
                System.out.println("[WaystoneInjector] All configured servers now auto-accept resource packs");
                System.out.println("[WaystoneInjector] ========================================");
            } else {
                System.out.println("[WaystoneInjector] All servers already properly configured");
            }
            
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Error updating server settings: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Reset the processed flag (for testing/reloading)
     */
    public static void reset() {
        hasProcessedServers = false;
        configuredServers.clear();
    }
}
