package com.example.waystoneinjector.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ExecuteClientCommandPacket {
    private final String command;

    public ExecuteClientCommandPacket(String command) {
        this.command = command;
    }

    public static void encode(ExecuteClientCommandPacket pkt, FriendlyByteBuf buf) {
        buf.writeUtf(pkt.command);
    }

    public static ExecuteClientCommandPacket decode(FriendlyByteBuf buf) {
        return new ExecuteClientCommandPacket(buf.readUtf());
    }

    public static void handle(ExecuteClientCommandPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                mc.player.connection.sendCommand(pkt.command);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
