package com.example.waystoneinjector.client.gui;

import com.example.waystoneinjector.client.gui.widget.ScrollableWaystoneList;
import com.example.waystoneinjector.client.gui.widget.WaystoneSearchField;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enhanced Waystone Selection Screen with Better Waystones Menu features
 * Phase 5: Drag-and-drop reordering
 * Phase 6: CTRL+hover detailed tooltips
 */
@SuppressWarnings("null")
public class EnhancedWaystoneSelectionScreen extends Screen {
    
    @SuppressWarnings("unused")
    private final Screen originalScreen;
    private final List<WaystoneData> allWaystones;
    private List<WaystoneData> filteredWaystones;
    
    private ScrollableWaystoneList waystoneList;
    private WaystoneSearchField searchField;
    
    public EnhancedWaystoneSelectionScreen(Screen originalScreen) {
        super(Component.literal("Enhanced Waystone Menu"));
        this.originalScreen = originalScreen;
        
        // Set up order manager file path
        Path configDir = Path.of("config");
        Path orderFile = configDir.resolve("waystoneinjector-order.json");
        WaystoneOrderManager.setOrderFilePath(orderFile);
        
        // Extract waystone data from original screen
        List<WaystoneData> extractedWaystones = WaystoneExtractor.extractWaystones(originalScreen);
        
        // Apply saved order
        this.allWaystones = WaystoneOrderManager.applyOrder(extractedWaystones);
        this.filteredWaystones = new ArrayList<>(allWaystones);
        
        System.out.println("[WaystoneInjector] Enhanced screen created with " + allWaystones.size() + " waystones");
    }
    
    @Override
    protected void init() {
        super.init();
        
        System.out.println("[WaystoneInjector] Enhanced Waystone Selection Screen initialized");
        
        // Create search field at top
        int searchWidth = 300;
        this.searchField = new WaystoneSearchField(
            this.font,
            this.width / 2 - searchWidth / 2,
            20,
            searchWidth,
            20
        );
        this.searchField.setOnSearchChanged(this::onSearchChanged);
        this.addRenderableWidget(searchField);
        
        // Create scrollable waystone list below search
        int listWidth = 300;
        int listHeight = this.height - 110; // Leave space for search, title, and close button
        
        this.waystoneList = new ScrollableWaystoneList(
            this.width / 2 - listWidth / 2,
            45,
            listWidth,
            listHeight,
            this.filteredWaystones,
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
    
    private void onSearchChanged(String query) {
        if (query.isEmpty()) {
            filteredWaystones = new ArrayList<>(allWaystones);
        } else {
            String lowerQuery = query.toLowerCase();
            filteredWaystones = allWaystones.stream()
                .filter(ws -> ws.getName().toLowerCase().contains(lowerQuery) ||
                             ws.getDimensionName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
        }
        
        waystoneList.updateWaystones(filteredWaystones);
        
        System.out.println("[WaystoneInjector] Search: '" + query + "' - " + filteredWaystones.size() + " results");
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
            8,
            0xFFFFFF
        );
        
        // Show waystone count and instructions
        graphics.drawCenteredString(
            this.font,
            Component.literal("Waystones: " + filteredWaystones.size() + " / " + allWaystones.size()),
            this.width / 2,
            this.height - 45,
            0xAAAAAA
        );
        
        // Show drag-and-drop hint
        graphics.drawCenteredString(
            this.font,
            Component.literal("§7SHIFT+Drag to reorder • CTRL+Hover for info"),
            this.width / 2,
            this.height - 55,
            0x888888
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
