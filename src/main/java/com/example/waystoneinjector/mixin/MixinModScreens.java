package com.example.waystoneinjector.mixin;

import com.example.waystoneinjector.gui.EnhancedWaystoneSelectionScreen;
import net.blay09.mods.balm.api.client.screen.BalmScreenFactory;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Mixin to replace WaystoneSelectionScreen with our enhanced version
 */
@Mixin(targets = "net.blay09.mods.waystones.client.ModScreens", remap = false)
public class MixinModScreens {
    
    /**
     * Intercept the first registerScreen call (waystoneSelection) and replace with our screen
     */
    @ModifyArg(
        method = "initialize",
        at = @At(
            value = "INVOKE",
            target = "Lnet/blay09/mods/balm/api/client/screen/BalmScreens;registerScreen(Ljava/util/function/Supplier;Lnet/blay09/mods/balm/api/client/screen/BalmScreenFactory;)V",
            ordinal = 0,
            remap = false
        ),
        index = 1,
        remap = false
    )
    private static BalmScreenFactory<?, ?> replaceWaystoneScreenFactory(BalmScreenFactory<?, ?> original) {
        System.out.println("[WaystoneInjector] ✓✓✓ Replacing WaystoneSelectionScreen with EnhancedWaystoneSelectionScreen ✓✓✓");
        BalmScreenFactory<WaystoneSelectionMenu, EnhancedWaystoneSelectionScreen> factory = EnhancedWaystoneSelectionScreen::new;
        return factory;
    }
}
