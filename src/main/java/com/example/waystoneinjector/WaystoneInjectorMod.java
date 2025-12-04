package com.example.waystoneinjector;

import com.example.waystoneinjector.config.WaystoneConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(WaystoneInjectorMod.MODID)
public class WaystoneInjectorMod {
    public static final String MODID = "waystoneinjector";

    public WaystoneInjectorMod() {
        // Client-side only mod - only register on client
        if (FMLEnvironment.dist.isClient()) {
            // Register config
            WaystoneConfig.register();
            
            // Register client-only event handlers (static methods require class registration)
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(com.example.waystoneinjector.client.ClientEvents.class);
            
            // Register built-in death and sleep event handlers (client-side detection)
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(com.example.waystoneinjector.client.DeathSleepEvents.class);
            
            // Register resource pack handler (auto-accept during redirects)
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(com.example.waystoneinjector.client.ResourcePackHandler.class);
        }
    }
}
