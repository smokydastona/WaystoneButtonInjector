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
 * Replaces the need for server-side Feverdream mod
 */
@OnlyIn(Dist.CLIENT)
public class DeathSleepEvents {
    
    // Track death counts per player (client-side)
    private static final Map<UUID, Integer> playerDeathCounts = new HashMap<>();
    
    // Flag to indicate a death redirect is in progress
    private static boolean deathRedirectActive = false;
    
    // Track if we've already handled this death (prevent duplicate triggers)
    private static boolean deathHandled = false;
    
    public static boolean isDeathRedirectActive() {
        return deathRedirectActive;
    }
    
    public static void clearDeathRedirectFlag() {
        deathRedirectActive = false;
    }
    
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
            
            System.out.println("[WaystoneInjector] Player death screen detected");
            
            // Get current server address
            String currentServer = getCurrentServerAddress(mc);
            if (currentServer == null) {
                System.out.println("[WaystoneInjector] Not connected to a server - ignoring death event");
                deathHandled = false;
                return;
            }
            
            System.out.println("[WaystoneInjector] Current server: " + currentServer);
            
            // Get the configured death server for this current server
            String deathServer = WaystoneConfig.getDeathRedirectServer(currentServer);
            if (deathServer == null || deathServer.isEmpty()) {
                System.out.println("[WaystoneInjector] No death redirect mapping found for server: " + currentServer);
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
                System.out.println("[WaystoneInjector] Not enough deaths yet - redirect cancelled");
                deathHandled = false;
                return;
            }
            
            // Reset death count after triggering redirect
            playerDeathCounts.remove(playerId);
            System.out.println("[WaystoneInjector] Death threshold reached - triggering redirect to: " + deathServer);
            
            // Set death redirect flag so we can teleport to spawn after connection
            deathRedirectActive = true;
            
            // Trigger redirect after a short delay (wait for death screen to fully load)
            new Thread(() -> {
                try {
                    Thread.sleep(1000); // Wait 1 second for death screen
                    mc.execute(() -> {
                        System.out.println("[WaystoneInjector] Executing death redirect");
                        com.example.waystoneinjector.client.ClientEvents.connectToServer(deathServer);
                        deathHandled = false; // Reset for next death
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
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
            
            // Get the configured sleep server for this current server
            String sleepServer = WaystoneConfig.getSleepRedirectServer(currentServer);
            if (sleepServer == null || sleepServer.isEmpty()) {
                System.out.println("[WaystoneInjector] No sleep redirect mapping found for server: " + currentServer);
                return;
            }
            
            // Check sleep chance (0-100%)
            int sleepChance = WaystoneConfig.getSleepChance(currentServer);
            int roll = (int) (Math.random() * 100);
            System.out.println("[WaystoneInjector] Sleep chance: " + sleepChance + "%, rolled: " + roll);
            
            if (roll >= sleepChance) {
                System.out.println("[WaystoneInjector] Sleep redirect chance failed - not redirecting");
                return;
            }
            
            System.out.println("[WaystoneInjector] Sleep redirect triggered! Redirecting to: " + sleepServer);
            
            // Trigger redirect
            mc.execute(() -> {
                com.example.waystoneinjector.client.ClientEvents.connectToServer(sleepServer);
            });
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
