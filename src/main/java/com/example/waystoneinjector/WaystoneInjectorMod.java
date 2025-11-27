package com.example.waystoneinjector;

import com.example.waystoneinjector.config.WaystoneConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WaystoneInjectorMod.MODID)
public class WaystoneInjectorMod {
    public static final String MODID = "waystoneinjector";

    public WaystoneInjectorMod() {
        // Register config FIRST
        WaystoneConfig.register();
        
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register client-only event handlers on the client
        if (FMLEnvironment.dist.isClient()) {
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.example.waystoneinjector.client.ClientEvents());
        }

        // Register networking
        com.example.waystoneinjector.network.Networking.register();
    }
}
