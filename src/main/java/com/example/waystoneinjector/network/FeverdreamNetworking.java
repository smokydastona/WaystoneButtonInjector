package com.example.waystoneinjector.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class FeverdreamNetworking {
    private static final String PROTOCOL_VERSION = "1";
    
    // Listen on the same channel that Feverdream sends on
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("feverdreamrespawn", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int id = 0;

    public static void register() {
        CHANNEL.registerMessage(id++, 
            FeverdreamRedirectPacket.class, 
            FeverdreamRedirectPacket::encode, 
            FeverdreamRedirectPacket::decode, 
            FeverdreamRedirectPacket::handle, 
            Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}
