package com.example.waystoneinjector.mixin;

import com.example.waystoneinjector.gui.EnhancedWaystoneSelectionScreen;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.waystones.menu.ModMenus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to register our enhanced waystone selection screen via Balm API
 */
@Mixin(targets = "net.blay09.mods.waystones.client.ModScreens", remap = false)
public class MixinModScreens {
    
    /**
     * Inject BEFORE Waystones registers its screens so we can override
     */
    @Inject(method = "initialize", at = @At("HEAD"), remap = false)
    private static void registerEnhancedScreen(BalmScreens screens, CallbackInfo ci) {
        try {
            System.out.println("[WaystoneInjector] ✓✓✓ MixinModScreens.initialize() CALLED ✓✓✓");
            
            // Register our enhanced screen FIRST - Balm will use the first registration
            screens.registerScreen(() -> ModMenus.waystoneSelection.get(), EnhancedWaystoneSelectionScreen::new);
            
            System.out.println("[WaystoneInjector] ✓ Successfully registered EnhancedWaystoneSelectionScreen via Balm API");
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] ERROR registering enhanced screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
