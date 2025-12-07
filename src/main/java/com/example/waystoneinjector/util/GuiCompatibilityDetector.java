package com.example.waystoneinjector.util;

import net.minecraft.client.gui.screens.Screen;

/**
 * Detects GUI compatibility and provides fallback positioning
 * Prevents broken layouts in modpacks with unknown GUI variants
 */
public class GuiCompatibilityDetector {
    
    public enum CompatibilityMode {
        STANDARD,      // Known Waystones GUI - use normal positioning
        SAFE_MODE,     // Unknown layout - use percentage-based positioning
        DISABLED       // User disabled custom buttons
    }
    
    private static CompatibilityMode currentMode = CompatibilityMode.STANDARD;
    private static boolean safeModeForcedByUser = false;
    
    /**
     * Set safe mode override from config
     */
    public static void setSafeModeOverride(boolean enabled) {
        safeModeForcedByUser = enabled;
        if (enabled) {
            currentMode = CompatibilityMode.SAFE_MODE;
            DebugLogger.warn("Safe mode enabled - using vanilla button style and percentage-based positioning");
        }
    }
    
    /**
     * Detect compatibility mode for given screen
     */
    public static CompatibilityMode detectMode(Screen screen) {
        if (safeModeForcedByUser) {
            return CompatibilityMode.SAFE_MODE;
        }
        
        // Check if screen class matches known Waystones variants
        String className = screen.getClass().getName();
        
        if (className.contains("WaystoneSelectionScreen") || 
            className.contains("SharestoneSelectionScreen")) {
            DebugLogger.gui("Detected known Waystones GUI: " + className);
            return CompatibilityMode.STANDARD;
        }
        
        DebugLogger.warn("Unknown GUI type: " + className + " - using safe mode positioning");
        return CompatibilityMode.SAFE_MODE;
    }
    
    /**
     * Calculate button position using percentage-based positioning for compatibility
     */
    public static class SafePosition {
        public final int x;
        public final int y;
        
        public SafePosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    /**
     * Get safe button position based on screen size and button index
     * Uses percentage-based positioning to work on any screen size
     */
    public static SafePosition getSafeButtonPosition(Screen screen, int buttonIndex, String side, int customXOffset, int customYOffset) {
        int screenWidth = screen.width;
        int screenHeight = screen.height;
        
        CompatibilityMode mode = detectMode(screen);
        
        if (mode == CompatibilityMode.SAFE_MODE) {
            // Use percentage-based positioning
            // Position buttons in bottom-right corner by default
            int baseX;
            int baseY = (int)(screenHeight * 0.75); // 75% down the screen
            
            if (side.equals("left")) {
                baseX = (int)(screenWidth * 0.05); // 5% from left edge
            } else if (side.equals("right")) {
                baseX = (int)(screenWidth * 0.85); // 85% from left edge (15% from right)
            } else {
                // Auto - alternate sides
                baseX = buttonIndex % 2 == 0 ? 
                    (int)(screenWidth * 0.05) : 
                    (int)(screenWidth * 0.85);
            }
            
            // Vertical spacing: 10% of screen height per button
            int verticalSpacing = (int)(screenHeight * 0.10);
            int y = baseY + (buttonIndex * verticalSpacing);
            
            // Apply custom offsets (but convert them to percentages if they're too large)
            int finalX = baseX + (Math.abs(customXOffset) > 100 ? customXOffset / 10 : customXOffset);
            int finalY = y + (Math.abs(customYOffset) > 100 ? customYOffset / 10 : customYOffset);
            
            DebugLogger.debug("Safe mode positioning: button " + buttonIndex + 
                " at (" + finalX + ", " + finalY + ") on " + screenWidth + "x" + screenHeight + " screen");
            
            return new SafePosition(finalX, finalY);
        }
        
        // Standard mode - use provided offsets directly
        return null; // Caller will use their own positioning logic
    }
    
    /**
     * Check if two buttons overlap and return suggested offset
     */
    public static boolean checkButtonOverlap(int x1, int y1, int width1, int height1,
                                            int x2, int y2, int width2, int height2) {
        boolean overlaps = !(x1 + width1 < x2 || x2 + width2 < x1 ||
                            y1 + height1 < y2 || y2 + height2 < y1);
        
        if (overlaps) {
            DebugLogger.warn("Button overlap detected at (" + x1 + "," + y1 + ") and (" + x2 + "," + y2 + ")");
        }
        
        return overlaps;
    }
    
    /**
     * Get auto-spacing offset to prevent overlap
     */
    public static int getAutoSpacingOffset(int existingY, int buttonHeight) {
        int spacing = 10; // 10 pixel padding
        return existingY + buttonHeight + spacing;
    }
    
    /**
     * Constrain button to safe screen bounds
     */
    public static SafePosition constrainToScreen(int x, int y, int width, int height, Screen screen) {
        int screenWidth = screen.width;
        int screenHeight = screen.height;
        
        // Ensure button stays within screen bounds
        int safeX = Math.max(10, Math.min(x, screenWidth - width - 10));
        int safeY = Math.max(10, Math.min(y, screenHeight - height - 10));
        
        if (safeX != x || safeY != y) {
            DebugLogger.warn("Button position constrained to screen bounds: " + 
                "(" + x + "," + y + ") → (" + safeX + "," + safeY + ")");
        }
        
        return new SafePosition(safeX, safeY);
    }
    
    /**
     * Check if custom texture loading is allowed
     */
    public static boolean shouldLoadCustomTextures() {
        return !safeModeForcedByUser;
    }
    
    /**
     * Check if custom sizing is allowed
     */
    public static boolean shouldUseCustomSizing() {
        return !safeModeForcedByUser;
    }
    
    /**
     * Get recommended button dimensions for safe mode
     */
    public static class SafeDimensions {
        public final int width;
        public final int height;
        
        public SafeDimensions(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
    
    public static SafeDimensions getSafeDimensions() {
        if (safeModeForcedByUser) {
            // Use vanilla button size
            return new SafeDimensions(200, 20);
        }
        return null; // Use custom dimensions
    }
}
