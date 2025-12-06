package com.example.waystoneinjector.mixin;

import com.example.waystoneinjector.gui.EnhancedWaystoneSelectionScreen;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to intercept MenuScreens registration and replace WaystoneSelectionScreen factory
 * This is the CORRECT approach - intercept at Minecraft's registration point, not Balm's
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
     * Intercept ALL screen registrations and replace WaystoneSelectionScreen
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Inject(
        method = "register(Lnet/minecraft/world/inventory/MenuType;Lnet/minecraft/client/gui/screens/MenuScreens$ScreenConstructor;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void onRegisterScreen(
            MenuType<? extends M> menuType, 
            MenuScreens.ScreenConstructor<M, U> factory, 
            CallbackInfo ci) {
        
        // Check if this is registering WaystoneSelectionMenu
        if (menuType.toString().contains("waystones:waystone_selection")) {
            System.out.println("[WaystoneInjector] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("[WaystoneInjector] INTERCEPTED MenuScreens.register()!");
            System.out.println("[WaystoneInjector] MenuType: " + menuType);
            System.out.println("[WaystoneInjector] Factory: " + factory);
            System.out.println("[WaystoneInjector] Replacing with EnhancedWaystoneSelectionScreen");
            
            // Register our enhanced screen instead
            MenuScreens.ScreenConstructor enhancedFactory = 
                (menu, inv, title) -> new EnhancedWaystoneSelectionScreen(
                    (WaystoneSelectionMenu) menu, 
                    inv, 
                    title
                );
            
            MenuScreens.register((MenuType) menuType, enhancedFactory);
            
            System.out.println("[WaystoneInjector] ✓✓✓ Successfully registered EnhancedWaystoneSelectionScreen ✓✓✓");
            System.out.println("[WaystoneInjector] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            // Cancel the original registration
            ci.cancel();
        }
    }
}
