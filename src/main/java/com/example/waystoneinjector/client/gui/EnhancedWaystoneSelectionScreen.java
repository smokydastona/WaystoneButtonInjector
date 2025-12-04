package com.example.waystoneinjector.client.gui;

import com.example.waystoneinjector.client.gui.widget.ScrollableWaystoneList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Enhanced Waystone Selection Screen with Better Waystones Menu features
 * Phase 3: Scrollable waystone list with mouse wheel and drag support
 */
@SuppressWarnings("null")
public class EnhancedWaystoneSelectionScreen extends Screen {
    
    @SuppressWarnings("unused")
    private final Screen originalScreen;
    private final List<WaystoneData> waystones;
    private ScrollableWaystoneList waystoneList;
    
    public EnhancedWaystoneSelectionScreen(Screen originalScreen) {
        super(Component.literal("Enhanced Waystone Menu"));
        this.originalScreen = originalScreen;
        
        // Extract waystone data from original screen
        this.waystones = WaystoneExtractor.extractWaystones(originalScreen);
        
        System.out.println("[WaystoneInjector] Enhanced screen created with " + waystones.size() + " waystones");
    }
    
    @Override
    protected void init() {
        super.init();
        
        System.out.println("[WaystoneInjector] Enhanced Waystone Selection Screen initialized");
        
        // Create scrollable waystone list
        int listWidth = 300;
        int listHeight = this.height - 80; // Leave space for title and close button
        
        this.waystoneList = new ScrollableWaystoneList(
            this.width / 2 - listWidth / 2,
            40,
            listWidth,
            listHeight,
            this.waystones,
            this::onWaystoneSelected
        );
        
        this.addRenderableWidget(waystoneList);
        
        // Add close button at bottom
        Button closeButton = Button.builder(
            Component.literal("Close"),
            btn -> this.onClose()
        ).bounds(this.width / 2 - 50, this.height - 30, 100, 20).build();
        
        this.addRenderableWidget(closeButton);
    }
    
    private void onWaystoneSelected(WaystoneData waystone) {
        System.out.println("[WaystoneInjector] Waystone selected: " + waystone.getName());
        System.out.println("[WaystoneInjector] Location: " + waystone.getX() + ", " + waystone.getY() + ", " + waystone.getZ());
        System.out.println("[WaystoneInjector] Dimension: " + waystone.getDimensionName());
        
        // Phase 2: For now just log the selection
        // Phase 3: Will implement actual teleportation
        
        // TODO: Implement teleportation via original waystone object or packets
        // For now, close the screen
        this.onClose();
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
        
        // Show waystone count
        graphics.drawCenteredString(
            this.font,
            Component.literal("Waystones: " + waystones.size()),
            this.width / 2,
            this.height - 45,
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
