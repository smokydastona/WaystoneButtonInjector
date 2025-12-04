package com.example.waystoneinjector.client.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages persistent waystone ordering per player
 * Phase 5: Save and load custom waystone order
 */
public class WaystoneOrderManager {
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Path orderFilePath = null;
    
    public static void setOrderFilePath(Path path) {
        orderFilePath = path;
    }
    
    /**
     * Load saved waystone order from file
     * @return Map of waystone name to display index
     */
    public static Map<String, Integer> loadWaystoneOrder() {
        if (orderFilePath == null || !Files.exists(orderFilePath)) {
            return new HashMap<>();
        }
        
        try {
            String json = Files.readString(orderFilePath);
            TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};
            return GSON.fromJson(json, typeToken.getType());
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Failed to load waystone order: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    /**
     * Save current waystone order to file
     * @param orderedWaystones List of waystones in current display order
     */
    public static void saveWaystoneOrder(List<WaystoneData> orderedWaystones) {
        if (orderFilePath == null) {
            return;
        }
        
        try {
            // Create map of waystone name to index
            Map<String, Integer> orderMap = new HashMap<>();
            for (int i = 0; i < orderedWaystones.size(); i++) {
                orderMap.put(orderedWaystones.get(i).getName(), i);
            }
            
            // Ensure parent directory exists
            Files.createDirectories(orderFilePath.getParent());
            
            // Write to file
            String json = GSON.toJson(orderMap);
            Files.writeString(orderFilePath, json);
            
            System.out.println("[WaystoneInjector] Saved waystone order: " + orderedWaystones.size() + " waystones");
            
        } catch (IOException e) {
            System.err.println("[WaystoneInjector] Failed to save waystone order: " + e.getMessage());
        }
    }
    
    /**
     * Apply saved order to a list of waystones
     * @param waystones Original waystone list
     * @return Reordered waystone list
     */
    public static List<WaystoneData> applyOrder(List<WaystoneData> waystones) {
        Map<String, Integer> orderMap = loadWaystoneOrder();
        
        if (orderMap.isEmpty()) {
            return waystones; // No saved order
        }
        
        // Sort waystones by saved order
        waystones.sort((a, b) -> {
            Integer indexA = orderMap.getOrDefault(a.getName(), Integer.MAX_VALUE);
            Integer indexB = orderMap.getOrDefault(b.getName(), Integer.MAX_VALUE);
            return indexA.compareTo(indexB);
        });
        
        return waystones;
    }
}
