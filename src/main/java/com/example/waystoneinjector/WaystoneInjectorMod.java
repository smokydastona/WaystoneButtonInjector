package com.example.waystoneinjector;

import com.example.waystoneinjector.config.WaystoneConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(WaystoneInjectorMod.MODID)
public class WaystoneInjectorMod {
    public static final String MODID = "waystoneinjector";

    public WaystoneInjectorMod() {
        System.out.println("[WaystoneInjector] ========================================");
        System.out.println("[WaystoneInjector] MOD CONSTRUCTOR CALLED");
        System.out.println("[WaystoneInjector] Environment: " + FMLEnvironment.dist);
        System.out.println("[WaystoneInjector] ========================================");
        
        // Client-side only mod - only register on client
        if (FMLEnvironment.dist.isClient()) {
            System.out.println("[WaystoneInjector] Client-side detected, initializing...");
            
            // Register config
            System.out.println("[WaystoneInjector] Registering config...");
            WaystoneConfig.register();
            System.out.println("[WaystoneInjector] Config registered!");
            
            // Initialize config (load debug settings + validate)
            System.out.println("[WaystoneInjector] Loading and validating config...");
            WaystoneConfig.onConfigLoad();
            System.out.println("[WaystoneInjector] Config loaded and validated!");
            
            // Register screen replacement handler (Sodium-inspired approach using events)
            System.out.println("[WaystoneInjector] Registering ScreenReplacementHandler...");
            com.example.waystoneinjector.client.ScreenReplacementHandler.register();
            System.out.println("[WaystoneInjector] ScreenReplacementHandler registered!");
            
            // Register client-only event handlers (static methods require class registration)
            System.out.println("[WaystoneInjector] Registering ClientEvents...");
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(com.example.waystoneinjector.client.ClientEvents.class);
            System.out.println("[WaystoneInjector] ClientEvents registered!");
            
            // Register built-in death and sleep event handlers (client-side detection)
            System.out.println("[WaystoneInjector] Registering DeathSleepEvents...");
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(com.example.waystoneinjector.client.DeathSleepEvents.class);
            System.out.println("[WaystoneInjector] DeathSleepEvents registered!");
            
            // Register resource pack handler (auto-accept during redirects)
            System.out.println("[WaystoneInjector] Registering ResourcePackHandler...");
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(com.example.waystoneinjector.client.ResourcePackHandler.class);
            System.out.println("[WaystoneInjector] ResourcePackHandler registered!");
            
            // Register server settings manager (auto-configure resource packs)
            System.out.println("[WaystoneInjector] Registering ServerSettingsManager...");
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(com.example.waystoneinjector.client.ServerSettingsManager.class);
            System.out.println("[WaystoneInjector] ServerSettingsManager registered!");
            
            System.out.println("[WaystoneInjector] ========================================");
            System.out.println("[WaystoneInjector] MOD INITIALIZATION COMPLETE");
            System.out.println("[WaystoneInjector] ========================================");
        } else {
            System.out.println("[WaystoneInjector] Server-side detected, skipping client initialization");
        }
    }
}
