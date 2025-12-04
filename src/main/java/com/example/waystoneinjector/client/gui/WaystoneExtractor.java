package com.example.waystoneinjector.client.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to extract waystone data from the original WaystoneSelectionScreen
 * Uses reflection to access private fields since Waystones doesn't expose a public API
 * 
 * Phase 2: This will be enhanced to actually extract waystone data
 * For now, returns dummy data for testing
 */
@SuppressWarnings("null")
public class WaystoneExtractor {
    
    /**
     * Extract waystone list from the original Waystones screen
     * @param originalScreen The WaystoneSelectionScreen instance
     * @return List of waystone data
     */
    public static List<WaystoneData> extractWaystones(Screen originalScreen) {
        List<WaystoneData> waystones = new ArrayList<>();
        
        try {
            System.out.println("[WaystoneInjector] ========== EXTRACTING WAYSTONES ==========");
            System.out.println("[WaystoneInjector] Screen class: " + originalScreen.getClass().getName());
            
            // Try reflection-based extraction first
            waystones = extractViaReflection(originalScreen);
            
            // If extraction failed or returned empty, use dummy data
            if (waystones.isEmpty()) {
                System.out.println("[WaystoneInjector] WARNING: Extraction returned empty list, using dummy data");
                waystones = getDummyWaystones();
            }
            
            System.out.println("[WaystoneInjector] Final waystone count: " + waystones.size());
            System.out.println("[WaystoneInjector] ==========================================");
            
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] EXCEPTION during extraction: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to dummy data
            waystones = getDummyWaystones();
            System.out.println("[WaystoneInjector] Using " + waystones.size() + " dummy waystones after exception");
        }
        
        return waystones;
    }
    
    /**
     * Extract waystones using reflection
     * Attempts to find and access the waystone list field in WaystoneSelectionScreen
     */
    private static List<WaystoneData> extractViaReflection(Screen screen) {
        List<WaystoneData> waystones = new ArrayList<>();
        
        try {
            Class<?> screenClass = screen.getClass();
            
            // Try to find the waystone list field
            // Common field names to try: waystones, entries, buttons, etc.
            Field[] fields = screenClass.getDeclaredFields();
            
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(screen);
                
                System.out.println("[WaystoneInjector] Found field: " + field.getName() + " of type " + field.getType().getName());
                
                // Check if it's a List
                if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    if (!list.isEmpty()) {
                        Object firstItem = list.get(0);
                        System.out.println("[WaystoneInjector] Found list field '" + field.getName() + "' with " + list.size() + " items of type " + firstItem.getClass().getName());
                        
                        // Try to extract waystone data from each item
                        waystones = extractFromList(list);
                        if (!waystones.isEmpty()) {
                            return waystones;
                        }
                    }
                }
            }
            
            // If we didn't find waystones, return dummy data for now
            System.out.println("[WaystoneInjector] Could not find waystone list, using dummy data");
            return getDummyWaystones();
            
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Reflection extraction failed: " + e.getMessage());
            e.printStackTrace();
            return getDummyWaystones();
        }
    }
    
    /**
     * Try to extract waystone data from a list of unknown objects
     */
    private static List<WaystoneData> extractFromList(List<?> unknownList) {
        List<WaystoneData> waystones = new ArrayList<>();
        
        for (Object obj : unknownList) {
            try {
                // Try to extract data using reflection
                WaystoneData data = extractFromObject(obj);
                if (data != null) {
                    waystones.add(data);
                }
            } catch (Exception e) {
                System.err.println("[WaystoneInjector] Failed to extract waystone from object: " + e.getMessage());
            }
        }
        
        return waystones;
    }
    
    /**
     * Try to extract waystone data from an unknown object
     */
    private static WaystoneData extractFromObject(Object obj) throws Exception {
        Class<?> objClass = obj.getClass();
        
        // Try to find getName(), getPos(), getDimension() methods
        String name = null;
        int x = 0, y = 0, z = 0;
        ResourceKey<Level> dimension = Level.OVERWORLD;
        boolean isGlobal = false;
        
        // Try common method names
        name = tryGetString(obj, "getName", "getWaystoneName", "name");
        
        // Try to get position
        try {
            Method getPosMethod = findMethod(objClass, "getPos", "getPosition", "pos");
            if (getPosMethod != null) {
                Object pos = getPosMethod.invoke(obj);
                if (pos != null) {
                    x = tryGetInt(pos, "getX", "x");
                    y = tryGetInt(pos, "getY", "y");
                    z = tryGetInt(pos, "getZ", "z");
                }
            }
        } catch (Exception e) {
            // Try direct coordinate methods
            x = tryGetInt(obj, "getX", "x");
            y = tryGetInt(obj, "getY", "y");
            z = tryGetInt(obj, "getZ", "z");
        }
        
        // Try to get dimension
        try {
            Method getDimMethod = findMethod(objClass, "getDimension", "getLevel", "dimension");
            if (getDimMethod != null) {
                Object dim = getDimMethod.invoke(obj);
                if (dim instanceof ResourceKey) {
                    @SuppressWarnings("unchecked")
                    ResourceKey<Level> dimKey = (ResourceKey<Level>) dim;
                    dimension = dimKey;
                }
            }
        } catch (Exception e) {
            // Default to overworld
        }
        
        // Try to get global status
        try {
            Method isGlobalMethod = findMethod(objClass, "isGlobal", "global");
            if (isGlobalMethod != null) {
                Object result = isGlobalMethod.invoke(obj);
                if (result instanceof Boolean) {
                    isGlobal = (Boolean) result;
                }
            }
        } catch (Exception e) {
            // Default to false
        }
        
        if (name != null) {
            return new WaystoneData(name, x, y, z, dimension, isGlobal, obj);
        }
        
        return null;
    }
    
    /**
     * Helper: Find a method by trying multiple possible names
     */
    private static Method findMethod(Class<?> clazz, String... methodNames) {
        for (String methodName : methodNames) {
            try {
                Method method = clazz.getDeclaredMethod(methodName);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException e) {
                // Try next name
            }
        }
        return null;
    }
    
    /**
     * Helper: Try to get a String value from an object
     */
    private static String tryGetString(Object obj, String... methodNames) {
        Method method = findMethod(obj.getClass(), methodNames);
        if (method != null) {
            try {
                Object result = method.invoke(obj);
                return result != null ? result.toString() : null;
            } catch (Exception e) {
                // Ignore
            }
        }
        return null;
    }
    
    /**
     * Helper: Try to get an int value from an object
     */
    private static int tryGetInt(Object obj, String... methodNames) {
        Method method = findMethod(obj.getClass(), methodNames);
        if (method != null) {
            try {
                Object result = method.invoke(obj);
                if (result instanceof Number) {
                    return ((Number) result).intValue();
                }
            } catch (Exception e) {
                // Ignore
            }
        }
        return 0;
    }
    
    /**
     * Fallback: Generate dummy waystone data for testing
     */
    private static List<WaystoneData> getDummyWaystones() {
        List<WaystoneData> waystones = new ArrayList<>();
        
        // Create some test waystones
        waystones.add(new WaystoneData("Spawn Village", 100, 64, 200, Level.OVERWORLD, true, null));
        waystones.add(new WaystoneData("Desert Temple", 500, 70, -300, Level.OVERWORLD, false, null));
        waystones.add(new WaystoneData("Nether Hub", 50, 100, 25, Level.NETHER, true, null));
        waystones.add(new WaystoneData("End Portal", 0, 48, 0, Level.END, false, null));
        waystones.add(new WaystoneData("Mountain Base", -200, 120, 400, Level.OVERWORLD, false, null));
        
        System.out.println("[WaystoneInjector] Using " + waystones.size() + " dummy waystones for testing");
        
        return waystones;
    }
}
