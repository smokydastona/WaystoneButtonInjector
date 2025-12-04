package com.example.waystoneinjector.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Enhanced Waystone Selection Screen with Better Waystones Menu features
 * Phase 1: Basic screen replacement - just displays a title for now
 */
@SuppressWarnings("null")
public class EnhancedWaystoneSelectionScreen extends Screen {
    
    @SuppressWarnings("unused")
    private final Screen originalScreen;
    
    public EnhancedWaystoneSelectionScreen(Screen originalScreen) {
        super(Component.literal("Enhanced Waystone Menu"));
        this.originalScreen = originalScreen;
    }
    
    @Override
    protected void init() {
        super.init();
        
        System.out.println("[WaystoneInjector] Enhanced Waystone Selection Screen initialized");
        
        // Add a close button for now
        @SuppressWarnings("null")
        Button closeButton = Button.builder(
            Component.literal("Close"),
            btn -> this.onClose()
        ).bounds(this.width / 2 - 50, this.height - 30, 100, 20).build();
        
        this.addRenderableWidget(closeButton);
    }
    
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Render background
        this.renderBackground(graphics);
        
        // Render title
        graphics.drawCenteredString(
            this.font,
            this.title,
            this.width / 2,
            20,
            0xFFFFFF
        );
        
        // Render info text
        graphics.drawCenteredString(
            this.font,
            Component.literal("Phase 1: Basic Screen Replacement"),
            this.width / 2,
            this.height / 2 - 10,
            0xAAAAAA
        );
        
        graphics.drawCenteredString(
            this.font,
            Component.literal("Enhanced features coming soon!"),
            this.width / 2,
            this.height / 2 + 10,
            0xAAAAAA
        );
        
        // Render widgets (buttons)
        super.render(graphics, mouseX, mouseY, partialTick);
    }
    
    @Override
    public void onClose() {
        System.out.println("[WaystoneInjector] Enhanced Waystone Selection Screen closing");
        // Return to previous screen or close
        if (this.minecraft != null) {
            this.minecraft.setScreen(null);
        }
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
