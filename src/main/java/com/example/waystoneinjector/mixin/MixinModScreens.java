package com.example.waystoneinjector.mixin;

import com.example.waystoneinjector.gui.EnhancedWaystoneSelectionScreen;
import net.blay09.mods.balm.api.client.screen.BalmScreenFactory;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

/**
 * Mixin to replace WaystoneSelectionScreen with our enhanced version
 * Uses @Redirect to intercept the registerScreen call completely
 */
@Mixin(targets = "net.blay09.mods.waystones.client.ModScreens", remap = false)
public class MixinModScreens {
    
    /**
     * Redirect the first registerScreen call (waystoneSelection) to use our enhanced screen
     */
    @Redirect(
        method = "initialize",
        at = @At(
            value = "INVOKE",
            target = "Lnet/blay09/mods/balm/api/client/screen/BalmScreens;registerScreen(Ljava/util/function/Supplier;Lnet/blay09/mods/balm/api/client/screen/BalmScreenFactory;)V",
            ordinal = 0,
            remap = false
        ),
        remap = false
    )
    @SuppressWarnings("unchecked")
    private static void redirectWaystoneScreenRegistration(
            BalmScreens instance, 
            Supplier<?> menuTypeSupplier, 
            BalmScreenFactory<?, ?> originalFactory) {
        
        System.out.println("[WaystoneInjector] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("[WaystoneInjector] INTERCEPTED WaystoneSelectionScreen registration!");
        System.out.println("[WaystoneInjector] Replacing with EnhancedWaystoneSelectionScreen");
        System.out.println("[WaystoneInjector] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // Create our enhanced factory
        BalmScreenFactory<WaystoneSelectionMenu, EnhancedWaystoneSelectionScreen> enhancedFactory = 
            EnhancedWaystoneSelectionScreen::new;
        
        // Cast and register with our enhanced screen
        Supplier<MenuType<? extends WaystoneSelectionMenu>> typedSupplier = 
            (Supplier<MenuType<? extends WaystoneSelectionMenu>>) menuTypeSupplier;
        instance.registerScreen(typedSupplier, enhancedFactory);
    }
}
