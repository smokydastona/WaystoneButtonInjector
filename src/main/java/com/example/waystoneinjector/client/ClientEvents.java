package com.example.waystoneinjector.client;

import com.example.waystoneinjector.network.Networking;
import com.example.waystoneinjector.network.WaystoneButtonPacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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

        // Button dimensions and positions
        int bw = 95;
        int bh = 20;
        int centerX = screen.width / 2;
        int bottomY = screen.height - 50;

        // Chaos Town Button (left)
        Button chaosTown = Button.builder(
            Component.translatable("waystoneinjector.button.chaos_town"),
            btn -> Networking.CHANNEL.sendToServer(new WaystoneButtonPacket(0))
        ).bounds(centerX - 100, bottomY, bw, bh).build();
        event.addListener(chaosTown);

        // The Undergrown Button (right)
        Button undergrown = Button.builder(
            Component.translatable("waystoneinjector.button.undergrown"),
            btn -> Networking.CHANNEL.sendToServer(new WaystoneButtonPacket(1))
        ).bounds(centerX + 5, bottomY, bw, bh).build();
        event.addListener(undergrown);
    }
}
