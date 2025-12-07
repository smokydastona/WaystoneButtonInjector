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
        
        // Waystone/Block variant settings
        public WaystoneVariantSettings waystoneVariant = new WaystoneVariantSettings();
        
        // Button settings
        public ButtonSettings buttons = new ButtonSettings();
        
        // Title/Text settings
        public TextSettings text = new TextSettings();
        
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
    
    public static class WaystoneVariantSettings {
        // Which waystone texture to use
        public String variant = "regular";  // regular, mossy, blackstone, deepslate, endstone
        public boolean showVariantTexture = false;  // Show waystone block in GUI
        public int variantX = 0;
        public int variantY = 0;
        public int variantWidth = 64;
        public int variantHeight = 64;
        
        // Sharestone settings
        public String sharestoneColor = "purple";  // black, blue, brown, cyan, gray, green, light_blue, light_gray, lime, magenta, orange, pink, purple, red, white, yellow
        public boolean showSharestoneTexture = false;
        public int sharestoneX = 0;
        public int sharestoneY = 0;
        public int sharestoneWidth = 32;
        public int sharestoneHeight = 32;
        
        // Other items
        public boolean showWarpScroll = false;
        public boolean showBoundScroll = false;
        public boolean showReturnScroll = false;
        public boolean showWarpStone = false;
        public boolean showPortstone = false;
        public boolean showWarpPlate = false;
    }
    
    public static class ButtonSettings {
        // Waystone entry buttons
        public int buttonHeight = 20;
        public int buttonSpacing = 2;
        public String buttonColor = "default";  // default, blue, green, red, purple
        public boolean showButtonBackground = true;
        
        // Close/Cancel buttons
        public boolean showCloseButton = true;
        public int closeButtonX = 0;
        public int closeButtonY = 0;
    }
    
    public static class TextSettings {
        // Title
        public boolean showTitle = true;
        public int titleX = 0;           // Offset from center
        public int titleY = 6;           // Y position
        public int titleColor = 0xFFFFFF;  // RGB hex
        public boolean titleShadow = true;
        
        // Waystone names in list
        public int nameColor = 0xFFFFFF;
        public boolean nameShadow = false;
        
        // Debug text
        public String customDebugText = "";  // Custom text to show in debug overlay
    }
    
    public static class RenderOrder {
        public int background = 0;
        public int waystoneVariant = 5;
        public int portal = 10;
        public int scrollList = 20;
        public int buttons = 30;
        public int text = 40;
        public int tooltips = 100;
    }
    
    /**
     * Load or create config file
     */
    public static void load() {
        if (!CONFIG_FILE.exists()) {
            System.out.println("[WaystoneInjector] Dev config not found, creating default at: " + CONFIG_FILE.getAbsolutePath());
            data = new DevConfigData();
            save();
            return;
        }
        
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            data = GSON.fromJson(reader, DevConfigData.class);
            lastModified = CONFIG_FILE.lastModified();
            System.out.println("[WaystoneInjector] Dev config loaded! Enabled=" + data.enabled + " from: " + CONFIG_FILE.getAbsolutePath());
            if (data.enabled) {
                System.out.println("[WaystoneInjector]   Scroll list: " + data.scrollList.width + "x" + data.scrollList.height + " centered=" + data.scrollList.centered);
            }
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
            System.out.println("[WaystoneInjector] Dev config file changed, reloading...");
            load();
        }
    }
    
    // Getters
    public static boolean isEnabled() { return data.enabled; }
    public static boolean showDebugOverlay() { return data.enabled && data.showDebugOverlay; }
    public static ScrollListSettings getScrollList() { return data.scrollList; }
    public static PortalSettings getPortal() { return data.portal; }
    public static BackgroundSettings getBackground() { return data.background; }
    public static TextureOverrides getTextures() { return data.textures; }
    public static WaystoneVariantSettings getWaystoneVariant() { return data.waystoneVariant; }
    public static ButtonSettings getButtons() { return data.buttons; }
    public static TextSettings getText() { return data.text; }
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
    
    /**
     * Get waystone variant texture based on config
     */
    public static ResourceLocation getWaystoneVariantTexture() {
        if (!data.enabled) return null;
        
        String variant = data.waystoneVariant.variant.toLowerCase();
        return new ResourceLocation("waystoneinjector", 
            "textures/gui/variants/waystone_" + variant + ".png");
    }
    
    /**
     * Get sharestone color texture based on config
     */
    public static ResourceLocation getSharestoneTexture() {
        if (!data.enabled) return null;
        
        String color = data.waystoneVariant.sharestoneColor.toLowerCase();
        return new ResourceLocation("waystoneinjector", 
            "textures/gui/animations/sharestone/" + color + ".png");
    }
    
    /**
     * Get item texture by type
     */
    public static ResourceLocation getItemTexture(String itemType) {
        if (!data.enabled) return null;
        
        return new ResourceLocation("waystoneinjector", 
            "textures/gui/items/" + itemType + ".png");
    }
}
