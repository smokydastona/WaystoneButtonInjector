package com.example.waystoneinjector.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Accessor interface for WaystoneSelectionScreen to safely access private fields
 */
@Mixin(targets = "net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreen", remap = false)
public interface IWaystoneSelectionScreenAccessor {
    
    @Accessor("waystoneList")
    Object getWaystoneList();
}
