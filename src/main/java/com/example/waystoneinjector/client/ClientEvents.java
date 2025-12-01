package com.example.waystoneinjector.client;

import com.example.waystoneinjector.config.WaystoneConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "waystoneinjector")
public class ClientEvents {

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        if (screen == null) return;

        // Detect Waystones selection screen specifically
        String className = screen.getClass().getName();
        System.out.println("[WaystoneInjector] Screen detected: " + className);
        
        if (!className.equals("net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreen")) {
            return;
        }

        System.out.println("[WaystoneInjector] Waystone screen detected! Adding buttons...");
        
        // Get config values
        List<String> labels = WaystoneConfig.getEnabledLabels();
        List<String> commands = WaystoneConfig.getEnabledCommands();
        
        System.out.println("[WaystoneInjector] Enabled buttons: " + labels.size());
        for (int i = 0; i < labels.size(); i++) {
            System.out.println("[WaystoneInjector] Button " + i + ": " + labels.get(i) + " -> " + commands.get(i));
        }
        
        int numButtons = Math.min(labels.size(), 6); // Max 6 buttons
        if (numButtons == 0) {
            System.out.println("[WaystoneInjector] No enabled buttons found!");
            return;
        }

        // Calculate button dimensions based on number of buttons
        // Split buttons to left and right sides
        int leftButtons = (numButtons + 1) / 2;  // Ceiling division
        int rightButtons = numButtons / 2;        // Floor division
        
        int buttonWidth = 60;
        int buttonHeight = 30;
        int verticalSpacing = 5;
        int sideMargin = 10; // Distance from screen edge
        
        int centerY = screen.height / 2;
        
        // Add left side buttons
        for (int i = 0; i < leftButtons; i++) {
            final int buttonIndex = i;
            String label = labels.get(buttonIndex);
            final String command = commands.get(buttonIndex);
            
            int y = centerY - ((leftButtons * buttonHeight + (leftButtons - 1) * verticalSpacing) / 2) + (i * (buttonHeight + verticalSpacing));
            
            Button button = Button.builder(
                Component.literal(label),
                btn -> {
                    // Directly handle the server transfer - no packets needed!
                    handleServerTransfer(command);
                }
            ).bounds(sideMargin, y, buttonWidth, buttonHeight).build();
            
            event.addListener(button);
        }
        
        // Add right side buttons
        for (int i = 0; i < rightButtons; i++) {
            final int buttonIndex = leftButtons + i;
            String label = labels.get(buttonIndex);
            final String command = commands.get(buttonIndex);
            
            int y = centerY - ((rightButtons * buttonHeight + (rightButtons - 1) * verticalSpacing) / 2) + (i * (buttonHeight + verticalSpacing));
            int x = screen.width - buttonWidth - sideMargin;
            
            Button button = Button.builder(
                Component.literal(label),
                btn -> {
                    // Directly handle the server transfer - no packets needed!
                    handleServerTransfer(command);
                }
            ).bounds(x, y, buttonWidth, buttonHeight).build();
            
            event.addListener(button);
        }
    }
    
    private static void handleServerTransfer(String command) {
        System.out.println("[WaystoneInjector] Handling server transfer: " + command);
        
        // Parse the redirect command to get the server address
        String serverAddress = parseRedirectAddress(command);
        if (serverAddress != null && !serverAddress.isEmpty()) {
            System.out.println("[WaystoneInjector] Connecting to server: " + serverAddress);
            connectToServer(serverAddress);
        } else {
            System.err.println("[WaystoneInjector] Could not parse server address from: " + command);
        }
    }
    
    private static String parseRedirectAddress(String command) {
        // Parse "redirect server.address.com" or "redirect @s server.address.com"
        String[] parts = command.trim().split("\\s+");
        if (parts.length >= 2 && parts[0].equalsIgnoreCase("redirect")) {
            // If second part is @s, use third part, otherwise use second part
            if (parts[1].equals("@s") && parts.length >= 3) {
                return parts[2];
            } else {
                return parts[1];
            }
        }
        return null;
    }
    
    private static void connectToServer(String serverAddress) {
        Minecraft mc = Minecraft.getInstance();
        
        // Parse address and port
        String[] parts = serverAddress.split(":");
        String address = parts[0];
        int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 25565;
        
        // Schedule the connection for next tick to ensure clean disconnect
        mc.execute(() -> {
            // Disconnect from current world
            if (mc.level != null) {
                mc.level.disconnect();
            }
            mc.clearLevel();
            
            // Create server data for the connection
            net.minecraft.client.multiplayer.ServerData serverData = 
                new net.minecraft.client.multiplayer.ServerData("", serverAddress, false);
            
            // Connect to the new server using a title screen as parent
            net.minecraft.client.gui.screens.Screen parentScreen = 
                mc.screen != null ? mc.screen : new net.minecraft.client.gui.screens.TitleScreen();
            
            net.minecraft.client.gui.screens.ConnectScreen.startConnecting(
                parentScreen,
                mc,
                new net.minecraft.client.multiplayer.resolver.ServerAddress(address, port),
                serverData,
                false
            );
        });
    }
}
