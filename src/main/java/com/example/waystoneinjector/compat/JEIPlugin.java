package com.example.waystoneinjector.compat;

/*
 * JEI integration disabled - JEI is not a required dependency
 * To enable: Add JEI to build.gradle dependencies and uncomment this code
 */

/*
import com.example.waystoneinjector.gui.EnhancedWaystoneSelectionScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    
    private static final ResourceLocation PLUGIN_ID = new ResourceLocation("waystoneinjector", "jei_plugin");
    
    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }
    
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(EnhancedWaystoneSelectionScreen.class, new IGuiContainerHandler<EnhancedWaystoneSelectionScreen>() {
            @Override
            public List<Rect2i> getGuiExtraAreas(EnhancedWaystoneSelectionScreen screen) {
                return List.of(new Rect2i(0, 0, screen.width, screen.height));
            }
        });
    }
}
*/
