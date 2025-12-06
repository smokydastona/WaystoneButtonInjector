package com.example.waystoneinjector.mixin;

import com.example.waystoneinjector.gui.EnhancedWaystoneSelectionScreen;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Mixin to intercept MenuScreens registration and replace WaystoneSelectionScreen factory
 * Uses @ModifyArg to replace the factory parameter at the registration point
 */
@Mixin(MenuScreens.class)
public class MixinMenuScreens {
    
    static {
        System.out.println("[WaystoneInjector] ╔═══════════════════════════════════════════════════════╗");
        System.out.println("[WaystoneInjector] ║  MixinMenuScreens CLASS LOADED                        ║");
        System.out.println("[WaystoneInjector] ║  Targeting: net.minecraft.client.gui.screens.MenuScreens ║");
        System.out.println("[WaystoneInjector] ╚═══════════════════════════════════════════════════════╝");
    }
    
    /**
     * Modify the factory argument when registering screens
     * This intercepts at the exact moment the factory is being stored
     */
    @SuppressWarnings("unchecked")
    @ModifyArg(
        method = "m_96206_",
        at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
        index = 1,
        remap = false
    )
    private static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> MenuScreens.ScreenConstructor<M, U> modifyFactory(
            MenuScreens.ScreenConstructor<M, U> originalFactory) {
        
        // We need to check the MenuType in the method context
        // Since we can't access method parameters in @ModifyArg, we'll wrap the factory
        // and check at creation time
        return (menu, inv, title) -> {
            // Check if this is a WaystoneSelectionMenu
            if (menu instanceof WaystoneSelectionMenu) {
                System.out.println("[WaystoneInjector] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                System.out.println("[WaystoneInjector] INTERCEPTED WaystoneSelectionScreen creation!");
                System.out.println("[WaystoneInjector] Creating EnhancedWaystoneSelectionScreen instead");
                System.out.println("[WaystoneInjector] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                
                return (U) new EnhancedWaystoneSelectionScreen(
                    (WaystoneSelectionMenu) menu,
                    inv,
                    title
                );
            }
            
            // For all other menus, use the original factory
            return originalFactory.create(menu, inv, title);
        };
    }
}
