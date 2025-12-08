package com.example.waystoneinjector.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * Manages server redirects with timeout handling
 * Prevents client freeze on connection hangs
 */
public class RedirectManager {
    
    private static final int DEFAULT_TIMEOUT_MS = 30000; // 30 seconds
    private static final int WARNING_THRESHOLD_MS = 5000; // 5 seconds - log warning
    
    private static CompletableFuture<Boolean> currentRedirect = null;
    private static long redirectStartTime = 0;
    private static String currentTarget = "";
    
    /**
     * Attempt redirect with timeout
     */
    @SuppressWarnings("null")
    public static CompletableFuture<Boolean> redirectWithTimeout(String serverAddress, int timeoutMs) {
        if (currentRedirect != null && !currentRedirect.isDone()) {
            DebugLogger.warn("Redirect already in progress to: " + currentTarget);
            ErrorToastManager.showWarning("Redirect In Progress", "Wait for current connection");
            return CompletableFuture.completedFuture(false);
        }
        
        redirectStartTime = System.currentTimeMillis();
        currentTarget = serverAddress;
        
        DebugLogger.redirectAttempt(getCurrentServer(), serverAddress, "button press");
        
        currentRedirect = CompletableFuture.supplyAsync(() -> {
            try {
                // Parse server address
                @Nonnull ServerAddress server = ServerAddress.parseString(serverAddress);
                
                // Start connection on main thread
                Minecraft.getInstance().execute(() -> {
                    try {
                        @Nonnull ServerData serverData = new ServerData("", serverAddress, false);
                        @Nonnull Minecraft mc = Minecraft.getInstance();
                        ConnectScreen.startConnecting(
                            new JoinMultiplayerScreen(new TitleScreen()),
                            mc,
                            server,
                            serverData,
                            false
                        );
                        DebugLogger.redirect("Connection screen opened for: " + serverAddress);
                    } catch (Exception e) {
                        DebugLogger.error("Failed to open connection screen", e);
                        ErrorToastManager.showRedirectError(serverAddress, "Failed to start connection");
                    }
                });
                
                // Monitor connection with timeout
                long startTime = System.currentTimeMillis();
                boolean warningShown = false;
                
                while (System.currentTimeMillis() - startTime < timeoutMs) {
                    long elapsed = System.currentTimeMillis() - startTime;
                    
                    // Show warning if connection is taking too long
                    if (!warningShown && elapsed > WARNING_THRESHOLD_MS) {
                        DebugLogger.warn("Redirect attempt taking longer than expected: " + elapsed + "ms");
                        warningShown = true;
                    }
                    
                    // Check if connected
                    if (isConnectedToServer(serverAddress)) {
                        DebugLogger.redirectResult(true, serverAddress, "");
                        DebugLogger.info("Connection successful in " + (System.currentTimeMillis() - redirectStartTime) + "ms");
                        ErrorToastManager.showRedirectSuccess(serverAddress);
                        return true;
                    }
                    
                    // Check if connection failed (back to multiplayer screen or title screen)
                    if (Minecraft.getInstance().screen instanceof JoinMultiplayerScreen ||
                        Minecraft.getInstance().screen instanceof TitleScreen) {
                        DebugLogger.redirectResult(false, serverAddress, "Connection failed (returned to menu)");
                        ErrorToastManager.showRedirectError(serverAddress, "Connection failed");
                        return false;
                    }
                    
                    Thread.sleep(100); // Check every 100ms
                }
                
                // Timeout reached
                long totalTime = System.currentTimeMillis() - redirectStartTime;
                DebugLogger.redirectResult(false, serverAddress, "Timeout after " + totalTime + "ms");
                ErrorToastManager.showRedirectTimeout(serverAddress, timeoutMs / 1000);
                return false;
                
            } catch (Exception e) {
                DebugLogger.redirectResult(false, serverAddress, e.getMessage());
                DebugLogger.error("Redirect failed with exception", e);
                ErrorToastManager.showRedirectError(serverAddress, "Error: " + e.getMessage());
                return false;
            }
        });
        
        return currentRedirect;
    }
    
    /**
     * Attempt redirect with default timeout
     */
    public static CompletableFuture<Boolean> redirect(String serverAddress) {
        return redirectWithTimeout(serverAddress, DEFAULT_TIMEOUT_MS);
    }
    
    /**
     * Check if currently connected to a server
     */
    private static boolean isConnectedToServer(String serverAddress) {
        Minecraft mc = Minecraft.getInstance();
        ServerData currentServerData = mc.getCurrentServer();
        if (currentServerData != null) {
            String currentServer = currentServerData.ip;
            return currentServer != null && 
                   (currentServer.equalsIgnoreCase(serverAddress) ||
                    currentServer.contains(serverAddress) ||
                    serverAddress.contains(currentServer));
        }
        return false;
    }
    
    /**
     * Get current server address
     */
    private static String getCurrentServer() {
        Minecraft mc = Minecraft.getInstance();
        ServerData currentServerData = mc.getCurrentServer();
        if (currentServerData != null) {
            return currentServerData.ip;
        }
        return "singleplayer";
    }
    
    /**
     * Cancel current redirect
     */
    public static void cancelRedirect() {
        if (currentRedirect != null && !currentRedirect.isDone()) {
            currentRedirect.cancel(true);
            DebugLogger.warn("Redirect cancelled by user");
            ErrorToastManager.showInfo("Redirect Cancelled", "Connection attempt stopped");
        }
    }
    
    /**
     * Check if redirect is in progress
     */
    public static boolean isRedirectInProgress() {
        return currentRedirect != null && !currentRedirect.isDone();
    }
    
    /**
     * Get elapsed time of current redirect
     */
    public static long getRedirectElapsedTime() {
        if (isRedirectInProgress()) {
            return System.currentTimeMillis() - redirectStartTime;
        }
        return 0;
    }
}
