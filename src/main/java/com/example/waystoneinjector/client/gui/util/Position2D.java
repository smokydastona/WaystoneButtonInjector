package com.example.waystoneinjector.client.gui.util;

/**
 * HudAlternatives-inspired immutable 2D position record.
 * Uses Java records for zero-overhead value objects with automatic equals/hashCode.
 * Immutable by design to prevent accidental mutation and enable safe caching.
 */
public record Position2D(int x, int y) {
    
    /**
     * Create a new position offset by the given deltas
     */
    public Position2D offset(int dx, int dy) {
        return new Position2D(x + dx, y + dy);
    }
    
    /**
     * Calculate distance to another position
     */
    public double distanceTo(Position2D other) {
        int dx = other.x - this.x;
        int dy = other.y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
