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
 */
public class ScrollableWaystoneList extends ObjectSelectionList<ScrollableWaystoneList.WaystoneEntry> {
    
    private final EnhancedWaystoneSelectionScreen parent;
    
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
            // Update button position - Button has public x and y fields in 1.20.1
            waystoneButton.setX(left);
            waystoneButton.setY(top);
            
            // Render the actual WaystoneButton - this will use all your custom textures
            // m_87963_ is the obfuscated name for renderButton in 1.20.1
            waystoneButton.render(guiGraphics, mouseX, mouseY, partialTicks);
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0 && waystoneButton.active) {
                return waystoneButton.mouseClicked(mouseX, mouseY, button);
            }
            return false;
        }
        
        @Override
        public Component getNarration() {
            return waystoneButton.getMessage();
        }
    }
}
