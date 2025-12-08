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
 * NOW SUPPORTS PER-VARIANT PROFILES!
 */
public class DevConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FMLPaths.CONFIGDIR.get().toFile(), "waystoneinjector-dev.json");
    private static DevConfigData data = new DevConfigData();
    private static long lastModified = 0;
    private static String currentVariantId = "regular";  // Track which variant is currently being displayed
    
    public static class DevConfigData {
        // Master toggle
        public boolean enabled = false;
        public boolean showDebugOverlay = true;
        
        // Scroll list settings (shared across all variants)
        public ScrollListSettings scrollList = new ScrollListSettings();
        
        // Button settings (shared across all variants)
        public ButtonSettings buttons = new ButtonSettings();
        
        // Render order (shared across all variants)
        public RenderOrder renderOrder = new RenderOrder();
        
        // PER-VARIANT PROFILES
        public Map<String, VariantProfile> variantProfiles = new HashMap<>();
        
        // DEPRECATED: Legacy single-variant settings (kept for backward compatibility)
        @Deprecated
        public PortalSettings portal = new PortalSettings();
        @Deprecated
        public BackgroundSettings background = new BackgroundSettings();
        @Deprecated
        public TextureOverrides textures = new TextureOverrides();
        @Deprecated
        public WaystoneVariantSettings waystoneVariant = new WaystoneVariantSettings();
        @Deprecated
        public TextSettings text = new TextSettings();
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
    
    /**
     * Set the current waystone variant being displayed
     */
    public static void setCurrentVariant(String variantId) {
        currentVariantId = variantId;
        System.out.println("[WaystoneInjector] Switched to variant profile: " + variantId);
    }
    
    /**
     * Get the current variant profile (or create default if missing)
     */
    public static VariantProfile getCurrentProfile() {
        if (!data.enabled || data.variantProfiles.isEmpty()) {
            // Fallback to legacy settings
            return createLegacyProfile();
        }
        
        return data.variantProfiles.computeIfAbsent(currentVariantId, k -> {
            System.out.println("[WaystoneInjector] Creating new profile for variant: " + k);
            return new VariantProfile();
        });
    }
    
    /**
     * Create a profile from legacy settings for backward compatibility
     */
    private static VariantProfile createLegacyProfile() {
        VariantProfile profile = new VariantProfile();
        profile.variantId = data.waystoneVariant.variant;
        
        // Copy portal settings
        profile.portal.xOffset = data.portal.xOffset;
        profile.portal.yOffset = data.portal.yOffset;
        profile.portal.width = data.portal.width;
        profile.portal.height = data.portal.height;
        profile.portal.centered = data.portal.centered;
        profile.portal.animationSpeed = data.portal.animationSpeed;
        
        // Copy background settings
        profile.background.renderDirtBackground = data.background.renderDirtBackground;
        profile.background.backgroundColor = data.background.backgroundColor;
        profile.background.renderMenuBackground = data.background.renderMenuBackground;
        profile.background.menuBackgroundAlpha = data.background.menuBackgroundAlpha;
        
        // Copy texture overrides
        profile.textures.portalAnimation = data.textures.portalAnimation;
        profile.textures.listBackground = data.textures.listBackground;
        profile.textures.menuBackground = data.textures.menuBackground;
        profile.textures.entryBackground = data.textures.entryBackground;
        profile.textures.portalFrame = data.textures.portalFrame;
        
        // Copy display settings
        profile.display.showVariantTexture = data.waystoneVariant.showVariantTexture;
        profile.display.variantX = data.waystoneVariant.variantX;
        profile.display.variantY = data.waystoneVariant.variantY;
        profile.display.variantWidth = data.waystoneVariant.variantWidth;
        profile.display.variantHeight = data.waystoneVariant.variantHeight;
        profile.display.sharestoneColor = data.waystoneVariant.sharestoneColor;
        profile.display.showSharestoneTexture = data.waystoneVariant.showSharestoneTexture;
        profile.display.sharestoneX = data.waystoneVariant.sharestoneX;
        profile.display.sharestoneY = data.waystoneVariant.sharestoneY;
        profile.display.sharestoneWidth = data.waystoneVariant.sharestoneWidth;
        profile.display.sharestoneHeight = data.waystoneVariant.sharestoneHeight;
        profile.display.showWarpScroll = data.waystoneVariant.showWarpScroll;
        profile.display.showBoundScroll = data.waystoneVariant.showBoundScroll;
        profile.display.showReturnScroll = data.waystoneVariant.showReturnScroll;
        profile.display.showWarpStone = data.waystoneVariant.showWarpStone;
        profile.display.showPortstone = data.waystoneVariant.showPortstone;
        profile.display.showWarpPlate = data.waystoneVariant.showWarpPlate;
        
        // Copy text settings
        profile.text.showTitle = data.text.showTitle;
        profile.text.titleX = data.text.titleX;
        profile.text.titleY = data.text.titleY;
        profile.text.titleColor = data.text.titleColor;
        profile.text.titleShadow = data.text.titleShadow;
        profile.text.nameColor = data.text.nameColor;
        profile.text.nameShadow = data.text.nameShadow;
        profile.text.customDebugText = data.text.customDebugText;
        
        return profile;
    }
    
    // Legacy getters (for backward compatibility - convert profile to legacy types)
    public static PortalSettings getPortal() { 
        VariantProfile profile = getCurrentProfile();
        PortalSettings legacy = new PortalSettings();
        legacy.xOffset = profile.portal.xOffset;
        legacy.yOffset = profile.portal.yOffset;
        legacy.width = profile.portal.width;
        legacy.height = profile.portal.height;
        legacy.centered = profile.portal.centered;
        legacy.animationSpeed = profile.portal.animationSpeed;
        return legacy;
    }
    
    public static BackgroundSettings getBackground() { 
        VariantProfile profile = getCurrentProfile();
        BackgroundSettings legacy = new BackgroundSettings();
        legacy.renderDirtBackground = profile.background.renderDirtBackground;
        legacy.backgroundColor = profile.background.backgroundColor;
        legacy.renderMenuBackground = profile.background.renderMenuBackground;
        legacy.menuBackgroundAlpha = profile.background.menuBackgroundAlpha;
        return legacy;
    }
    
    public static TextureOverrides getTextures() { 
        VariantProfile profile = getCurrentProfile();
        TextureOverrides legacy = new TextureOverrides();
        legacy.portalAnimation = profile.textures.portalAnimation;
        legacy.listBackground = profile.textures.listBackground;
        legacy.menuBackground = profile.textures.menuBackground;
        legacy.entryBackground = profile.textures.entryBackground;
        legacy.portalFrame = profile.textures.portalFrame;
        return legacy;
    }
    
    public static WaystoneVariantSettings getWaystoneVariant() { 
        // Convert new display settings to legacy format
        VariantProfile profile = getCurrentProfile();
        WaystoneVariantSettings legacy = new WaystoneVariantSettings();
        legacy.variant = profile.variantId;
        legacy.showVariantTexture = profile.display.showVariantTexture;
        legacy.variantX = profile.display.variantX;
        legacy.variantY = profile.display.variantY;
        legacy.variantWidth = profile.display.variantWidth;
        legacy.variantHeight = profile.display.variantHeight;
        legacy.sharestoneColor = profile.display.sharestoneColor;
        legacy.showSharestoneTexture = profile.display.showSharestoneTexture;
        legacy.sharestoneX = profile.display.sharestoneX;
        legacy.sharestoneY = profile.display.sharestoneY;
        legacy.sharestoneWidth = profile.display.sharestoneWidth;
        legacy.sharestoneHeight = profile.display.sharestoneHeight;
        legacy.showWarpScroll = profile.display.showWarpScroll;
        legacy.showBoundScroll = profile.display.showBoundScroll;
        legacy.showReturnScroll = profile.display.showReturnScroll;
        legacy.showWarpStone = profile.display.showWarpStone;
        legacy.showPortstone = profile.display.showPortstone;
        legacy.showWarpPlate = profile.display.showWarpPlate;
        return legacy;
    }
    
    public static TextSettings getText() { 
        VariantProfile profile = getCurrentProfile();
        TextSettings legacy = new TextSettings();
        legacy.showTitle = profile.text.showTitle;
        legacy.titleX = profile.text.titleX;
        legacy.titleY = profile.text.titleY;
        legacy.titleColor = profile.text.titleColor;
        legacy.titleShadow = profile.text.titleShadow;
        legacy.nameColor = profile.text.nameColor;
        legacy.nameShadow = profile.text.nameShadow;
        legacy.customDebugText = profile.text.customDebugText;
        return legacy;
    }
    
    // Shared getters (not variant-specific)
    public static boolean isEnabled() { return data.enabled; }
    public static boolean showDebugOverlay() { return data.enabled && data.showDebugOverlay; }
    public static ScrollListSettings getScrollList() { return data.scrollList; }
    public static ButtonSettings getButtons() { return data.buttons; }
    public static RenderOrder getRenderOrder() { return data.renderOrder; }
    
    /**
     * Get portal texture for frame (with override support)
     */
    @SuppressWarnings("null")
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
    @SuppressWarnings("null")
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
     * Get waystone variant texture based on current profile
     */
    public static ResourceLocation getWaystoneVariantTexture() {
        if (!data.enabled) return null;
        
        VariantProfile profile = getCurrentProfile();
        String variant = profile.variantId.toLowerCase();
        return new ResourceLocation("waystoneinjector", 
            "textures/gui/variants/waystone_" + variant + ".png");
    }
    
    /**
     * Get sharestone color texture based on current profile
     */
    public static ResourceLocation getSharestoneTexture() {
        if (!data.enabled) return null;
        
        VariantProfile profile = getCurrentProfile();
        String color = profile.display.sharestoneColor.toLowerCase();
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
