package com.example.waystoneinjector.mixin;

import com.example.waystoneinjector.gui.GuiWaystoneSelectionScreen;
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
 * Mixin to register our custom scrollable waystone selection screen
 */
@Mixin(targets = "net.blay09.mods.waystones.client.ModScreens", remap = false)
public class MixinModScreens {
    
    /**
     * Inject after Waystones registers its screen to override it with ours
     */
    @Inject(method = "initialize", at = @At("RETURN"), remap = false)
    private static void replaceWaystoneScreen(CallbackInfo ci) {
        try {
            // Get the WaystoneSelectionMenu type
            var modMenusClass = Class.forName("net.blay09.mods.waystones.menu.ModMenus");
            var waystoneSelectionField = modMenusClass.getDeclaredField("waystoneSelection");
            waystoneSelectionField.setAccessible(true);
            @SuppressWarnings("unchecked")
            MenuType<WaystoneSelectionMenu> menuType = (MenuType<WaystoneSelectionMenu>) waystoneSelectionField.get(null);
            
            // Register our custom screen
            MenuScreens.register(menuType, GuiWaystoneSelectionScreen::new);
            
            System.out.println("[WaystoneInjector] Successfully replaced waystone selection screen with scrollable version");
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Failed to replace waystone selection screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
