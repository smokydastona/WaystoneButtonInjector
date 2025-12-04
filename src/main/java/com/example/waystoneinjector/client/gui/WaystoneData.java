package com.example.waystoneinjector.client.gui;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

/**
 * Data class representing a single waystone entry
 * This is our internal representation - extracted from Waystones mod's screen
 */
public class WaystoneData {
    private final String name;
    private final int x;
    private final int y;
    private final int z;
    private final ResourceKey<Level> dimension;
    private final boolean isGlobal;
    private final Object waystoneObject; // Store original waystone object for teleportation
    
    public WaystoneData(String name, int x, int y, int z, ResourceKey<Level> dimension, boolean isGlobal, Object waystoneObject) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.isGlobal = isGlobal;
        this.waystoneObject = waystoneObject;
    }
    
    public String getName() {
        return name;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getZ() {
        return z;
    }
    
    public ResourceKey<Level> getDimension() {
        return dimension;
    }
    
    public boolean isGlobal() {
        return isGlobal;
    }
    
    public Object getWaystoneObject() {
        return waystoneObject;
    }
    
    public String getDimensionName() {
        String dimName = dimension.location().toString();
        if (dimName.contains("overworld")) return "Overworld";
        if (dimName.contains("the_nether")) return "The Nether";
        if (dimName.contains("the_end")) return "The End";
        return dimName;
    }
    
    public double getDistanceToPlayer(double playerX, double playerZ, ResourceKey<Level> playerDimension) {
        // Only calculate distance if in same dimension
        if (!this.dimension.equals(playerDimension)) {
            return Double.MAX_VALUE; // Different dimension
        }
        
        double dx = this.x - playerX;
        double dz = this.z - playerZ;
        return Math.sqrt(dx * dx + dz * dz);
    }
}
