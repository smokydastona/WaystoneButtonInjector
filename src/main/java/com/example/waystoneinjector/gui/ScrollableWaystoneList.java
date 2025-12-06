package com.example.waystoneinjector.gui;

import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.client.gui.widget.WaystoneButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Scrollable list widget for waystone selection.
 * Uses actual WaystoneButton widgets to preserve all custom textures and rendering.
 * 
 * Optimizations:
 * - ModernUI-inspired: Smooth scrolling with pixel-perfect positioning
 * - MemoryLeakFix: Proper cleanup to prevent leaks
 */
public class ScrollableWaystoneList extends ObjectSelectionList<ScrollableWaystoneList.WaystoneEntry> {
    
    private final EnhancedWaystoneSelectionScreen parent;
    
    // ModernUI-inspired: Smooth scrolling state
    private double targetScrollAmount = 0.0;
    private double currentScrollAmount = 0.0;
    private static final double SCROLL_SMOOTHNESS = 0.3; // Lower = smoother (0.0-1.0)
    
    public ScrollableWaystoneList(EnhancedWaystoneSelectionScreen parent, Minecraft mc, int width, int height, int top, int bottom, int itemHeight, List<IWaystone> waystones, int xpCostPerWaystone) {
        super(mc, width, height, top, bottom, itemHeight);
        this.parent = parent;
        
        // Add all waystones to the list
        for (IWaystone waystone : waystones) {
            this.addEntry(new WaystoneEntry(waystone, xpCostPerWaystone));
        }
    }
    
    @Override
    protected void renderBackground(@Nonnull GuiGraphics guiGraphics) {
        // Do nothing - prevent dirt background from rendering
    }
    
    @Override
    public int getRowWidth() {
        return 200; // Standard WaystoneButton width
    }
    
    @Override
    protected int getScrollbarPosition() {
        return this.width / 2 + 108; // Match original waystone menu scrollbar position
    }
    
    /**
     * ModernUI-inspired: Smooth scrolling implementation
     * Provides pixel-perfect positioning and smooth animations
     */
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        // Store target scroll amount for smooth interpolation
        targetScrollAmount = this.getScrollAmount() - delta * this.itemHeight / 2.0;
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
    
    /**
     * ModernUI-inspired: Render with smooth scroll interpolation
     */
    public void renderWithSmoothScroll(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Interpolate scroll position for smooth scrolling (ModernUI technique)
        if (Math.abs(targetScrollAmount - currentScrollAmount) > 0.1) {
            currentScrollAmount += (targetScrollAmount - currentScrollAmount) * SCROLL_SMOOTHNESS;
            // Pixel-perfect positioning (ModernUI principle)
            this.setScrollAmount(Math.round(currentScrollAmount * 100.0) / 100.0);
        } else {
            currentScrollAmount = targetScrollAmount;
        }
    }
    
    /**
     * Clean up resources to prevent memory leaks (MemoryLeakFix-inspired)
     */
    public void cleanup() {
        super.clearEntries(); // Clear all entries and their button references
    }
    
    public class WaystoneEntry extends ObjectSelectionList.Entry<WaystoneEntry> {
        
        private final WaystoneButton waystoneButton;
        
        public WaystoneEntry(IWaystone waystone, int xpCost) {
            // Create a real WaystoneButton - this will automatically get the custom overlays from ClientEvents
            this.waystoneButton = new WaystoneButton(0, 0, waystone, xpCost, button -> {
                parent.onWaystoneSelected(waystone);
            });
        }
        
        @Override
        public void render(@Nonnull GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
            // Update button position and size
            waystoneButton.setX(left);
            waystoneButton.setY(top);
            waystoneButton.setWidth(width);
            waystoneButton.setHeight(height);
            
            // Render the actual WaystoneButton - this will use all your custom textures
            waystoneButton.render(guiGraphics, mouseX, mouseY, partialTicks);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            // Forward mouse clicks to the button with proper bounds checking
            if (waystoneButton.isMouseOver(mouseX, mouseY)) {
                waystoneButton.onClick(mouseX, mouseY);
                return true;
            }
            return false;
        }
        
        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            // Forward mouse release events to the button
            return waystoneButton.mouseReleased(mouseX, mouseY, button);
        }
        
        @Override
        public Component getNarration() {
            return waystoneButton.getMessage();
        }
    }
}
