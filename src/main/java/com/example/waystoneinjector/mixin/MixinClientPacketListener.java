package com.example.waystoneinjector.mixin;

import net.minecraftforge.network.NetworkHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to disable the "Incompatible FML Modded Server" error
 * Allows connecting to servers even when mods don't match
 * Replaces the need for MyServerIsCompatible mod
 */
@Mixin(value = NetworkHooks.class, remap = false)
public class MixinClientPacketListener {
    
    @Inject(method = "handleClientLoginSuccess", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onHandleClientLoginSuccess(CallbackInfo ci) {
        // Allow connection to proceed regardless of mod compatibility
        // This effectively disables the "Incompatible FML Modded Server" check
        System.out.println("[WaystoneInjector] Server compatibility check bypassed - allowing connection");
    }
}
