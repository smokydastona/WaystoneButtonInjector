package com.example.waystoneinjector.config;

/**
 * Configuration profile for a specific waystone variant.
 * Each variant can have its own portal style, colors, textures, etc.
 */
public class VariantProfile {
    // Variant identifier
    public String variantId = "regular";  // regular, mossy, sandy, blackstone, deepslate, endstone
    
    // Portal settings for this variant
    public PortalSettings portal = new PortalSettings();
    
    // Background settings
    public BackgroundSettings background = new BackgroundSettings();
    
    // Texture overrides specific to this variant
    public TextureOverrides textures = new TextureOverrides();
    
    // Waystone display settings
    public WaystoneDisplaySettings display = new WaystoneDisplaySettings();
    
    // Text settings
    public TextSettings text = new TextSettings();
    
    public static class PortalSettings {
        public int xOffset = 0;
        public int yOffset = 0;
        public int width = 256;
        public int height = 256;
        public boolean centered = true;
        public int animationSpeed = 100;
    }
    
    public static class BackgroundSettings {
        public boolean renderDirtBackground = false;
        public int backgroundColor = 0x80000000;
        public boolean renderMenuBackground = true;
        public int menuBackgroundAlpha = 180;
    }
    
    public static class TextureOverrides {
        public String portalAnimation = "waystoneinjector:textures/gui/animations/portal/mystic_%d.png";
        public String listBackground = "waystoneinjector:textures/gui/backgrounds/list_panel.png";
        public String menuBackground = "waystoneinjector:textures/gui/backgrounds/menu_background.png";
        public String entryBackground = "waystoneinjector:textures/gui/backgrounds/list_entry_background.png";
        public String portalFrame = "waystoneinjector:textures/gui/backgrounds/portal_frame.png";
    }
    
    public static class WaystoneDisplaySettings {
        public boolean showVariantTexture = false;
        public int variantX = 0;
        public int variantY = 0;
        public int variantWidth = 64;
        public int variantHeight = 64;
        
        // Sharestone settings
        public String sharestoneColor = "purple";
        public boolean showSharestoneTexture = false;
        public int sharestoneX = 0;
        public int sharestoneY = 0;
        public int sharestoneWidth = 32;
        public int sharestoneHeight = 32;
        
        // Items
        public boolean showWarpScroll = false;
        public boolean showBoundScroll = false;
        public boolean showReturnScroll = false;
        public boolean showWarpStone = false;
        public boolean showPortstone = false;
        public boolean showWarpPlate = false;
    }
    
    public static class TextSettings {
        public boolean showTitle = true;
        public int titleX = 0;
        public int titleY = 6;
        public int titleColor = 0xFFFFFF;
        public boolean titleShadow = true;
        public int nameColor = 0xFFFFFF;
        public boolean nameShadow = false;
        public String customDebugText = "";
    }
}
