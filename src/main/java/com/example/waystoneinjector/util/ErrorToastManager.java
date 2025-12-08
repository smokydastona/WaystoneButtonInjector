package com.example.waystoneinjector.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;

/**
 * Displays user-friendly error messages as in-game toasts
 * Much better UX than log-only errors
 */
@SuppressWarnings("null")
public class ErrorToastManager {
    
    /**
     * Show error toast for redirect failure
     */
    public static void showRedirectError(String serverAddress, String reason) {
        Minecraft.getInstance().execute(() -> {
            DebugLogger.error("Redirect failed: " + serverAddress + " - " + reason);
            
            Minecraft.getInstance().getToasts().addToast(
                new SystemToast(
                    SystemToast.SystemToastIds.PERIODIC_NOTIFICATION,
                    Component.literal("§cRedirect Failed"),
                    Component.literal(reason)
                )
            );
        });
    }
    
    /**
     * Show error toast for config validation failure
     */
    public static void showConfigError(String buttonName, String error) {
        Minecraft.getInstance().execute(() -> {
            DebugLogger.error("Config error: " + buttonName + " - " + error);
            
            Minecraft.getInstance().getToasts().addToast(
                new SystemToast(
                    SystemToast.SystemToastIds.PERIODIC_NOTIFICATION,
                    Component.literal("§cConfig Error: " + buttonName),
                    Component.literal(error)
                )
            );
        });
    }
    
    /**
     * Show warning toast for button disabled
     */
    public static void showButtonDisabled(String buttonName, String reason) {
        Minecraft.getInstance().execute(() -> {
            DebugLogger.warn("Button disabled: " + buttonName + " - " + reason);
            
            Minecraft.getInstance().getToasts().addToast(
                new SystemToast(
                    SystemToast.SystemToastIds.PERIODIC_NOTIFICATION,
                    Component.literal("§eButton Disabled: " + buttonName),
                    Component.literal(reason)
                )
            );
        });
    }
    
    /**
     * Show error toast for invalid server address
     */
    public static void showInvalidServerError(String serverAddress) {
        Minecraft.getInstance().execute(() -> {
            DebugLogger.error("Invalid server address: " + serverAddress);
            
            Minecraft.getInstance().getToasts().addToast(
                new SystemToast(
                    SystemToast.SystemToastIds.PERIODIC_NOTIFICATION,
                    Component.literal("§cInvalid Server Address"),
                    Component.literal("Check config: " + serverAddress)
                )
            );
        });
    }
    
    /**
     * Show timeout toast for redirect
     */
    public static void showRedirectTimeout(String serverAddress, int timeoutSeconds) {
        Minecraft.getInstance().execute(() -> {
            DebugLogger.error("Redirect timeout: " + serverAddress + " after " + timeoutSeconds + "s");
            
            Minecraft.getInstance().getToasts().addToast(
                new SystemToast(
                    SystemToast.SystemToastIds.PERIODIC_NOTIFICATION,
                    Component.literal("§cConnection Timeout"),
                    Component.literal("Server: " + serverAddress)
                )
            );
        });
    }
    
    /**
     * Show success toast for redirect
     */
    public static void showRedirectSuccess(String serverAddress) {
        Minecraft.getInstance().execute(() -> {
            DebugLogger.success("Redirect successful: " + serverAddress);
            
            Minecraft.getInstance().getToasts().addToast(
                new SystemToast(
                    SystemToast.SystemToastIds.PERIODIC_NOTIFICATION,
                    Component.literal("§aConnecting..."),
                    Component.literal(serverAddress)
                )
            );
        });
    }
    
    /**
     * Show warning toast for button overlap
     */
    public static void showOverlapWarning(String button1, String button2) {
        Minecraft.getInstance().execute(() -> {
            DebugLogger.warn("Button overlap detected: " + button1 + " and " + button2);
            
            Minecraft.getInstance().getToasts().addToast(
                new SystemToast(
                    SystemToast.SystemToastIds.PERIODIC_NOTIFICATION,
                    Component.literal("§eButton Overlap"),
                    Component.literal("Auto-spacing applied")
                )
            );
        });
    }
    
    /**
     * Show info toast for safe mode activation
     */
    public static void showSafeModeInfo() {
        Minecraft.getInstance().execute(() -> {
            DebugLogger.info("Safe mode activated - using vanilla button style");
            
            Minecraft.getInstance().getToasts().addToast(
                new SystemToast(
                    SystemToast.SystemToastIds.PERIODIC_NOTIFICATION,
                    Component.literal("§eSafe Mode Active"),
                    Component.literal("Using vanilla button style")
                )
            );
        });
    }
    
    /**
     * Show generic error toast
     */
    public static void showError(String title, String message) {
        Minecraft.getInstance().execute(() -> {
            DebugLogger.error(title + ": " + message);
            
            Minecraft.getInstance().getToasts().addToast(
                new SystemToast(
                    SystemToast.SystemToastIds.PERIODIC_NOTIFICATION,
                    Component.literal("§c" + title),
                    Component.literal(message)
                )
            );
        });
    }
    
    /**
     * Show generic warning toast
     */
    public static void showWarning(String title, String message) {
        Minecraft.getInstance().execute(() -> {
            DebugLogger.warn(title + ": " + message);
            
            Minecraft.getInstance().getToasts().addToast(
                new SystemToast(
                    SystemToast.SystemToastIds.PERIODIC_NOTIFICATION,
                    Component.literal("§e" + title),
                    Component.literal(message)
                )
            );
        });
    }
    
    /**
     * Show generic info toast
     */
    public static void showInfo(String title, String message) {
        Minecraft.getInstance().execute(() -> {
            DebugLogger.info(title + ": " + message);
            
            Minecraft.getInstance().getToasts().addToast(
                new SystemToast(
                    SystemToast.SystemToastIds.PERIODIC_NOTIFICATION,
                    Component.literal("§a" + title),
                    Component.literal(message)
                )
            );
        });
    }
}
