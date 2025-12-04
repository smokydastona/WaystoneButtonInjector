package com.example.waystoneinjector.client.gui;

import com.example.waystoneinjector.client.ClientEvents;
import com.example.waystoneinjector.client.gui.widget.ScrollableWaystoneList;
import com.example.waystoneinjector.client.gui.widget.WaystoneSearchField;
import com.example.waystoneinjector.config.WaystoneConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Method;
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
        
        System.out.println("[WaystoneInjector] ========== CREATING ENHANCED SCREEN ==========");
        
        // Set up order manager file path
        Path configDir = Path.of("config");
        Path orderFile = configDir.resolve("waystoneinjector-order.json");
        WaystoneOrderManager.setOrderFilePath(orderFile);
        
        // Extract waystone data from original screen
        System.out.println("[WaystoneInjector] Calling WaystoneExtractor...");
        List<WaystoneData> extractedWaystones = WaystoneExtractor.extractWaystones(originalScreen);
        System.out.println("[WaystoneInjector] WaystoneExtractor returned: " + extractedWaystones.size() + " waystones");
        
        // Apply saved order
        this.allWaystones = WaystoneOrderManager.applyOrder(extractedWaystones);
        this.filteredWaystones = new ArrayList<>(allWaystones);
        
        System.out.println("[WaystoneInjector] After applyOrder: " + allWaystones.size() + " waystones");
        System.out.println("[WaystoneInjector] Filtered waystones: " + filteredWaystones.size());
        
        // Print each waystone for debugging
        for (int i = 0; i < allWaystones.size(); i++) {
            WaystoneData ws = allWaystones.get(i);
            System.out.println("[WaystoneInjector]   " + i + ": " + ws.getName() + " at " + ws.getX() + "," + ws.getY() + "," + ws.getZ());
        }
        
        System.out.println("[WaystoneInjector] ===============================================");
    }
    
    @Override
    protected void init() {
        super.init();
        
        System.out.println("[WaystoneInjector] ========== INITIALIZING SCREEN ==========");
        System.out.println("[WaystoneInjector] allWaystones size: " + allWaystones.size());
        System.out.println("[WaystoneInjector] filteredWaystones size: " + filteredWaystones.size());
        
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
        
        System.out.println("[WaystoneInjector] Search field created");
        
        // Create scrollable waystone list below search
        int listWidth = 300;
        int listHeight = this.height - 110; // Leave space for search, title, and close button
        
        System.out.println("[WaystoneInjector] Creating ScrollableWaystoneList with " + filteredWaystones.size() + " waystones");
        this.waystoneList = new ScrollableWaystoneList(
            this.width / 2 - listWidth / 2,
            45,
            listWidth,
            listHeight,
            this.filteredWaystones,
            this::onWaystoneSelected
        );
        
        this.addRenderableWidget(waystoneList);
        System.out.println("[WaystoneInjector] ScrollableWaystoneList added to screen");
        
        // Add close button at bottom
        Button closeButton = Button.builder(
            Component.literal("Close"),
            btn -> this.onClose()
        ).bounds(this.width / 2 - 50, this.height - 30, 100, 20).build();
        
        this.addRenderableWidget(closeButton);
        
        // Add custom buttons from config
        addCustomButtons();
        
        System.out.println("[WaystoneInjector] Screen initialization complete");
        System.out.println("[WaystoneInjector] =======================================");
    }
    
    private void addCustomButtons() {
        // Get config values
        List<String> labels = WaystoneConfig.getEnabledLabels();
        List<String> commands = WaystoneConfig.getEnabledCommands();
        
        System.out.println("[WaystoneInjector] Adding custom buttons: " + labels.size());
        
        int numButtons = Math.min(labels.size(), 6); // Max 6 buttons
        if (numButtons == 0) {
            return;
        }
        
        // Calculate button dimensions
        int leftButtons = (numButtons + 1) / 2;
        int rightButtons = numButtons / 2;
        
        int buttonWidth = 60;
        int buttonHeight = 30;
        int verticalSpacing = 5;
        int sideMargin = 10;
        
        int centerY = this.height / 2;
        
        // Add left side buttons
        for (int i = 0; i < leftButtons; i++) {
            final int buttonIndex = i;
            String label = labels.get(buttonIndex);
            final String command = commands.get(buttonIndex);
            
            int y = centerY - ((leftButtons * buttonHeight + (leftButtons - 1) * verticalSpacing) / 2) + (i * (buttonHeight + verticalSpacing));
            
            Button button = Button.builder(
                Component.literal(label),
                btn -> handleServerTransfer(command)
            ).bounds(sideMargin, y, buttonWidth, buttonHeight).build();
            
            this.addRenderableWidget(button);
        }
        
        // Add right side buttons
        for (int i = 0; i < rightButtons; i++) {
            final int buttonIndex = leftButtons + i;
            String label = labels.get(buttonIndex);
            final String command = commands.get(buttonIndex);
            
            int y = centerY - ((rightButtons * buttonHeight + (rightButtons - 1) * verticalSpacing) / 2) + (i * (buttonHeight + verticalSpacing));
            int x = this.width - buttonWidth - sideMargin;
            
            Button button = Button.builder(
                Component.literal(label),
                btn -> handleServerTransfer(command)
            ).bounds(x, y, buttonWidth, buttonHeight).build();
            
            this.addRenderableWidget(button);
        }
    }
    
    private void handleServerTransfer(String command) {
        System.out.println("[WaystoneInjector] Handling server transfer: " + command);
        String serverAddress = parseRedirectAddress(command);
        if (serverAddress != null && !serverAddress.isEmpty()) {
            ClientEvents.connectToServer(serverAddress);
        } else {
            System.err.println("[WaystoneInjector] Could not parse server address from: " + command);
        }
    }
    
    private String parseRedirectAddress(String command) {
        String[] parts = command.trim().split("\\s+");
        if (parts.length >= 2 && parts[0].equalsIgnoreCase("redirect")) {
            if (parts[1].equals("@s") && parts.length >= 3) {
                return parts[2];
            } else {
                return parts[1];
            }
        }
        return null;
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
        
        // Trigger teleport via original waystone object using reflection
        Object waystoneObject = waystone.getWaystoneObject();
        if (waystoneObject != null) {
            try {
                // Try to find and invoke the teleport method on the original waystone
                // The original screen likely has a method to handle waystone selection
                Method[] methods = originalScreen.getClass().getDeclaredMethods();
                for (Method method : methods) {
                    // Look for methods that take the waystone object as parameter
                    if (method.getParameterCount() == 1 && 
                        method.getParameterTypes()[0].isAssignableFrom(waystoneObject.getClass())) {
                        method.setAccessible(true);
                        System.out.println("[WaystoneInjector] Invoking teleport method: " + method.getName());
                        method.invoke(originalScreen, waystoneObject);
                        this.onClose();
                        return;
                    }
                }
                
                System.err.println("[WaystoneInjector] Could not find teleport method on original screen");
                this.onClose();
            } catch (Exception e) {
                System.err.println("[WaystoneInjector] Error teleporting to waystone: " + e.getMessage());
                e.printStackTrace();
                this.onClose();
            }
        } else {
            System.err.println("[WaystoneInjector] Waystone object is null - cannot teleport");
            this.onClose();
        }
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
