package com.example.waystoneinjector.client;

import com.example.waystoneinjector.config.WaystoneConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Client-side event handler for death and sleep events
 * Executes commands on death/sleep
 */
@OnlyIn(Dist.CLIENT)
public class DeathSleepEvents {
    
    // Track death counts per player (client-side)
    private static final Map<UUID, Integer> playerDeathCounts = new HashMap<>();
    
    // Track if we've already handled this death (prevent duplicate triggers)
    private static boolean deathHandled = false;
    
    @SubscribeEvent
    public static void onDeathScreenOpen(ScreenEvent.Opening event) {
        // Detect when death screen opens
        if (event.getNewScreen() instanceof DeathScreen && !deathHandled) {
            deathHandled = true; // Prevent duplicate handling
            
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                deathHandled = false;
                return;
            }
            
            LocalPlayer player = mc.player;
            if (player == null) {
                deathHandled = false;
                return;
            }
            
            System.out.println("[WaystoneInjector] Player death screen detected");
            
            // Get current server address
            String currentServer = getCurrentServerAddress(mc);
            if (currentServer == null) {
                System.out.println("[WaystoneInjector] Not connected to a server - ignoring death event");
                deathHandled = false;
                return;
            }
            
            System.out.println("[WaystoneInjector] Current server: " + currentServer);
            
            // Get the configured death command for this current server
            String deathCommand = WaystoneConfig.getDeathRedirectServer(currentServer);
            if (deathCommand == null || deathCommand.isEmpty()) {
                System.out.println("[WaystoneInjector] No death command found for server: " + currentServer);
                deathHandled = false;
                return;
            }
            
            // Track death count
            UUID playerId = player.getUUID();
            int currentDeaths = playerDeathCounts.getOrDefault(playerId, 0);
            currentDeaths++;
            playerDeathCounts.put(playerId, currentDeaths);
            
            int requiredDeaths = WaystoneConfig.getFeverdreamDeathCount();
            System.out.println("[WaystoneInjector] Death count: " + currentDeaths + "/" + requiredDeaths);
            
            if (currentDeaths < requiredDeaths) {
                System.out.println("[WaystoneInjector] Not enough deaths yet - command cancelled");
                deathHandled = false;
                return;
            }
            
            // Reset death count after triggering command
            playerDeathCounts.remove(playerId);
            System.out.println("[WaystoneInjector] Death threshold reached - executing command: " + deathCommand);
            
            // Execute command after a short delay
            new Thread(() -> {
                try {
                    Thread.sleep(1000); // Wait 1 second for death screen
                    mc.execute(() -> {
                        System.out.println("[WaystoneInjector] Executing death command");
                        executeCommand(mc, deathCommand);
                        deathHandled = false; // Reset for next death
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    
    @SubscribeEvent
    public static void onPlayerRespawn(net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent event) {
        // Only handle on client side
        if (event.getEntity().level().isClientSide() && event.getEntity() instanceof LocalPlayer) {
            LocalPlayer player = (LocalPlayer) event.getEntity();
            
            // If death screen handler already triggered, skip this
            if (deathHandled) {
                deathHandled = false; // Reset flag
                return;
            }
            
            System.out.println("[WaystoneInjector] Player respawn detected (instant respawn enabled)");
            
            Minecraft mc = Minecraft.getInstance();
            String currentServer = getCurrentServerAddress(mc);
            if (currentServer == null) {
                System.out.println("[WaystoneInjector] Not connected to a server - ignoring respawn event");
                return;
            }
            
            System.out.println("[WaystoneInjector] Current server: " + currentServer);
            
            // Get the configured death command for this current server
            String deathCommand = WaystoneConfig.getDeathRedirectServer(currentServer);
            if (deathCommand == null || deathCommand.isEmpty()) {
                System.out.println("[WaystoneInjector] No death command found for server: " + currentServer);
                return;
            }
            
            // Track death count
            UUID playerId = player.getUUID();
            int currentDeaths = playerDeathCounts.getOrDefault(playerId, 0);
            currentDeaths++;
            playerDeathCounts.put(playerId, currentDeaths);
            
            int requiredDeaths = WaystoneConfig.getFeverdreamDeathCount();
            System.out.println("[WaystoneInjector] Death count: " + currentDeaths + "/" + requiredDeaths);
            
            if (currentDeaths < requiredDeaths) {
                System.out.println("[WaystoneInjector] Not enough deaths yet - command cancelled");
                return;
            }
            
            // Reset death count after triggering command
            playerDeathCounts.remove(playerId);
            System.out.println("[WaystoneInjector] Death threshold reached - executing command: " + deathCommand);
            
            // Execute command immediately
            mc.execute(() -> {
                System.out.println("[WaystoneInjector] Executing death command from instant respawn");
                executeCommand(mc, deathCommand);
            });
        }
    }
    
    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        // Only handle on client side and only for the local player
        if (event.getEntity().level().isClientSide() && event.getEntity() instanceof LocalPlayer) {
            System.out.println("[WaystoneInjector] Player wake up detected");
            
            // Get current server address
            Minecraft mc = Minecraft.getInstance();
            String currentServer = getCurrentServerAddress(mc);
            if (currentServer == null) {
                System.out.println("[WaystoneInjector] Not connected to a server - ignoring wake up event");
                return;
            }
            
            System.out.println("[WaystoneInjector] Current server: " + currentServer);
            
            // Get the configured sleep command for this current server
            String sleepCommand = WaystoneConfig.getSleepRedirectServer(currentServer);
            if (sleepCommand == null || sleepCommand.isEmpty()) {
                System.out.println("[WaystoneInjector] No sleep command found for server: " + currentServer);
                return;
            }
            
            // Check sleep chance (0-100%)
            int sleepChance = WaystoneConfig.getSleepChance(currentServer);
            int roll = (int) (Math.random() * 100);
            System.out.println("[WaystoneInjector] Sleep chance: " + sleepChance + "%, rolled: " + roll);
            
            if (roll >= sleepChance) {
                System.out.println("[WaystoneInjector] Sleep command chance failed - not executing");
                return;
            }
            
            System.out.println("[WaystoneInjector] Sleep command triggered! Executing: " + sleepCommand);
            
            // Execute command
            mc.execute(() -> {
                executeCommand(mc, sleepCommand);
            });
        }
    }
    
    /**
     * Execute a command (either client command or server command)
     */
    @SuppressWarnings("null")
    private static void executeCommand(Minecraft mc, String command) {
        if (command.startsWith("redirect ")) {
            // Legacy support for redirect commands
            String serverAddress = ClientEvents.parseRedirectAddress(command);
            if (serverAddress != null && !serverAddress.isEmpty()) {
                ClientEvents.connectToServer(serverAddress);
            }
        } else if (mc.player != null && mc.player.connection != null) {
            // Execute as chat command (will be sent to server)
            if (!command.startsWith("/")) {
                command = "/" + command;
            }
            System.out.println("[WaystoneInjector] Sending command: " + command);
            String finalCommand = command.substring(1); // Remove leading /
            mc.player.connection.sendCommand(finalCommand);
        }
    }
    
    // Reset death count (can be called when needed)
    public static void resetDeathCount(UUID playerId) {
        playerDeathCounts.remove(playerId);
    }
    
    // Helper method to get current server address
    private static String getCurrentServerAddress(Minecraft mc) {
        var server = mc.getCurrentServer();
        if (server != null) {
            return server.ip;
        }
        return null;
    }
}
