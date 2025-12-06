/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.MinecraftServer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.blay09.mods.balm.mixin;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.server.ServerReloadFinishedEvent;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={MinecraftServer.class})
public class MinecraftServerMixin {
    @Inject(method={"reloadResources(Ljava/util/Collection;)Ljava/util/concurrent/CompletableFuture;"}, at={@At(value="RETURN")}, cancellable=true)
    private void reloadResources(Collection<String> p_129862_, CallbackInfoReturnable<CompletableFuture<Void>> callbackInfo) {
        ((CompletableFuture)callbackInfo.getReturnValue()).thenAccept(it -> Balm.getEvents().fireEvent(new ServerReloadFinishedEvent((MinecraftServer)this)));
    }
}

