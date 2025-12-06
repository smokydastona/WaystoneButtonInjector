package com.example.waystoneinjector.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

/**
 * Mixin to inject into Waystones' WaystoneSelectionScreen to customize button rendering
 */
@Mixin(targets = "net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreen", remap = false)
public class MixinWaystoneSelectionScreen extends Screen {
    
    protected MixinWaystoneSelectionScreen() {
        super(null);
    }
    
    @Shadow
    private Object waystoneList;
    
    /**
     * Inject after the screen renders to add our custom overlays
     */
    @Inject(method = "render", at = @At("RETURN"))
    private void onRenderPost(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        System.out.println("[WaystoneInjector-Mixin] Render post-injection triggered");
        // Our custom rendering will be handled by ClientEvents which has access to this
    }
}
