package com.example.waystoneinjector.client;

import com.example.waystoneinjector.client.gui.EnhancedWaystoneSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "waystoneinjector")
public class ClientEvents {

    @SuppressWarnings("null")
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        if (screen == null) return;

        // Detect Waystones selection screen specifically
        String className = screen.getClass().getName();
        System.out.println("[WaystoneInjector] Screen detected: " + className);
        
        if (!className.equals("net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreen")) {
            return;
        }

        System.out.println("[WaystoneInjector] Waystone screen detected! Replacing with enhanced screen...");
        
        // Phase 1: Replace the screen with our enhanced version
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new EnhancedWaystoneSelectionScreen(screen));
        return; // Don't add buttons to the old screen since we're replacing it
    }
}
