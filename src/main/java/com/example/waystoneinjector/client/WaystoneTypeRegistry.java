package com.example.waystoneinjector.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Persistent registry that tracks waystone names to their types
 */
public class WaystoneTypeRegistry {
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, String> waystoneTypes = new HashMap<>();
    private static File registryFile;
    
    static {
        try {
            File gameDir = Minecraft.getInstance().gameDirectory;
            File configDir = new File(gameDir, "config");
            registryFile = new File(configDir, "waystoneinjector_types.json");
            load();
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Failed to initialize type registry: " + e.getMessage());
        }
    }
    
    /**
     * Register a waystone name with its type
     */
    public static void registerWaystone(String name, String type) {
        if (name == null || type == null) return;
        
        waystoneTypes.put(name, type);
        save();
        System.out.println("[WaystoneInjector] Registered waystone '" + name + "' as type: " + type);
    }
    
    /**
     * Get the type of a waystone by name
     */
    public static String getWaystoneType(String name) {
        return waystoneTypes.getOrDefault(name, "unknown");
    }
    
    /**
     * Check if a waystone type is known
     */
    public static boolean isKnown(String name) {
        return waystoneTypes.containsKey(name);
    }
    
    /**
     * Save the registry to disk
     */
    private static void save() {
        try (FileWriter writer = new FileWriter(registryFile)) {
            GSON.toJson(waystoneTypes, writer);
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Failed to save type registry: " + e.getMessage());
        }
    }
    
    /**
     * Load the registry from disk
     */
    private static void load() {
        if (!registryFile.exists()) {
            System.out.println("[WaystoneInjector] No existing type registry found, creating new one");
            return;
        }
        
        try (FileReader reader = new FileReader(registryFile)) {
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> loaded = GSON.fromJson(reader, type);
            if (loaded != null) {
                waystoneTypes.putAll(loaded);
                System.out.println("[WaystoneInjector] Loaded " + waystoneTypes.size() + " waystone types from registry");
            }
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Failed to load type registry: " + e.getMessage());
        }
    }
    
    /**
     * Get color for a waystone type
     */
    public static int getColorForType(String type) {
        return switch (type) {
            case "sharestone" -> 0xFF9370DB;    // Purple
            case "mossy" -> 0xFF6B8E23;         // Olive green
            case "blackstone" -> 0xFF2A2729;    // Dark gray
            case "deepslate" -> 0xFF494949;     // Medium gray
            case "endstone" -> 0xFFDFDFA0;      // Pale yellow
            case "regular" -> 0xFF8B5A2B;       // Brown
            default -> 0xFFFFFFFF;              // White (unknown)
        };
    }
}
