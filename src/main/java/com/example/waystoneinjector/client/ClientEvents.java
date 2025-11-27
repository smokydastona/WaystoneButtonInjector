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
        if (!className.equals("net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreen")) {
            return;
        }

        // Get config values
        List<? extends String> labels = WaystoneConfig.BUTTON_LABELS.get();
        List<? extends String> commands = WaystoneConfig.BUTTON_COMMANDS.get();
        
        int numButtons = Math.min(Math.min(labels.size(), commands.size()), 6); // Max 6 buttons
        if (numButtons == 0) return;

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
            String command = commands.get(buttonIndex);
            
            int y = centerY - ((leftButtons * buttonHeight + (leftButtons - 1) * verticalSpacing) / 2) + (i * (buttonHeight + verticalSpacing));
            
            Button button = Button.builder(
                Component.literal(label),
                btn -> {
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.player != null) {
                        mc.player.connection.sendCommand(command);
                    }
                }
            ).bounds(sideMargin, y, buttonWidth, buttonHeight).build();
            
            event.addListener(button);
        }
        
        // Add right side buttons
        for (int i = 0; i < rightButtons; i++) {
            final int buttonIndex = leftButtons + i;
            String label = labels.get(buttonIndex);
            String command = commands.get(buttonIndex);
            
            int y = centerY - ((rightButtons * buttonHeight + (rightButtons - 1) * verticalSpacing) / 2) + (i * (buttonHeight + verticalSpacing));
            int x = screen.width - buttonWidth - sideMargin;
            
            Button button = Button.builder(
                Component.literal(label),
                btn -> {
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.player != null) {
                        mc.player.connection.sendCommand(command);
                    }
                }
            ).bounds(x, y, buttonWidth, buttonHeight).build();
            
            event.addListener(button);
        }
    }
}
