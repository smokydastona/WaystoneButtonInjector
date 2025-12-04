package com.example.waystoneinjector.mixin;

import com.example.waystoneinjector.client.ResourcePackHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundResourcePackPacket;
import net.minecraft.network.protocol.game.ServerboundResourcePackPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to automatically accept resource packs during server redirects
 */
@Mixin(ClientPacketListener.class)
public class MixinResourcePackPrompt {
    
    @Inject(method = "handleResourcePack(Lnet/minecraft/network/protocol/game/ClientboundResourcePackPacket;)V", 
            at = @At("HEAD"), 
            remap = false)
    private void onResourcePackRequest(ClientboundResourcePackPacket packet, CallbackInfo ci) {
        if (ResourcePackHandler.shouldAutoAccept()) {
            System.out.println("[WaystoneInjector] Auto-accepting resource pack: " + packet.getUrl());
            
            // Auto-accept the resource pack
            Minecraft mc = Minecraft.getInstance();
            @SuppressWarnings("null")
            Runnable task = () -> {
                if (mc.getConnection() != null) {
                    // Send acceptance to server
                    mc.getConnection().send(new ServerboundResourcePackPacket(
                        ServerboundResourcePackPacket.Action.ACCEPTED
                    ));
                }
            };
            mc.execute(task);
        }
    }
}
