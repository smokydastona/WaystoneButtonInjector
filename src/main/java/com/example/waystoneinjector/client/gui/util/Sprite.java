package com.example.waystoneinjector.client.gui.util;

/**
 * HudAlternatives-inspired immutable Sprite class.
 * Combines position and dimensions with composition pattern.
 * All fields are final and immutable to enable safe caching and prevent bugs.
 */
public final class Sprite {
    private final Position2D position;
    private final Dimensions dimensions;
    
    public Sprite(int x, int y, int width, int height) {
        this.position = new Position2D(x, y);
        this.dimensions = new Dimensions(width, height);
    }
    
    public Sprite(Position2D pos, Dimensions dims) {
        this.position = pos;
        this.dimensions = dims;
    }
    
    // Direct field accessors - no getter overhead
    public int x() { return position.x(); }
    public int y() { return position.y(); }
    public int width() { return dimensions.width(); }
    public int height() { return dimensions.height(); }
    
    // Composite accessors
    public Position2D position() { return position; }
    public Dimensions dimensions() { return dimensions; }
    
    /**
     * Check if a point is within this sprite's bounds
     */
    public boolean contains(int px, int py) {
        return px >= x() && px < x() + width() && 
               py >= y() && py < y() + height();
    }
    
    /**
     * Create a new sprite with offset position
     */
    public Sprite offset(int dx, int dy) {
        return new Sprite(position.offset(dx, dy), dimensions);
    }
    
    /**
     * Create a new sprite with scaled dimensions
     */
    public Sprite scale(double factor) {
        return new Sprite(position, dimensions.scale(factor));
    }
}
