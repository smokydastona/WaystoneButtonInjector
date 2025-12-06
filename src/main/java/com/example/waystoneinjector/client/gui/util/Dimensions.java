package com.example.waystoneinjector.client.gui.util;

/**
 * HudAlternatives-inspired immutable dimensions record.
 * Ensures width/height cannot be accidentally modified after creation.
 */
public record Dimensions(int width, int height) {
    
    /**
     * Validate dimensions are positive
     */
    public Dimensions {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
    }
    
    /**
     * Check if a point is within these dimensions (relative to 0,0)
     */
    public boolean contains(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    /**
     * Scale dimensions by a factor
     */
    public Dimensions scale(double factor) {
        return new Dimensions((int)(width * factor), (int)(height * factor));
    }
}
