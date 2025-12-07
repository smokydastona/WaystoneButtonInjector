package com.example.waystoneinjector.mixin;

import com.example.waystoneinjector.gui.EnhancedWaystoneSelectionScreen;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

/**
 * MIXIN #2 - Intercepts MenuScreens registration
 * Directly modifies the registry map to replace the factory
 */
@Mixin(MenuScreens.class)
public class MixinMenuScreens {
    
    @Shadow
    @Final
    @Mutable
    private static Map<MenuType<?>, MenuScreens.ScreenConstructor<?, ?>> f_96579_;
    
    static {
        System.out.println("[WaystoneInjector] ╔═══════════════════════════════════════════════════════╗");
        System.out.println("[WaystoneInjector] ║  MixinMenuScreens CLASS LOADED                        ║");
        System.out.println("[WaystoneInjector] ║  Targeting: MenuScreens registration map             ║");
        System.out.println("[WaystoneInjector] ╚═══════════════════════════════════════════════════════╝");
    }
    
    /**
     * INTERCEPT #2: Hook after registration to replace factory in map
     */
    @Inject(
        method = "register(Lnet/minecraft/world/inventory/MenuType;Lnet/minecraft/client/gui/screens/MenuScreens$ScreenConstructor;)V",
        at = @At("RETURN")
    )
    private static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> 
    void afterRegister(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, U> factory, CallbackInfo ci) {
        
        // Check if this is a WaystoneSelectionMenu type
        try {
            String menuTypeName = menuType.toString();
            if (menuTypeName.contains("waystones") && menuTypeName.contains("waystone_selection")) {
                System.out.println("[WaystoneInjector] ═══════════════════════════════════════════════════════");
                System.out.println("[WaystoneInjector] ██ MIXIN #2 - POST-REGISTRATION FACTORY SWAP ██");
                System.out.println("[WaystoneInjector] ██ MenuType: " + menuTypeName);
                System.out.println("[WaystoneInjector] ██ Replacing factory in registry map ██");
                System.out.println("[WaystoneInjector] ═══════════════════════════════════════════════════════");
                
                // Create wrapper factory that checks menu type at creation
                MenuScreens.ScreenConstructor<M, U> wrappedFactory = (menu, inv, title) -> {
                    if (menu instanceof WaystoneSelectionMenu) {
                        System.out.println("[WaystoneInjector] → Factory wrapper: Creating EnhancedWaystoneSelectionScreen");
                        @SuppressWarnings("unchecked")
                        U enhanced = (U) new EnhancedWaystoneSelectionScreen(
                            (WaystoneSelectionMenu) menu,
                            inv,
                            title
                        );
                        return enhanced;
                    }
                    return factory.create(menu, inv, title);
                };
                
                // Replace in map
                f_96579_.put(menuType, wrappedFactory);
                System.out.println("[WaystoneInjector] ✓ Factory replaced in registry");
            }
        } catch (Exception e) {
            System.out.println("[WaystoneInjector] ⚠ Error in post-registration: " + e.getMessage());
        }
    }
}
