package com.example.waystoneinjector.mixin;

import net.minecraftforge.network.NetworkHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to disable the "Incompatible FML Modded Server" error
 * Allows connecting to servers even when mods don't match
 * Replaces the need for MyServerIsCompatible mod
 */
@Mixin(value = NetworkHooks.class, remap = false)
public class MixinClientPacketListener {
    
    @Inject(method = "isVanillaConnection", at = @At("HEAD"), cancellable = true, remap = false)
    private static void forceVanillaConnection(CallbackInfoReturnable<Boolean> cir) {
        // Always return true to bypass mod compatibility checks
        System.out.println("[WaystoneInjector] Forcing vanilla connection - bypassing mod compatibility check");
        cir.setReturnValue(true);
    }
}
