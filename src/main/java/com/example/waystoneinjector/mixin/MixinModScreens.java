package com.example.waystoneinjector.mixin;

import com.example.waystoneinjector.gui.EnhancedWaystoneSelectionScreen;
import net.blay09.mods.balm.api.client.screen.BalmScreenFactory;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * AGGRESSIVE MIXIN - Replaces screen factory at registration
 * This intercepts at Waystones' ModScreens.initialize() method
 */
@Mixin(targets = "net.blay09.mods.waystones.client.ModScreens", remap = false)
public class MixinModScreens {
    
    static {
        System.out.println("[WaystoneInjector] ╔═══════════════════════════════════════════════════════╗");
        System.out.println("[WaystoneInjector] ║  MixinModScreens CLASS LOADED                         ║");
        System.out.println("[WaystoneInjector] ║  Mixin targeting: net.blay09.mods.waystones.client.ModScreens  ║");
        System.out.println("[WaystoneInjector] ╚═══════════════════════════════════════════════════════╝");
    }
    
    /**
     * INTERCEPT #1: Modify the factory argument being passed to registerScreen
     * This replaces the factory BEFORE it's registered
     */
    @ModifyArg(
        method = "initialize",
        at = @At(
            value = "INVOKE",
            target = "Lnet/blay09/mods/balm/api/client/screen/BalmScreens;registerScreen(Ljava/util/function/Supplier;Lnet/blay09/mods/balm/api/client/screen/BalmScreenFactory;)V",
            ordinal = 0
        ),
        index = 1,
        remap = false
    )
    private static BalmScreenFactory<?, ?> replaceWaystoneFactory(BalmScreenFactory<?, ?> originalFactory) {
        System.out.println("[WaystoneInjector] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("[WaystoneInjector] MIXIN #1 - INTERCEPTED FACTORY REGISTRATION!");
        System.out.println("[WaystoneInjector] Original: " + originalFactory.getClass().getName());
        System.out.println("[WaystoneInjector] Replacing with EnhancedWaystoneSelectionScreen factory");
        System.out.println("[WaystoneInjector] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // Return our enhanced factory instead
        return (BalmScreenFactory<WaystoneSelectionMenu, EnhancedWaystoneSelectionScreen>) 
            EnhancedWaystoneSelectionScreen::new;
    }
}
