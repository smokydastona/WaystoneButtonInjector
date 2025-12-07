package com.example.waystoneinjector.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Development configuration for live GUI editing.
 * Changes in config/waystoneinjector-dev.json are applied immediately.
 */
public class DevConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FMLPaths.CONFIGDIR.get().toFile(), "waystoneinjector-dev.json");
    private static DevConfigData data = new DevConfigData();
    private static long lastModified = 0;
    
    public static class DevConfigData {
        // Master toggle
        public boolean enabled = false;
        public boolean showDebugOverlay = true;
        
        // Scroll list settings
        public ScrollListSettings scrollList = new ScrollListSettings();
        
        // Portal settings
        public PortalSettings portal = new PortalSettings();
        
        // Background settings
        public BackgroundSettings background = new BackgroundSettings();
        
        // Texture overrides
        public TextureOverrides textures = new TextureOverrides();
        
        // Render order (higher = rendered on top)
        public RenderOrder renderOrder = new RenderOrder();
    }
    
    public static class ScrollListSettings {
        public int xOffset = 0;          // Offset from calculated center X
        public int yOffset = 0;          // Offset from top
        public int width = 200;          // List width
        public int height = 150;         // List height
        public int topMargin = 60;       // Distance from top of screen
        public int bottomMargin = 40;    // Distance from bottom
        public int itemHeight = 22;      // Height of each entry
        public boolean centered = true;  // Auto-center horizontally
    }
    
    public static class PortalSettings {
        public int xOffset = 0;          // Offset from center X
        public int yOffset = 0;          // Offset from center Y
        public int width = 256;          // Portal width
        public int height = 256;         // Portal height
        public boolean centered = true;  // Auto-center
        public int animationSpeed = 100; // Milliseconds per frame
    }
    
    public static class BackgroundSettings {
        public boolean renderDirtBackground = false;
        public int backgroundColor = 0x80000000;  // ARGB format
        public boolean renderMenuBackground = true;
        public int menuBackgroundAlpha = 180;     // 0-255
    }
    
    public static class TextureOverrides {
        public String portalAnimation = "waystoneinjector:textures/gui/animations/portal/mystic_%d.png";
        public String listBackground = "waystoneinjector:textures/gui/backgrounds/list_panel.png";
        public String menuBackground = "waystoneinjector:textures/gui/backgrounds/menu_background.png";
        public String entryBackground = "waystoneinjector:textures/gui/backgrounds/list_entry_background.png";
        public String portalFrame = "waystoneinjector:textures/gui/backgrounds/portal_frame.png";
    }
    
    public static class RenderOrder {
        public int background = 0;
        public int portal = 10;
        public int scrollList = 20;
        public int buttons = 30;
        public int tooltips = 100;
    }
    
    /**
     * Load or create config file
     */
    public static void load() {
        if (!CONFIG_FILE.exists()) {
            data = new DevConfigData();
            save();
            return;
        }
        
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            data = GSON.fromJson(reader, DevConfigData.class);
            lastModified = CONFIG_FILE.lastModified();
        } catch (IOException e) {
            System.err.println("Failed to load dev config: " + e.getMessage());
            data = new DevConfigData();
        }
    }
    
    /**
     * Save config to file
     */
    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(data, writer);
            lastModified = CONFIG_FILE.lastModified();
        } catch (IOException e) {
            System.err.println("Failed to save dev config: " + e.getMessage());
        }
    }
    
    /**
     * Check if config file was modified and reload if needed
     */
    public static void checkReload() {
        if (!data.enabled) return;
        
        if (CONFIG_FILE.exists() && CONFIG_FILE.lastModified() > lastModified) {
            load();
            System.out.println("Dev config reloaded!");
        }
    }
    
    // Getters
    public static boolean isEnabled() { return data.enabled; }
    public static boolean showDebugOverlay() { return data.enabled && data.showDebugOverlay; }
    public static ScrollListSettings getScrollList() { return data.scrollList; }
    public static PortalSettings getPortal() { return data.portal; }
    public static BackgroundSettings getBackground() { return data.background; }
    public static TextureOverrides getTextures() { return data.textures; }
    public static RenderOrder getRenderOrder() { return data.renderOrder; }
    
    /**
     * Get portal texture for frame (with override support)
     */
    public static ResourceLocation getPortalTexture(int frame) {
        if (data.enabled && data.textures.portalAnimation != null) {
            String path = String.format(data.textures.portalAnimation, frame + 1);
            String[] parts = path.split(":");
            if (parts.length == 2) {
                return new ResourceLocation(parts[0], parts[1]);
            }
        }
        return new ResourceLocation("waystoneinjector", "textures/gui/animations/portal/mystic_" + (frame + 1) + ".png");
    }
    
    /**
     * Get texture override or default
     */
    public static ResourceLocation getTexture(String key, ResourceLocation defaultTexture) {
        if (!data.enabled) return defaultTexture;
        
        String override = switch (key) {
            case "list_background" -> data.textures.listBackground;
            case "menu_background" -> data.textures.menuBackground;
            case "entry_background" -> data.textures.entryBackground;
            case "portal_frame" -> data.textures.portalFrame;
            default -> null;
        };
        
        if (override != null) {
            String[] parts = override.split(":");
            if (parts.length == 2) {
                return new ResourceLocation(parts[0], parts[1]);
            }
        }
        
        return defaultTexture;
    }
}
