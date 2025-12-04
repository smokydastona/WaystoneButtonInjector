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
            
            // Register Feverdream packet listener
            com.example.waystoneinjector.network.FeverdreamNetworking.register();
            
            // Register client-only event handlers
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.example.waystoneinjector.client.ClientEvents());
        }
    }
}
