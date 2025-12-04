package com.example.waystoneinjector.mixin;

import com.example.waystoneinjector.client.ResourcePackHandler;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

/**
 * Mixin to automatically accept resource pack prompts during server redirects
 * Intercepts the confirmation screen and auto-clicks "Yes"
 */
@Mixin(ConfirmScreen.class)
public class MixinResourcePackPrompt {
    
    @Inject(method = "<init>(Ljava/util/function/BooleanSupplier;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;)V", 
            at = @At("RETURN"))
    private void onResourcePackPrompt(BooleanSupplier callback, Component title, Component message, CallbackInfo ci) {
        if (ResourcePackHandler.shouldAutoAccept()) {
            String titleStr = title.getString().toLowerCase();
            String messageStr = message.getString().toLowerCase();
            
            // Detect resource pack prompt by checking for keywords
            if (titleStr.contains("server resource") || messageStr.contains("server resource") ||
                titleStr.contains("resource pack") || messageStr.contains("resource pack")) {
                
                System.out.println("[WaystoneInjector] Auto-accepting resource pack prompt");
                
                // Auto-click "Yes" by invoking the callback with true
                try {
                    callback.getAsBoolean(); // This accepts the resource pack
                } catch (Exception e) {
                    System.err.println("[WaystoneInjector] Failed to auto-accept resource pack: " + e.getMessage());
                }
            }
        }
    }
}
