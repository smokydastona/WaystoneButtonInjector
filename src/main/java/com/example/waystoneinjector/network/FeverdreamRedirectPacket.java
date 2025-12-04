package com.example.waystoneinjector.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FeverdreamRedirectPacket {
    private final String serverName;

    public FeverdreamRedirectPacket(String serverName) {
        this.serverName = serverName;
    }

    public static void encode(FeverdreamRedirectPacket pkt, FriendlyByteBuf buf) {
        buf.writeUtf(pkt.serverName);
    }

    public static FeverdreamRedirectPacket decode(FriendlyByteBuf buf) {
        return new FeverdreamRedirectPacket(buf.readUtf());
    }

    public static void handle(FeverdreamRedirectPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Handle on client side
            com.example.waystoneinjector.client.FeverdreamHandler.handleRedirect(pkt.serverName);
        });
        ctx.get().setPacketHandled(true);
    }

    public String getServerName() {
        return serverName;
    }
}
