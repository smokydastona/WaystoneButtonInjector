package com.example.waystoneinjector.util;

import com.example.waystoneinjector.WaystoneInjectorMod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Centralized debug logging system with verbosity levels
 * Helps diagnose GUI injection, config issues, redirect failures, etc.
 */
public class DebugLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(WaystoneInjectorMod.MODID);
    private static final String PREFIX = "[WaystoneInjector]";
    
    // Config for debug mode (will be set by WaystoneConfig)
    private static boolean debugMode = false;
    private static boolean verboseMode = false;
    
    // Granular category toggles
    private static boolean debugGui = true;
    private static boolean debugConfig = true;
    private static boolean debugMixin = true;
    private static boolean debugRedirect = true;
    private static boolean debugEvent = true;
    private static boolean debugResource = true;
    
    public static void setDebugMode(boolean enabled) {
        debugMode = enabled;
        if (enabled) {
            info("═══════════════════════════════════════════════════════");
            info("DEBUG MODE ENABLED - Verbose logging active");
            info("═══════════════════════════════════════════════════════");
        }
    }
    
    public static void setVerboseMode(boolean enabled) {
        verboseMode = enabled;
    }
    
    // Granular category setters
    public static void setDebugGui(boolean enabled) { debugGui = enabled; }
    public static void setDebugConfig(boolean enabled) { debugConfig = enabled; }
    public static void setDebugMixin(boolean enabled) { debugMixin = enabled; }
    public static void setDebugRedirect(boolean enabled) { debugRedirect = enabled; }
    public static void setDebugEvent(boolean enabled) { debugEvent = enabled; }
    public static void setDebugResource(boolean enabled) { debugResource = enabled; }
    
    public static boolean isDebugMode() {
        return debugMode;
    }
    
    public static boolean isVerboseMode() {
        return verboseMode;
    }
    
    // ═══════════════════════════════════════════════════════════════
    // CORE LOGGING METHODS
    // ═══════════════════════════════════════════════════════════════
    
    /**
     * Always logged - critical information
     */
    public static void info(String message) {
        LOGGER.info("{} {}", PREFIX, message);
    }
    
    /**
     * Always logged - warnings about potential issues
     */
    public static void warn(String message) {
        LOGGER.warn("{} ⚠ {}", PREFIX, message);
    }
    
    /**
     * Always logged - error conditions
     */
    public static void error(String message) {
        LOGGER.error("{} ✖ {}", PREFIX, message);
    }
    
    /**
     * Always logged - error with exception
     */
    public static void error(String message, Throwable throwable) {
        LOGGER.error("{} ✖ {}", PREFIX, message, throwable);
    }
    
    /**
     * Only logged if debug mode is enabled
     */
    public static void debug(String message) {
        if (debugMode) {
            LOGGER.info("{} [DEBUG] {}", PREFIX, message);
        }
    }
    
    /**
     * Only logged if verbose mode is enabled (very detailed)
     */
    public static void verbose(String message) {
        if (verboseMode) {
            LOGGER.info("{} [VERBOSE] {}", PREFIX, message);
        }
    }
    
    // ═══════════════════════════════════════════════════════════════
    // SPECIALIZED LOGGING CATEGORIES
    // ═══════════════════════════════════════════════════════════════
    
    /**
     * GUI-related debug logs
     */
    public static void gui(String message) {
        if (debugGui) {
            debug("[GUI] " + message);
        }
    }
    
    /**
     * Config-related debug logs
     */
    public static void config(String message) {
        if (debugConfig) {
            debug("[CONFIG] " + message);
        }
    }
    
    /**
     * Mixin-related debug logs
     */
    public static void mixin(String message) {
        if (debugMixin) {
            debug("[MIXIN] " + message);
        }
    }
    
    /**
     * Redirect/connection-related debug logs
     */
    public static void redirect(String message) {
        if (debugRedirect) {
            debug("[REDIRECT] " + message);
        }
    }
    
    /**
     * Event-related debug logs
     */
    public static void event(String message) {
        if (debugEvent) {
            debug("[EVENT] " + message);
        }
    }
    
    /**
     * Texture/resource-related debug logs
     */
    public static void resource(String message) {
        if (debugResource) {
            debug("[RESOURCE] " + message);
        }
    }
    
    // ═══════════════════════════════════════════════════════════════
    // FORMATTED LOGGING HELPERS
    // ═══════════════════════════════════════════════════════════════
    
    /**
     * Log a success message with checkmark
     */
    public static void success(String message) {
        info("✓ " + message);
    }
    
    /**
     * Log a failure message with X mark
     */
    public static void failure(String message) {
        error("✖ " + message);
    }
    
    /**
     * Log a section header
     */
    public static void section(String title) {
        info("═══════════════════════════════════════════════════════");
        info("  " + title);
        info("═══════════════════════════════════════════════════════");
    }
    
    /**
     * Log a subsection
     */
    public static void subsection(String title) {
        debug("─────────────────────────────────────────────────────");
        debug("  " + title);
        debug("─────────────────────────────────────────────────────");
    }
    
    /**
     * Log button press action
     */
    public static void buttonPress(String buttonLabel, String command, boolean success) {
        if (success) {
            debug("Button pressed: '" + buttonLabel + "' → Command: '" + command + "' → ✓ SUCCESS");
        } else {
            warn("Button pressed: '" + buttonLabel + "' → Command: '" + command + "' → ✖ FAILED");
        }
    }
    
    /**
     * Log GUI detection
     */
    public static void guiDetected(String guiType, String className) {
        debug("GUI Detected: Type='" + guiType + "', Class='" + className + "'");
    }
    
    /**
     * Log config validation result
     */
    public static void configValidation(String configKey, boolean valid, String reason) {
        if (valid) {
            verbose("Config '" + configKey + "' → ✓ Valid");
        } else {
            warn("Config '" + configKey + "' → ✖ Invalid: " + reason);
        }
    }
    
    /**
     * Log redirect attempt
     */
    public static void redirectAttempt(String sourceServer, String destServer, String trigger) {
        redirect("Redirect triggered: " + trigger + " → From '" + sourceServer + "' to '" + destServer + "'");
    }
    
    /**
     * Log redirect result
     */
    public static void redirectResult(boolean success, String serverAddress, String errorReason) {
        if (success) {
            success("Redirect successful → Connected to '" + serverAddress + "'");
        } else {
            failure("Redirect failed → Server: '" + serverAddress + "', Reason: " + errorReason);
        }
    }
    
    /**
     * Log texture loading
     */
    public static void textureLoaded(String texturePath, boolean success) {
        if (success) {
            resource("Texture loaded: '" + texturePath + "'");
        } else {
            warn("Texture failed to load: '" + texturePath + "' → Using fallback");
        }
    }
    
    /**
     * Log mixin injection
     */
    public static void mixinInjection(String mixinName, String targetClass, boolean success) {
        if (success) {
            mixin("Mixin injected: '" + mixinName + "' → Target: '" + targetClass + "'");
        } else {
            error("Mixin injection failed: '" + mixinName + "' → Target: '" + targetClass + "'");
        }
    }
}
