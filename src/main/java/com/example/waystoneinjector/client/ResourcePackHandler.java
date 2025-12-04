package com.example.waystoneinjector.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;

/**
 * Handles resource pack prompts when connecting to servers
 * Automatically accepts resource packs during redirects
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "waystoneinjector")
public class ResourcePackHandler {
    
    private static boolean autoAcceptNextPack = false;
    
    /**
     * Enable auto-accept for the next resource pack prompt
     * Called before initiating a server redirect
     */
    public static void enableAutoAccept() {
        autoAcceptNextPack = true;
        System.out.println("[WaystoneInjector] Auto-accept enabled for next resource pack");
    }
    
    /**
     * Disable auto-accept after connection is established
     * Also handle spawn teleport for death redirects
     */
    @SubscribeEvent
    public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        if (autoAcceptNextPack) {
            System.out.println("[WaystoneInjector] Player logged in - resource pack auto-accept will remain active for this connection");
            // Keep it enabled for a bit in case the pack prompt comes after login
            new Thread(() -> {
                try {
                    Thread.sleep(5000); // Wait 5 seconds for resource pack prompt
                    autoAcceptNextPack = false;
                    System.out.println("[WaystoneInjector] Auto-accept disabled");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        
        // Check if this is a death redirect - if so, teleport to spawn
        if (DeathSleepEvents.isDeathRedirectActive()) {
            System.out.println("[WaystoneInjector] Death redirect completed - will teleport to spawn point");
            Minecraft mc = Minecraft.getInstance();
            new Thread(() -> {
                try {
                    Thread.sleep(1000); // Wait 1 second for world to load
                    @SuppressWarnings("null")
                    Runnable task = () -> {
                        if (mc.player != null && mc.getConnection() != null) {
                            // Execute /spawn command to go to spawn point
                            mc.player.connection.sendCommand("spawn");
                            System.out.println("[WaystoneInjector] Executed spawn command");
                        }
                        DeathSleepEvents.clearDeathRedirectFlag();
                    };
                    mc.execute(task);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    
    /**
     * Check if auto-accept is currently enabled
     */
    public static boolean shouldAutoAccept() {
        return autoAcceptNextPack;
    }
}
