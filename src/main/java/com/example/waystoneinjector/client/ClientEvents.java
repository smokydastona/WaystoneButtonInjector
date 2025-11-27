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
        
        int numButtons = Math.min(labels.size(), commands.size());
        if (numButtons == 0) return;

        // Button dimensions
        int bw = 95;
        int bh = 20;
        int centerX = screen.width / 2;
        int bottomY = screen.height - 50;
        
        // Calculate spacing for buttons
        int totalWidth = (numButtons * bw) + ((numButtons - 1) * 5);
        int startX = centerX - (totalWidth / 2);

        // Add buttons dynamically from config
        for (int i = 0; i < numButtons; i++) {
            final int buttonIndex = i;
            String label = labels.get(i);
            String command = commands.get(buttonIndex);
            int x = startX + (i * (bw + 5));
            
            Button button = Button.builder(
                Component.literal(label),
                btn -> {
                    // Execute command on client
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.player != null) {
                        mc.player.connection.sendCommand(command);
                    }
                }
            ).bounds(x, bottomY, bw, bh).build();
            
            event.addListener(button);
        }
    }
}
