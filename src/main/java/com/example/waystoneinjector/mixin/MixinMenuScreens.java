package com.example.waystoneinjector.mixin;

import com.example.waystoneinjector.gui.EnhancedWaystoneSelectionScreen;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * MIXIN #2 - Intercepts MenuScreens registration
 * Wraps the factory to return our enhanced screen
 */
@Mixin(MenuScreens.class)
public class MixinMenuScreens {
    
    static {
        System.out.println("[WaystoneInjector] ╔═══════════════════════════════════════════════════════╗");
        System.out.println("[WaystoneInjector] ║  MixinMenuScreens CLASS LOADED                        ║");
        System.out.println("[WaystoneInjector] ║  Strategy: Wrap factory via @ModifyArg               ║");
        System.out.println("[WaystoneInjector] ╚═══════════════════════════════════════════════════════╝");
    }
    
    /**
     * INTERCEPT: Wrap the factory being registered
     */
    @SuppressWarnings({"unchecked", "rawtypes", "null"})
    @ModifyArg(
        method = "register(Lnet/minecraft/world/inventory/MenuType;Lnet/minecraft/client/gui/screens/MenuScreens$ScreenConstructor;)V",
        at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
        index = 1
    )
    private static MenuScreens.ScreenConstructor wrapFactory(MenuScreens.ScreenConstructor original) {
        
        // Create wrapper that checks if menu is WaystoneSelectionMenu
        return new MenuScreens.ScreenConstructor() {
            @Override
            public Screen create(AbstractContainerMenu menu, net.minecraft.world.entity.player.Inventory inv, net.minecraft.network.chat.Component title) {
                if (menu instanceof WaystoneSelectionMenu) {
                    System.out.println("[WaystoneInjector] → Creating EnhancedWaystoneSelectionScreen");
                    return new EnhancedWaystoneSelectionScreen(
                        (WaystoneSelectionMenu) menu,
                        inv,
                        title
                    );
                }
                // For all other menus, use original factory
                return original.create(menu, inv, title);
            }
        };
    }
}
