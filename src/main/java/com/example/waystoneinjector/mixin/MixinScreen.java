package com.example.waystoneinjector.mixin;

import com.example.waystoneinjector.gui.EnhancedWaystoneSelectionScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to prevent dirt background rendering in EnhancedWaystoneSelectionScreen
 */
@Mixin(Screen.class)
public class MixinScreen {
    
    // Target the obfuscated method name (m_96639_) for renderBackground in production
    @Inject(method = "m_96639_(Lnet/minecraft/client/gui/GuiGraphics;I)V", at = @At("HEAD"), cancellable = true, remap = false)
    private void preventDirtBackground(GuiGraphics guiGraphics, int vOffset, CallbackInfo ci) {
        // Cast this to Screen to check instance type
        Screen screen = (Screen) (Object) this;
        
        // If this is our enhanced screen, cancel the dirt rendering completely
        if (screen instanceof EnhancedWaystoneSelectionScreen) {
            System.out.println("[WaystoneInjector] Mixin: Blocked dirt background rendering for EnhancedWaystoneSelectionScreen");
            ci.cancel(); // Don't render dirt background
        }
    }
}
