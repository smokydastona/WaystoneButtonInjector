# WaystoneConfig.java - Complete Config Additions for v3.0.233

## FIELD DECLARATIONS (Add after line 14)

```java
    // Debug/logging settings
    public static final ForgeConfigSpec.BooleanValue DEBUG_MODE;
    public static final ForgeConfigSpec.BooleanValue VERBOSE_MODE;
    
    // Granular debug categories (v3.0.233)
    public static final ForgeConfigSpec.BooleanValue DEBUG_GUI;
    public static final ForgeConfigSpec.BooleanValue DEBUG_CONFIG;
    public static final ForgeConfigSpec.BooleanValue DEBUG_MIXIN;
    public static final ForgeConfigSpec.BooleanValue DEBUG_REDIRECT;
    public static final ForgeConfigSpec.BooleanValue DEBUG_EVENT;
    public static final ForgeConfigSpec.BooleanValue DEBUG_RESOURCE;
    
    // Safety/compatibility settings (v3.0.233)
    public static final ForgeConfigSpec.BooleanValue SAFE_MODE;
    public static final ForgeConfigSpec.BooleanValue AUTO_SPACING;
    public static final ForgeConfigSpec.IntValue REDIRECT_TIMEOUT_SECONDS;
```

## STATIC BUILDER BLOCK (Add after DEBUG_MODE and VERBOSE_MODE initialization, around line 210)

```java
        DEBUG_MODE = builder
                .comment("",
                        "Enable debug mode for detailed logging",
                        "Logs: GUI injection, button presses, redirects, config validation",
                        "Recommended: Enable when troubleshooting issues",
                        "")
                .define("debugMode", false);
        
        VERBOSE_MODE = builder
                .comment("",
                        "Enable verbose mode for ULTRA-detailed logging (very spammy!)",
                        "Only use when diagnosing complex issues",
                        "")
                .define("verboseMode", false);
        
        // === NEW: Granular Debug Categories ===
        builder.comment("",
                        "────────────────────────────────────────────────────────────────────────────────",
                        "  Granular Debug Categories - Control which types of logs appear",
                        "  (Only takes effect when debugMode = true)",
                        "────────────────────────────────────────────────────────────────────────────────");
        
        DEBUG_GUI = builder
                .comment("Log GUI detection, screen injection, and button rendering")
                .define("gui", true);
        
        DEBUG_CONFIG = builder
                .comment("Log configuration validation and config reloads")
                .define("config", true);
        
        DEBUG_MIXIN = builder
                .comment("Log mixin injections and transformations")
                .define("mixin", true);
        
        DEBUG_REDIRECT = builder
                .comment("Log server redirects, connections, and timeouts")
                .define("redirect", true);
        
        DEBUG_EVENT = builder
                .comment("Log death/sleep events and Feverdream triggers")
                .define("event", true);
        
        DEBUG_RESOURCE = builder
                .comment("Log texture loading and resource management")
                .define("resource", true);
        
        builder.pop();
        
        // ═══════════════════════════════════════════════════════════════════════════════
        // Safety and Compatibility Settings
        // ═══════════════════════════════════════════════════════════════════════════════
        builder.push("safety");
        
        builder.comment("",
                        "═══════════════════════════════════════════════════════════════════════════════",
                        "  SAFETY AND COMPATIBILITY SETTINGS",
                        "  These settings help prevent UI glitches and client freezes",
                        "═══════════════════════════════════════════════════════════════════════════════");
        
        SAFE_MODE = builder
                .comment("",
                        "Enable safe mode for maximum compatibility",
                        "  - Uses vanilla Minecraft button style (no custom textures)",
                        "  - Uses percentage-based positioning (works on any screen size)",
                        "  - Disables custom button sizing (prevents off-screen buttons)",
                        "",
                        "When to enable:",
                        "  - Large modpacks (200+ mods) with unknown GUI modifications",
                        "  - Custom UI mods that change Waystone GUI layout",
                        "  - Buttons appear off-screen or overlap with other elements",
                        "  - First-time setup to verify button placement",
                        "",
                        "Default: false (use custom textures and positioning)",
                        "")
                .define("safeMode", false);
        
        AUTO_SPACING = builder
                .comment("",
                        "Automatically adjust button positions if they overlap",
                        "  - Detects when buttons would render on top of each other",
                        "  - Adds vertical spacing to prevent overlap",
                        "  - Shows warning toast when overlap is detected",
                        "",
                        "Recommended: true (prevents accidental overlap from bad configs)",
                        "")
                .define("autoSpacing", true);
        
        REDIRECT_TIMEOUT_SECONDS = builder
                .comment("",
                        "Server redirect timeout in seconds",
                        "  - How long to wait for connection before timing out",
                        "  - Prevents client freeze when servers are unreachable",
                        "  - Shows warning toast at 5 seconds",
                        "  - Returns to server list on timeout",
                        "",
                        "Default: 30 seconds",
                        "Range: 5-120 seconds",
                        "")
                .defineInRange("redirectTimeout", 30, 5, 120);
        
        builder.pop();
```

## BUTTON TOOLTIP FIELDS (Add to each button section)

For **BUTTON 1** (add after BUTTON1_COMMAND, around line 236):

```java
        BUTTON1_COMMAND = builder
                .comment("Command when clicked:",
                        "  - Server transfer: 'redirect @s server.ip:25565'",
                        "  - Any command: 'spawn', 'warp lobby', 'waystone teleport Home'")
                .define("command", "");
        
        BUTTON1_TOOLTIP = builder
                .comment("",
                        "Hover tooltip for this button (optional)",
                        "Supports color codes: &aGreen, &cRed, &bBlue, etc.",
                        "Leave empty for no tooltip",
                        "")
                .define("tooltip", "");
```

For **BUTTON 2-6** (add similar tooltip field after each button's command):

```java
        BUTTON2_TOOLTIP = builder
                .comment("Hover tooltip (optional, supports & color codes)")
                .define("tooltip", "");

        BUTTON3_TOOLTIP = builder
                .comment("Hover tooltip (optional, supports & color codes)")
                .define("tooltip", "");

        BUTTON4_TOOLTIP = builder
                .comment("Hover tooltip (optional, supports & color codes)")
                .define("tooltip", "");

        BUTTON5_TOOLTIP = builder
                .comment("Hover tooltip (optional, supports & color codes)")
                .define("tooltip", "");

        BUTTON6_TOOLTIP = builder
                .comment("Hover tooltip (optional, supports & color codes)")
                .define("tooltip", "");
```

## CONFIG LOAD METHOD (Add after static block, around line 680)

```java
    /**
     * Called when config is loaded or reloaded.
     * Applies debug settings and validates configuration.
     */
    public static void onConfigLoad() {
        // Apply debug settings
        DebugLogger.setDebugMode(DEBUG_MODE.get());
        DebugLogger.setVerboseMode(VERBOSE_MODE.get());
        
        // Apply granular category toggles
        if (DEBUG_MODE.get()) {
            DebugLogger.setDebugGui(DEBUG_GUI.get());
            DebugLogger.setDebugConfig(DEBUG_CONFIG.get());
            DebugLogger.setDebugMixin(DEBUG_MIXIN.get());
            DebugLogger.setDebugRedirect(DEBUG_REDIRECT.get());
            DebugLogger.setDebugEvent(DEBUG_EVENT.get());
            DebugLogger.setDebugResource(DEBUG_RESOURCE.get());
            
            DebugLogger.config("Granular debug categories applied: GUI=" + DEBUG_GUI.get() + 
                              ", Config=" + DEBUG_CONFIG.get() + 
                              ", Mixin=" + DEBUG_MIXIN.get() + 
                              ", Redirect=" + DEBUG_REDIRECT.get() + 
                              ", Event=" + DEBUG_EVENT.get() + 
                              ", Resource=" + DEBUG_RESOURCE.get());
        }
        
        // Apply safety settings
        GuiCompatibilityDetector.setSafeModeOverride(SAFE_MODE.get());
        
        if (SAFE_MODE.get()) {
            ErrorToastManager.showSafeModeInfo();
            DebugLogger.info("Safe mode enabled - using vanilla buttons and percentage positioning");
        }
        
        // Validate configuration
        ConfigValidator.ValidationResult result = ConfigValidator.validateAllButtons();
        
        if (result.hasErrors()) {
            DebugLogger.error("Config validation failed with " + result.errors.size() + " errors:");
            for (String error : result.errors) {
                DebugLogger.error("  - " + error);
                // Show first error as toast for visibility
                if (result.errors.indexOf(error) == 0) {
                    ErrorToastManager.showConfigError("Configuration", error);
                }
            }
        }
        
        if (result.hasWarnings()) {
            DebugLogger.warning("Config validation completed with " + result.warnings.size() + " warnings:");
            for (String warning : result.warnings) {
                DebugLogger.warning("  - " + warning);
            }
        }
        
        if (!result.hasErrors() && !result.hasWarnings()) {
            DebugLogger.success("Configuration validated successfully - all settings are valid");
        }
    }
```

## FIELD DECLARATIONS FOR TOOLTIPS (Add with other field declarations at top)

```java
    // Tooltip fields (v3.0.233)
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON1_TOOLTIP;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON2_TOOLTIP;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON3_TOOLTIP;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON4_TOOLTIP;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON5_TOOLTIP;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON6_TOOLTIP;
```

---

## INTEGRATION CHECKLIST

After adding all config fields, wire them up:

### 1. DebugLogger Integration
Already complete - DebugLogger has category setters ready to use.

### 2. GuiCompatibilityDetector Integration
```java
// In EnhancedWaystoneSelectionScreen or button creation code:
GuiCompatibilityDetector.setSafeModeOverride(WaystoneConfig.SAFE_MODE.get());
```

### 3. RedirectManager Integration
```java
// Replace existing redirect logic:
int timeoutMs = WaystoneConfig.REDIRECT_TIMEOUT_SECONDS.get() * 1000;
RedirectManager.redirectWithTimeout(serverAddress, timeoutMs);
```

### 4. ErrorToastManager Integration
```java
// In ConfigValidator error handling:
if (!errors.isEmpty()) {
    ErrorToastManager.showConfigError("Button " + buttonNum, errors.get(0));
}
```

### 5. Tooltip Integration
```java
// In button creation code:
String tooltip = WaystoneConfig.BUTTON1_TOOLTIP.get();
if (!tooltip.isEmpty()) {
    button.setTooltip(Tooltip.create(Component.literal(tooltip)));
}
```

---

## EXAMPLE CONFIG OUTPUT

After these changes, `waystoneinjector-client.toml` will look like:

```toml
[debug]
    debugMode = false
    verboseMode = false
    
    # Granular debug categories
    gui = true
    config = true
    mixin = true
    redirect = true
    event = true
    resource = true

[safety]
    safeMode = false
    autoSpacing = true
    redirectTimeout = 30

[button1]
    enabled = true
    label = "&aHub Server"
    command = "redirect @s hub.example.com:25565"
    tooltip = "&7Click to join Hub server"
    width = 60
    height = 30
    # ... rest of button config
```

---

## COMPLETE FILE ORDER

1. **Field Declarations** (lines 12-30)
   - DEBUG_MODE, VERBOSE_MODE
   - DEBUG_GUI through DEBUG_RESOURCE
   - SAFE_MODE, AUTO_SPACING, REDIRECT_TIMEOUT_SECONDS
   - BUTTON1_TOOLTIP through BUTTON6_TOOLTIP

2. **Static Builder Block** (lines 118-~400)
   - DEBUG_MODE, VERBOSE_MODE initialization
   - Debug category initialization (new)
   - Safety settings section (new)
   - Button 1-6 sections (add tooltip to each)
   - Feverdream section

3. **onConfigLoad() Method** (after static block, ~line 680)
   - Apply debug settings
   - Apply safety settings
   - Run validation
   - Show toasts for errors

4. **register() Method** (existing, no changes)
