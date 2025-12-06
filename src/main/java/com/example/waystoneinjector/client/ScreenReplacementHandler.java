package com.example.waystoneinjector.client;

import com.example.waystoneinjector.gui.EnhancedWaystoneSelectionScreen;
import net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Screen replacement handler using Forge's ScreenEvent.Opening (BetterWaystonesMenu approach)
 * Intercepts screen opening to replace WaystoneSelectionScreen with EnhancedWaystoneSelectionScreen
 */
public class ScreenReplacementHandler {
    
    static {
        System.out.println("[WaystoneInjector] ╔═══════════════════════════════════════════════════════╗");
        System.out.println("[WaystoneInjector] ║  ScreenReplacementHandler CLASS LOADED                ║");
        System.out.println("[WaystoneInjector] ║  Using Forge's ScreenEvent.Opening                    ║");
        System.out.println("[WaystoneInjector] ╚═══════════════════════════════════════════════════════╝");
    }
    
    public static void register() {
        System.out.println("[WaystoneInjector] Registering ScreenEvent.Opening handler...");
        
        try {
            MinecraftForge.EVENT_BUS.register(new ScreenReplacementHandler());
            System.out.println("[WaystoneInjector] ✓ ScreenEvent.Opening handler registered successfully!");
        } catch (Exception e) {
            System.out.println("[WaystoneInjector] ✗ Failed to register ScreenEvent.Opening handler!");
            e.printStackTrace();
        }
    }
    
    @SubscribeEvent
    @SuppressWarnings("null")
    public void onScreenOpen(ScreenEvent.Opening event) {
        if (event.getScreen() == null) return;
        
        System.out.println("[WaystoneInjector] ScreenEvent.Opening fired!");
        System.out.println("[WaystoneInjector] Screen type: " + event.getScreen().getClass().getName());
        
        // Check if the screen being opened is a WaystoneSelectionScreen
        if (event.getScreen() instanceof WaystoneSelectionScreen) {
            WaystoneSelectionScreen originalScreen = (WaystoneSelectionScreen) event.getScreen();
            
            System.out.println("[WaystoneInjector] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("[WaystoneInjector] INTERCEPTED WaystoneSelectionScreen!");
            System.out.println("[WaystoneInjector] Replacing with EnhancedWaystoneSelectionScreen");
            
            Minecraft client = Minecraft.getInstance();
            if (client.player == null) {
                System.out.println("[WaystoneInjector] ⚠ Player is null, cannot create enhanced screen");
                return;
            }
            
            try {
                // Create the enhanced screen
                EnhancedWaystoneSelectionScreen enhancedScreen = new EnhancedWaystoneSelectionScreen(
                    originalScreen.getMenu(),
                    client.player.getInventory(),
                    originalScreen.getTitle()
                );
                
                // Replace the screen using setNewScreen
                event.setNewScreen(enhancedScreen);
                
                System.out.println("[WaystoneInjector] ✓✓✓ Successfully replaced screen! ✓✓✓");
                System.out.println("[WaystoneInjector] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            } catch (Exception ex) {
                System.out.println("[WaystoneInjector] ✗ Failed to replace screen!");
                ex.printStackTrace();
            }
        }
    }
}
