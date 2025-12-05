package com.example.waystoneinjector.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Supplier;

/**
 * Custom button that renders with waystone-type themed background textures and optional server icon
 */
@SuppressWarnings("null")
public class ThemedButton extends Button {
    
    private final Supplier<String> waystoneTypeSupplier;
    private final String serverAddress;
    private ResourceLocation serverIcon;
    
    public ThemedButton(int x, int y, int width, int height, Component message, 
                       OnPress onPress, Supplier<String> waystoneTypeSupplier, String side, 
                       int buttonIndex, int totalButtons, String serverAddress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.waystoneTypeSupplier = waystoneTypeSupplier;
        this.serverAddress = serverAddress;
        this.serverIcon = null;
        
        // Try to load server icon
        if (serverAddress != null && !serverAddress.isEmpty()) {
            loadServerIcon();
        }
    }
    
    private void loadServerIcon() {
        try {
            Minecraft mc = Minecraft.getInstance();
            ServerList serverList = new ServerList(mc);
            serverList.load();
            
            // Find matching server in the list
            for (int i = 0; i < serverList.size(); i++) {
                ServerData server = serverList.get(i);
                if (server.ip.equalsIgnoreCase(serverAddress)) {
                    // Server icon is stored with a ResourceLocation based on server IP
                    String iconId = "minecraft:servers/" + server.ip.replaceAll("[^a-z0-9/._-]", "_");
                    serverIcon = new ResourceLocation(iconId);
                    System.out.println("[ThemedButton] Found server icon for " + serverAddress + ": " + iconId);
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("[ThemedButton] Failed to load server icon: " + e.getMessage());
        }
    }
    
    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Render themed background
        renderThemedBackground(graphics);
        
        // Render server icon if available (above background, below text)
        if (serverIcon != null) {
            renderServerIcon(graphics);
        }
        
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
    
    private void renderServerIcon(GuiGraphics graphics) {
        try {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            
            // Render 16x16 server icon centered in button, slightly transparent
            int iconSize = 16;
            int iconX = getX() + (width - iconSize) / 2;
            int iconY = getY() + 2; // Near top of button
            
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.7F); // 70% opacity
            graphics.blit(serverIcon, iconX, iconY, 0, 0, iconSize, iconSize, iconSize, iconSize);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); // Reset
        } catch (Exception e) {
            // Icon might not be loaded yet, ignore
        }
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
