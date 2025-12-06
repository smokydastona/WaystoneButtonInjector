package com.example.waystoneinjector.mixin;

import com.example.waystoneinjector.gui.EnhancedWaystoneSelectionScreen;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * MIXIN #2 - Intercepts MenuScreens factory wrapper
 * Replaces factory at the Map storage level
 */
@Mixin(MenuScreens.class)
public class MixinMenuScreens {
    
    static {
        System.out.println("[WaystoneInjector] ╔═══════════════════════════════════════════════════════╗");
        System.out.println("[WaystoneInjector] ║  MixinMenuScreens CLASS LOADED                        ║");
        System.out.println("[WaystoneInjector] ║  Targeting: MenuScreens factory storage              ║");
        System.out.println("[WaystoneInjector] ╚═══════════════════════════════════════════════════════╝");
    }
    
    /**
     * INTERCEPT #2: Modify the ScreenConstructor being stored
     */
    @ModifyVariable(
        method = "*",
        at = @At("HEAD"),
        argsOnly = true
    )
    private static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> 
    MenuScreens.ScreenConstructor<M, U> replaceScreenConstructor(MenuScreens.ScreenConstructor<M, U> factory) {
        
        // Wrap the factory to check at creation time
        return (menu, inv, title) -> {
            if (menu instanceof WaystoneSelectionMenu) {
                System.out.println("[WaystoneInjector] ═══════════════════════════════════════════════════════");
                System.out.println("[WaystoneInjector] ██ MIXIN #2 - FACTORY WRAPPER TRIGGERED ██");
                System.out.println("[WaystoneInjector] ██ Menu type: " + menu.getClass().getSimpleName());
                System.out.println("[WaystoneInjector] ██ Creating EnhancedWaystoneSelectionScreen ██");
                System.out.println("[WaystoneInjector] ═══════════════════════════════════════════════════════");
                
                @SuppressWarnings("unchecked")
                U enhanced = (U) new EnhancedWaystoneSelectionScreen(
                    (WaystoneSelectionMenu) menu,
                    inv,
                    title
                );
                return enhanced;
            }
            
            // For all other screens, use original factory
            return factory.create(menu, inv, title);
        };
    }
}
