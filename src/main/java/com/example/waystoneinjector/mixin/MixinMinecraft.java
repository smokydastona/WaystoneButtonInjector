package com.example.waystoneinjector.mixin;

import com.example.waystoneinjector.gui.EnhancedWaystoneSelectionScreen;
import net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * NUCLEAR OPTION - Intercept at Minecraft.setScreen() itself
 * This is the LAST line of defense - replaces screen RIGHT before display
 */
@Mixin(Minecraft.class)
public class MixinMinecraft {
    
    static {
        System.out.println("[WaystoneInjector] ╔═══════════════════════════════════════════════════════╗");
        System.out.println("[WaystoneInjector] ║  MixinMinecraft CLASS LOADED - NUCLEAR OPTION         ║");
        System.out.println("[WaystoneInjector] ║  Targeting: Minecraft.setScreen()                     ║");
        System.out.println("[WaystoneInjector] ╚═══════════════════════════════════════════════════════╗");
    }
    
    /**
     * INTERCEPT #3: At the very last moment before screen is set
     * This is called EVERY time any screen opens, so we check the type
     * NOTE: Uses "m_91152_" as SRG name for setScreen in production
     */
    @ModifyVariable(
        method = {"setScreen", "m_91152_"},
        at = @At("HEAD"),
        argsOnly = true,
        require = 0
    )
    @SuppressWarnings("null")
    private Screen replaceWaystoneScreenAtDisplay(Screen screen) {
        if (screen instanceof WaystoneSelectionScreen) {
            WaystoneSelectionScreen originalScreen = (WaystoneSelectionScreen) screen;
            
            System.out.println("[WaystoneInjector] ═══════════════════════════════════════════════════════");
            System.out.println("[WaystoneInjector] ██ MIXIN #3 - MINECRAFT.SETSCREEN INTERCEPT ██");
            System.out.println("[WaystoneInjector] ██ ORIGINAL: " + screen.getClass().getSimpleName());
            System.out.println("[WaystoneInjector] ██ FORCING REPLACEMENT WITH ENHANCED SCREEN ██");
            System.out.println("[WaystoneInjector] ═══════════════════════════════════════════════════════");
            
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                System.out.println("[WaystoneInjector] ⚠ WARNING: Player is null!");
                return screen;
            }
            
            try {
                // Create enhanced screen with same menu
                EnhancedWaystoneSelectionScreen enhanced = new EnhancedWaystoneSelectionScreen(
                    originalScreen.getMenu(),
                    mc.player.getInventory(),
                    originalScreen.getTitle()
                );
                
                System.out.println("[WaystoneInjector] ✓✓✓ SUCCESSFULLY REPLACED SCREEN ✓✓✓");
                return enhanced;
            } catch (Exception e) {
                System.out.println("[WaystoneInjector] ✗✗✗ REPLACEMENT FAILED! ✗✗✗");
                e.printStackTrace();
                return screen;
            }
        }
        
        return screen;
    }
}
