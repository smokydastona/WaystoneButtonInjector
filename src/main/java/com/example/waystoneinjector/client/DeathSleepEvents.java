package com.example.waystoneinjector.client;

import com.example.waystoneinjector.config.WaystoneConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
 * Uses LivingDeathEvent + PlayerRespawnEvent for reliable instant-respawn detection
 */
@OnlyIn(Dist.CLIENT)
public class DeathSleepEvents {
    
    // Track death counts per player (client-side)
    private static final Map<UUID, Integer> playerDeathCounts = new HashMap<>();
    
    // Track if player actually died (works with instant respawn)
    private static boolean died = false;
    
    // Detects actual client death — works even with instant respawn enabled
    @SubscribeEvent
    public static void onClientDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof LocalPlayer) {
            died = true;
            System.out.println("[WaystoneInjector] Player death detected (LivingDeathEvent)");
        }
    }
    
    // Detects client respawn (after death OR dimension join)
    @SubscribeEvent
    public static void onClientRespawn(PlayerEvent.PlayerRespawnEvent event) {
        // Only handle client-side
        if (!event.getEntity().level().isClientSide()) {
            return;
        }
        
        if (!(event.getEntity() instanceof LocalPlayer)) {
            return;
        }
        
        if (!died) {
            // Not a real death, just dimension change
            return;
        }
        
        // Reset death flag
        died = false;
        
        System.out.println("[WaystoneInjector] Player respawn after ACTUAL DEATH (instant respawn OK)");
        
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        
        // Get current server address
        String currentServer = getCurrentServerAddress(mc);
        if (currentServer == null) {
            System.out.println("[WaystoneInjector] Not connected to a server - ignoring death event");
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
            System.out.println("[WaystoneInjector] Executing death command from respawn");
            executeCommand(mc, deathCommand);
        });
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
