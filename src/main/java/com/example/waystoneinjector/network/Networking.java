package com.example.waystoneinjector.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class Networking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("waystoneinjector", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int id = 0;

    public static void register() {
        CHANNEL.registerMessage(id++, WaystoneButtonPacket.class, 
            WaystoneButtonPacket::encode, 
            WaystoneButtonPacket::decode, 
            WaystoneButtonPacket::handle, 
            Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
}
