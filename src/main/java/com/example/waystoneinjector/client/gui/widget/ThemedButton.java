package com.example.waystoneinjector.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom button that renders with waystone-type themed background textures
 */
@SuppressWarnings("null")
public class ThemedButton extends Button {
    
    private final String waystoneType;
    private final String side; // "left" or "right"
    private final int buttonIndex; // 0-2 for position in stack
    private final int totalButtons; // Total buttons on this side (1-3)
    
    public ThemedButton(int x, int y, int width, int height, Component message, 
                       OnPress onPress, String waystoneType, String side, 
                       int buttonIndex, int totalButtons) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.waystoneType = waystoneType;
        this.side = side;
        this.buttonIndex = buttonIndex;
        this.totalButtons = totalButtons;
    }
    
    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Render themed background
        renderThemedBackground(graphics);
        
        // Render button text using parent's rendering
        super.renderWidget(graphics, mouseX, mouseY, partialTick);
    }
    
    private void renderThemedBackground(GuiGraphics graphics) {
        // Get the appropriate texture based on waystone type, side, and button count
        ResourceLocation texture = getBackgroundTexture();
        
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        
        // Calculate texture coordinates based on button position
        int textureY = buttonIndex * 32; // Each button slot is 32 pixels tall
        
        // Render the background texture
        graphics.blit(texture, 
            getX(), getY(), // Screen position
            0, textureY,    // Texture UV start
            width, height,  // Size to render
            64, 96);        // Total texture size (64 wide, 96 tall for 3 buttons)
    }
    
    private ResourceLocation getBackgroundTexture() {
        // Pattern: waystoneinjector:textures/gui/buttons/<type>_<side>_<count>.png
        // Examples:
        //   - regular_left_1.png (1 button on left)
        //   - mossy_right_2.png (2 buttons on right)
        //   - sharestone_left_3.png (3 buttons on left)
        
        String filename = String.format("textures/gui/buttons/%s_%s_%d.png", 
            waystoneType, side, totalButtons);
        
        return new ResourceLocation("waystoneinjector", filename);
    }
}
