package com.example.waystoneinjector.mixin;

import com.example.waystoneinjector.gui.CustomWaystoneButton;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.client.gui.widget.WaystoneButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin to replace WaystoneButton creation with CustomWaystoneButton.
 * This allows overlay textures to render behind button content.
 */
@Mixin(targets = "net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreenBase", remap = false)
public abstract class MixinWaystoneSelectionScreen {

    /**
     * Redirects WaystoneButton instantiation to use CustomWaystoneButton instead.
     * This is called in createWaystoneButton() method.
     */
    @Redirect(
        method = "createWaystoneButton",
        at = @At(
            value = "NEW",
            target = "(IILnet/blay09/mods/waystones/api/IWaystone;ILnet/minecraft/client/gui/components/Button$OnPress;)Lnet/blay09/mods/waystones/client/gui/widget/WaystoneButton;"
        ),
        remap = false
    )
    private WaystoneButton redirectWaystoneButtonCreation(
        int x, 
        int y, 
        IWaystone waystone, 
        int xpLevelCost, 
        net.minecraft.client.gui.components.Button.OnPress pressable
    ) {
        System.out.println("[WaystoneInjector] Injecting custom button for waystone: " + waystone.getName());
        return new CustomWaystoneButton(x, y, waystone, xpLevelCost, pressable);
    }
}
