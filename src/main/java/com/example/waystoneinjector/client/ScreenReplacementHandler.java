package com.example.waystoneinjector.client;

import com.example.waystoneinjector.gui.EnhancedWaystoneSelectionScreen;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.OpenScreenEvent;
import net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreen;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/**
 * Event-based screen replacement handler (Sodium-inspired approach)
 * Intercepts screen opening events and replaces WaystoneSelectionScreen with EnhancedWaystoneSelectionScreen
 */
public class ScreenReplacementHandler {
    
    public static void register() {
        System.out.println("[WaystoneInjector] ╔═══════════════════════════════════════════════════════╗");
        System.out.println("[WaystoneInjector] ║  Registering ScreenReplacementHandler                ║");
        System.out.println("[WaystoneInjector] ║  Using Balm OpenScreenEvent (Sodium-style approach)  ║");
        System.out.println("[WaystoneInjector] ╚═══════════════════════════════════════════════════════╝");
        
        // Register using Balm's event system
        Balm.getEvents().onEvent(OpenScreenEvent.class, ScreenReplacementHandler::onScreenOpen);
        
        System.out.println("[WaystoneInjector] ✓ ScreenReplacementHandler registered successfully");
    }
    
    private static void onScreenOpen(OpenScreenEvent event) {
        Screen screen = event.getScreen();
        
        // Check if the screen being opened is WaystoneSelectionScreen
        if (screen instanceof WaystoneSelectionScreen) {
            System.out.println("[WaystoneInjector] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("[WaystoneInjector] INTERCEPTED WaystoneSelectionScreen opening!");
            System.out.println("[WaystoneInjector] Screen type: " + screen.getClass().getName());
            
            // Cast to access menu (WaystoneSelectionScreen extends AbstractContainerScreen)
            if (screen instanceof AbstractContainerScreen) {
                AbstractContainerScreen<?> containerScreen = (AbstractContainerScreen<?>) screen;
                
                // Get the menu from the screen
                if (containerScreen.getMenu() instanceof WaystoneSelectionMenu) {
                    WaystoneSelectionMenu menu = (WaystoneSelectionMenu) containerScreen.getMenu();
                    
                    var minecraft = net.minecraft.client.Minecraft.getInstance();
                    var player = minecraft.player;
                    if (player == null) {
                        System.out.println("[WaystoneInjector] ⚠ Warning: Player is null, cannot create enhanced screen");
                        return;
                    }
                    
                    // Create our enhanced screen with the same menu
                    EnhancedWaystoneSelectionScreen enhancedScreen = new EnhancedWaystoneSelectionScreen(
                        menu,
                        player.getInventory(),
                        screen.getTitle()
                    );
                    
                    // Replace the screen!
                    event.setScreen(enhancedScreen);
                    
                    System.out.println("[WaystoneInjector] ✓✓✓ Replaced with EnhancedWaystoneSelectionScreen ✓✓✓");
                    System.out.println("[WaystoneInjector] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    return;
                }
            }
            
            System.out.println("[WaystoneInjector] ⚠ Warning: Could not extract menu from screen");
            System.out.println("[WaystoneInjector] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        }
    }
}
