package com.example.waystoneinjector.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Supplier;

/**
 * Custom button that renders with waystone-type themed background textures
 */
@SuppressWarnings("null")
public class ThemedButton extends Button {
    
    private final Supplier<String> waystoneTypeSupplier;
    
    public ThemedButton(int x, int y, int width, int height, Component message, 
                       OnPress onPress, Supplier<String> waystoneTypeSupplier, String side, 
                       int buttonIndex, int totalButtons) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.waystoneTypeSupplier = waystoneTypeSupplier;
    }
    
    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Render themed background
        renderThemedBackground(graphics);
        
        // Render button text manually
        int textColor = this.active ? 0xFFFFFF : 0xA0A0A0;
        graphics.drawCenteredString(
            net.minecraft.client.Minecraft.getInstance().font,
            this.getMessage(),
            getX() + width / 2,
            getY() + (height - 8) / 2,
            textColor
        );
    }
    
    private void renderThemedBackground(GuiGraphics graphics) {
        // Get the appropriate texture based on waystone type
        ResourceLocation texture = getBackgroundTexture();
        
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        
        // Render the background texture (now 64x32 instead of 64x96)
        graphics.blit(texture, 
            getX(), getY(),     // Screen position
            0, 0,               // Texture UV start (always 0,0 since each file is one button)
            width, height,      // Size to render
            64, 32);            // Total texture size (64 wide, 32 tall)
    }
    
    private ResourceLocation getBackgroundTexture() {
        // Pattern: waystoneinjector:textures/gui/buttons/<type>.png
        // Examples: regular.png, mossy.png, sharestone.png, etc.
        
        // Get current waystone type dynamically
        String currentType = waystoneTypeSupplier.get();
        
        String filename = String.format("textures/gui/buttons/%s.png", currentType);
        
        return new ResourceLocation("waystoneinjector", filename);
    }
}
